package com.example.shoppingapp;

public class ProjectModel {
    private String tvHeadline, tvDescription, tvPrice, tvProductType, tvBrand, tvGender;
    private String ivProductImage;
    private String key;

    public ProjectModel() {
    }

    public ProjectModel(String tvHeadline, String tvDescription, String tvPrice, String tvProductType, String tvBrand, String tvGender, String ivProductImage, String key) {
        this.tvHeadline = tvHeadline;
        this.tvDescription = tvDescription;
        this.tvPrice = tvPrice;
        this.tvProductType = tvProductType;
        this.tvBrand = tvBrand;
        this.tvGender = tvGender;
        this.ivProductImage = ivProductImage;
        this.key = key;
    }

    public String getTvHeadline() {
        return tvHeadline;
    }

    public void setTvHeadline(String tvHeadline) {
        this.tvHeadline = tvHeadline;
    }

    public String getTvDescription() {
        return tvDescription;
    }

    public void setTvDescription(String tvDescription) {
        this.tvDescription = tvDescription;
    }

    public String getTvPrice() {
        return tvPrice;
    }

    public void setTvPrice(String tvPrice) {
        this.tvPrice = tvPrice;
    }

    public String getTvProductType() {
        return tvProductType;
    }

    public void setTvProductType(String tvProductType) {
        this.tvProductType = tvProductType;
    }

    public String getTvBrand() {
        return tvBrand;
    }

    public void setTvBrand(String tvBrand) {
        this.tvBrand = tvBrand;
    }

    public String getTvGender() {
        return tvGender;
    }

    public void setTvGender(String tvGender) {
        this.tvGender = tvGender;
    }

    public String getIvProductImage() {
        return ivProductImage;
    }

    public void setIvProductImage(String ivProductImage) {
        this.ivProductImage = ivProductImage;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
