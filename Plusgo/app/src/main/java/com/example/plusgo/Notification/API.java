package com.example.plusgo.Notification;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface API {

    @FormUrlEncoded
    @POST("send")
    Call<ResponseBody> sendNotification(
            @Field("token") String token,
            @Field("title") String title,
            @Field("body") String body,
            @Field("reqPassenger") String reqPassenger,
            @Field("reqDriver") String reqDriver,
            @Field("source") String source,
            @Field("destination") String destination,
            @Field("passengerToken") String passengerToken
    );


    @FormUrlEncoded
    @POST("send")
    Call<ResponseBody> AcceptNotification(
            @Field("token") String token,
            @Field("title") String title,
            @Field("body") String body
//            @Field("reqPassenger") String reqPassenger,
//            @Field("reqDriver") String reqDriver,
//            @Field("source") String source,
//            @Field("destination") String destination,
//            @Field("passengerToken") String passengerToken
    );
}
