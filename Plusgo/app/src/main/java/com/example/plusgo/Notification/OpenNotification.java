package com.example.plusgo.Notification;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
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

public class OpenNotification extends AppCompatActivity {
    private TextView txtFullName,txtSource,txtDestination,txtHiddenPassengerToken,txtHiddenPassengerid;
    private Button btnAccept,btnDecline;

    public OpenNotification() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_notification);
        txtFullName = (findViewById(R.id.txtFullName));
        txtSource = (findViewById(R.id.txtSource));
        txtDestination = (findViewById(R.id.txtDestination));
        txtHiddenPassengerToken = (findViewById(R.id.txtHiddenPassengerToken));
        txtHiddenPassengerid = (findViewById(R.id.txtHiddenPassengerid));
        btnAccept = (findViewById(R.id.btnAccept));
        btnDecline = (findViewById(R.id.btnDecline));

        String notificationBody = MyFirebaseMessagingService.NotificationBodyCatcher;
        Log.d("Check" , notificationBody);
//        textView.setText(notificationBody);

        //Set Variables to Text Views
        String Full_Name = notificationBody.split("\n")[1];
        txtFullName.setText(Full_Name);

        String Source = notificationBody.split("\n")[3];
        txtSource.setText(Source);

        String Destination = notificationBody.split("\n")[4];
        txtDestination.setText(Destination);

        String passengerToken = notificationBody.split("\n")[5];
        txtHiddenPassengerToken.setText(passengerToken);

        String reqPassengerId = notificationBody.split("\n")[6];
        txtHiddenPassengerid.setText(reqPassengerId);




        findViewById(R.id.btnAccept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AcceptRide();
            }
        });
    }




    //Accept Message
    private void AcceptRide(){
        String title = "Ride Confirmation";
        String body = "Driver will arriving soon";
        String passengerToken = "eQ1tTDeDAk8:APA91bHDVmf-2pd5qlEUxwFoR_ENMeTYEBENoyG7infabGxNXo51Mqkg1UkbWddVih29qS44UpIed8_qrKSUwijvDVpj3iBogsrOsOImMAjT60ikts4Fu0K3Ez-RVuycWkEoJJo6Uq3m";


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://plusgo-ce90f.firebaseapp.com/api/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        API api = retrofit.create(API.class);
        Call<ResponseBody> call = api.AcceptNotification(passengerToken,title,body);

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    Toast.makeText(OpenNotification.this,response.body().string(),Toast.LENGTH_LONG).show();
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
