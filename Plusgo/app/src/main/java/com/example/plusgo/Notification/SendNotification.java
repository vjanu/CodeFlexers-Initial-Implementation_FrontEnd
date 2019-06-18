package com.example.plusgo.Notification;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import com.example.plusgo.R;

public class SendNotification extends AppCompatActivity {

    public TextView NotiTitle,NotiBody,NotiToken;
    public Button NotiSend;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_notification);

        NotiTitle = (findViewById(R.id.txttitle));
        NotiBody = (findViewById(R.id.txtbody));
        NotiToken = (findViewById(R.id.txttoken));

        findViewById(R.id.btnSend).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendNotification();
            }
        });

    }

    private void sendNotification(){
        String title = NotiTitle.getText().toString().trim();
        String body = NotiBody.getText().toString().trim();
        String token = NotiToken.getText().toString().trim();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://fnotification-2e302.firebaseapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        API api = retrofit.create(API.class);
        Call<ResponseBody> call = api.senNotification(token,title,body);

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
