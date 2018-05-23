package ru.sv.shishkina.vkoauth.network;


import com.google.gson.annotations.SerializedName;

public class VkResponse<Data> {
    @SerializedName("response")
    private Data data;

    public Data getData() {
        return data;
    }
}
