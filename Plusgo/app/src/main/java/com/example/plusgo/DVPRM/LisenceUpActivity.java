package com.example.plusgo.DVPRM;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.plusgo.R;

public class LisenceUpActivity extends AppCompatActivity {

    Button uploadLISbtn,verifyLISbtn,proceedLISbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lisence_up);

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
