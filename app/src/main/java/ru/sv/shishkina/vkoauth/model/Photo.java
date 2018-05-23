package ru.sv.shishkina.vkoauth.model;


import com.google.gson.annotations.SerializedName;

public class Photo {
    @SerializedName("photo_807")
    private String photoUrl;

    public String getPhotoUrl() {
        return photoUrl;
    }

    public void setPhotoUrl(String photoUrl) {
        this.photoUrl = photoUrl;
    }
}
