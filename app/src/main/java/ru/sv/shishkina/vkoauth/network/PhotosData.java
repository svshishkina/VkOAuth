package ru.sv.shishkina.vkoauth.network;


import com.google.gson.annotations.SerializedName;

import java.util.List;

import ru.sv.shishkina.vkoauth.model.Photo;

public class PhotosData {
    @SerializedName("items")
    private List<Photo> photos;

    public List<Photo> getPhotos() {
        return photos;
    }
}
