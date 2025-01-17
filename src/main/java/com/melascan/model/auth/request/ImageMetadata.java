package com.melascan.model.auth.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ImageMetadata {

    public ImageMetadata(String imageName, String imageUrl, long size, String lastModified) {
        this.imageName = imageName;
        this.imageUrl = imageUrl;
        this.size = size;
        this.lastModified = lastModified;
    }

    private String imageName;
    private String imageUrl;
    private long size;
    private String lastModified;
}
