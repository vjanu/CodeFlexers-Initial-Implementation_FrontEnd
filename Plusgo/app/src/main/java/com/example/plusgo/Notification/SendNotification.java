package com.example.plusgo.Notification;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.plusgo.R;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class SendNotification extends AppCompatActivity {

    public TextView NotiTitle,NotiBody,NotiToken;
    public Button NotiSend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notification);

        NotiTitle = (findViewById(R.id.txttitle));
        NotiBody = (findViewById(R.id.txtbody));
        //NotiToken = (findViewById(R.id.txttoken));

        findViewById(R.id.btnSend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNotification();
            }
        });

    }

    private void sendNotification(){
        String title = "Ride Request";
        String body = NotiBody.getText().toString().trim();
        String token = "dZLuDis_9tY:APA91bGuuAfA4FsmW_YuKRdadP4fKuyDHkBbCjVbqLn_qcrYgjSQfkWKrj-vDUzSIXKznFc6DB10yAIVeuHeHPeeSzXQmNOUXwh_YVCcGf3a1YyZcoVt2NiVRibC7UdZXU7VJVfjbjKt";
        String reqPassenger = "Surath Gunawardena";
        String reqDriver = "U1111111-Driver";
        String source = "Kaduwela";
        String destination = "Battaramulla";
        String passengerToken = "eQ1tTDeDAk8:APA91bHDVmf-2pd5qlEUxwFoR_ENMeTYEBENoyG7infabGxNXo51Mqkg1UkbWddVih29qS44UpIed8_qrKSUwijvDVpj3iBogsrOsOImMAjT60ikts4Fu0K3Ez-RVuycWkEoJJo6Uq3m";



        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://plusgo-ce90f.firebaseapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        API api = retrofit.create(API.class);
        Call<ResponseBody> call = api.sendNotification(token,title,body,reqPassenger,reqDriver,source,destination,passengerToken);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Toast.makeText(SendNotification.this,response.body().string(),Toast.LENGTH_LONG).show();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });
    }


}
