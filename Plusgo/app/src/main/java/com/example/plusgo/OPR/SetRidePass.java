/*
 * *
 *  * Created by Viraj Wickramasinghe
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 5/9/19 11:50 AM
 *
 */

/*
 * *
 *  * Created by Athrie
 *  *
 *  *
 *
 */

package com.example.plusgo.OPR;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.plusgo.R;

public class SetRidePass extends AppCompatActivity {

    EditText srctxt,destxt;
    Button search_btn;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setride);


        search_btn=findViewById(R.id.search_btn);
        search_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}
