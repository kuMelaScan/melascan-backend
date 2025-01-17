package com.melascan.controller;

import com.melascan.model.auth.request.ImageMetadata;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.ListObjectsV2Request;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;
import software.amazon.awssdk.services.s3.presigner.S3Presigner;
import software.amazon.awssdk.services.s3.presigner.model.GetObjectPresignRequest;
import software.amazon.awssdk.services.s3.presigner.model.PresignedGetObjectRequest;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/images")
public class S3ImageController {

    private final S3Client s3Client;
    private final S3Presigner s3Presigner;
    private final String BUCKET_NAME = "";

    public S3ImageController(S3Client s3Client, S3Presigner s3Presigner) {
        this.s3Client = s3Client;
        this.s3Presigner = s3Presigner;
    }

    // Upload an image
    @PostMapping
    public ResponseEntity<String> uploadImage(@RequestParam("userId") String userId,
                                              @RequestParam("image") MultipartFile file) {
        try {
            String key = userId + "/" + file.getOriginalFilename();

            // Upload to S3
            s3Client.putObject(PutObjectRequest.builder()
                            .bucket(BUCKET_NAME)
                            .key(key)
                            .contentType(file.getContentType())
                            .build(),
                    software.amazon.awssdk.core.sync.RequestBody.fromBytes(file.getBytes()));

            return ResponseEntity.ok("Image uploaded successfully with key: " + key);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Image upload failed.");
        }
    }
    /**
     * Endpoint to list all images for a user with presigned GET URLs.
     */
    @GetMapping("/list/{userId}")
    public ResponseEntity<List<ImageMetadata>> listUserImages(@PathVariable String userId) {
        try {
            ListObjectsV2Request listRequest = ListObjectsV2Request.builder()
                    .bucket(BUCKET_NAME)
                    .prefix(userId + "/")
                    .build();

            List<ImageMetadata> imageMetadataList = s3Client.listObjectsV2(listRequest).contents().stream()
                    .map(s3Object -> {
                        // Create a Presigned URL for each object
                        String presignedUrl = generatePresignedGetUrl(s3Object.key());
                        return new ImageMetadata(
                                s3Object.key().replace(userId + "/", ""), // Image name
                                presignedUrl, // Presigned URL
                                s3Object.size(), // Size in bytes
                                s3Object.lastModified().toString() // Last modified date
                        );
                    })
                    .collect(Collectors.toList());

            return ResponseEntity.ok(imageMetadataList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Endpoint to get a presigned URL for downloading a specific image.
     */
    @GetMapping("/{userId}/{imageName}")
    public ResponseEntity<String> getImagePresignedUrl(@PathVariable String userId, @PathVariable String imageName) {
        try {
            String key = userId + "/" + imageName;

            // Generate the presigned URL
            String presignedUrl = generatePresignedGetUrl(key);

            return ResponseEntity.ok(presignedUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.internalServerError().body("Failed to generate presigned URL.");
        }
    }

    /**
     * Helper method to generate a presigned GET URL for an S3 object.
     */
    private String generatePresignedGetUrl(String key) {
        GetObjectPresignRequest presignRequest = GetObjectPresignRequest.builder()
                .getObjectRequest(b -> b.bucket(BUCKET_NAME).key(key))
                .signatureDuration(java.time.Duration.ofMinutes(60)) // URL valid for 60 minutes
                .build();

        PresignedGetObjectRequest presignedRequest = s3Presigner.presignGetObject(presignRequest);
        return presignedRequest.url().toString();
    }
}
