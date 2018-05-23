package ru.sv.shishkina.vkoauth.network;


import android.util.Log;

import java.io.IOException;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import ru.sv.shishkina.vkoauth.BuildConfig;
import ru.sv.shishkina.vkoauth.model.Photo;
import ru.sv.shishkina.vkoauth.model.User;

public class ApiService {
    public static final String CLIENT_ID = "";
    public static final String REDIRECT_URL = "";
    public static final String API_VERSION = "5.76";
    public static final String FIELDS = "photo_max";

    public static final String AUTH_URL = "https://oauth.vk.com/authorize?client_id=" + CLIENT_ID
            + "&redirect_uri=" + REDIRECT_URL + "&display=mobile&scope=photos&response_type=token&state=";

    private static volatile ApiService instance;

    public static ApiService getInstance() {
        if (null == instance) {
            synchronized (ApiService.class) {
                if (null == instance) {
                    instance = new ApiService();
                }
            }
        }
        return instance;
    }

    private final OkHttpClient okHttpClient;
    private final Retrofit retrofit;
    private final VkService service;

    private String accessToken;
    private long userId;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    private ApiService() {
        okHttpClient = buildHttpClient();
        retrofit = buildRetrofit(okHttpClient);
        service = retrofit.create(VkService.class);
    }

    private OkHttpClient buildHttpClient() {
        final OkHttpClient.Builder okHttpClientBuilder =  new OkHttpClient.Builder();
        if(BuildConfig.DEBUG){
            HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            okHttpClientBuilder.addInterceptor(interceptor);
        }
        return okHttpClientBuilder.build();
    }

    private Retrofit buildRetrofit(OkHttpClient okHttpClient) {
        return new Retrofit.Builder()
                .baseUrl("https://api.vk.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();
    }

    public User getUserInfo() {
        if (null != accessToken) {
            User user;

            Call<VkResponse<List<User>>> call = service.getUserInfo(accessToken, API_VERSION, FIELDS);

            try {
                Response<VkResponse<List<User>>> vkResponse = call.execute();
                VkResponse<List<User>> apiReponse = vkResponse.body();
                user = apiReponse.getData().get(0);
            } catch (IOException e) {
                Log.e("ApiService", "Oops!", e);
                user = null;
            }

            return user;
        }

        return null;
    }

    public List<Photo> getUserPhotos() {
        List<Photo> photos;

        Call<VkResponse<PhotosData>> call = service.getUserPhotos(accessToken, API_VERSION, userId, 7, 1);

        try {
            Response<VkResponse<PhotosData>> vkResponse = call.execute();
            VkResponse<PhotosData> apiReponse = vkResponse.body();
            photos = apiReponse.getData().getPhotos();
            photos.remove(0);
        } catch (IOException e) {
            Log.e("ApiService", "Oops!", e);
            photos = null;
        }

        return photos;
    }
}
