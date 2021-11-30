package com.example.retrofit.ui.uploadimage;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ImageResponse {
    @SerializedName("data")
    @Expose
    private List<ImageModel> imageModelList = null;

    public List<ImageModel> getImageModelList() {
        return imageModelList;
    }

    public void setImageModelList(List<ImageModel> imageModelList) {
        this.imageModelList = imageModelList;
    }
}
