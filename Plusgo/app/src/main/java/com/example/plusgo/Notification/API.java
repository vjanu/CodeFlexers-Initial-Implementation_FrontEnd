package com.example.plusgo.Notification;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface API {

    @FormUrlEncoded
    @POST("send")
    Call<ResponseBody> senNotification(
            @Field("token") String token,
            @Field("title") String title,
            @Field("body") String body
    );
}
