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
            @Field("passengerToken") String passengerToken,
            @Field("reqPassengerId") String reqPassengerId,
            @Field("driverId") String driverId,
            @Field("tripId") String tripId

    );


    @FormUrlEncoded
    @POST("send")
    Call<ResponseBody> AcceptNotification(
            @Field("token") String token,
            @Field("title") String title,
            @Field("body") String body
//            @Field("TripId") String TripId,
//            @Field("passengerId") String passengerId,
//            @Field("driverId") String driverId
//            @Field("reqPassenger") String reqPassenger,
//            @Field("reqDriver") String reqDriver,
//            @Field("source") String source,
//            @Field("destination") String destination,
//            @Field("passengerToken") String passengerToken
    );


    @FormUrlEncoded
    @POST("send")
    Call<ResponseBody> PriceNotification(
            @Field("token") String token,
            @Field("title") String title,
            @Field("body") String body
//            @Field("passengerId") String passengerId,
//            @Field("vehicleId") String vehicleId
//            @Field("reqPassenger") String reqPassenger,
//            @Field("reqDriver") String reqDriver,
//            @Field("source") String source,
//            @Field("destination") String destination,
//            @Field("passengerToken") String passengerToken
    );
}
