package ru.sv.shishkina.vkoauth.network;


import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;
import ru.sv.shishkina.vkoauth.model.User;

public interface VkService {
    @GET("method/users.get")
    Call<VkResponse<List<User>>> getUserInfo(@Query("access_token") String accessToken,
                                             @Query("v") String apiVersion,
                                             @Query("fields") String fields);

    @GET("method/photos.getAll")
    Call<VkResponse<PhotosData>> getUserPhotos(@Query("access_token") String accessToken,
                                               @Query("v") String apiVersion,
                                               @Query("owner_id") long ownerId,
                                               @Query("count") int count,
                                               @Query("no_service_albums") int noServiceAlbums);
}
