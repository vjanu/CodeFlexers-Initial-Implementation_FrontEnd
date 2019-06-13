package com.example.plusgo.DVPRM;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.plusgo.R;

public class LisenceUpActivity extends AppCompatActivity {

    Button uploadLISbtn,verifyLISbtn,proceedLISbtn;
    TextView extractednic_lis;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lisence_up);
        SharedPreferences preferences = getSharedPreferences("dvprm", Context.MODE_PRIVATE);
        String nic_validated = preferences.getString("nic_validated", "000000000000");

        //Set this visible after comparing extracted nic from lisence, if not ask to upload another image
        extractednic_lis =(TextView)findViewById(R.id.extractednic_lis);
        extractednic_lis.setText(nic_validated);

        uploadLISbtn =(Button)findViewById(R.id.lis_upload_up);
        uploadLISbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent verify = new Intent(LisenceUpActivity.this, DocUploadActivity.class);
                startActivity(verify);
            }
        });
    }
}
