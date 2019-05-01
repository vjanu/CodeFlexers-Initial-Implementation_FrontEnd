/*
 * *
 *  * Created by Athrie
 *  *
 *  *
 *
 */

package com.example.plusgo;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class SetRidePass extends AppCompatActivity {

    EditText srctxt,destxt;
    Button search_btn;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setride);

        srctxt=findViewById(R.id.srctxt);
        destxt=findViewById(R.id.destxt);
        search_btn=findViewById(R.id.search_btn);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
