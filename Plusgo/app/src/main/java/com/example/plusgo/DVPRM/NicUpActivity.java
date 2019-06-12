package com.example.plusgo.DVPRM;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.plusgo.R;

public class NicUpActivity extends AppCompatActivity {

    Button uploadNICbtn,verifyNICbtn,proceedNICbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nic_up);

        uploadNICbtn =(Button)findViewById(R.id.nic_upload_up);
        uploadNICbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent verify = new Intent(NicUpActivity.this, DocUploadActivity.class);
                startActivity(verify);
            }
        });

        proceedNICbtn =(Button)findViewById(R.id.proceednic);
        proceedNICbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent verify1 = new Intent(NicUpActivity.this, LisenceUpActivity.class);
                startActivity(verify1);
                finish();
            }
        });
    }
}
