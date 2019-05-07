/*
 * *
 *  * Created by Viraj Wickramasinghe
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 5/5/19 8:18 PM
 *
 */

package com.example.plusgo.UPM;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;

import com.example.plusgo.R;

public class NewUserActivity extends AppCompatActivity {
    private Spinner profession;
    private ImageButton goToPreferences;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        profession = findViewById(R.id.profession);
        String[] prof = new String[]{"Driver", "Body Guard", "Security Officer","Clerical Staff", "Clerk", "Intern", "Administrative Assistant","Associate Engineer","Bank Assistant","IT Support",
                "Cashier","Network Engineer","Software Engineer","Database Administrator", "Project Manager","HR","Nurse","Lecturer","Teacher",
                "Tech Lead","Doctor","Lawyer","Professor","Senior Lecturer","Senior Lead","Senior Accountant","CEO","CIO","IT Director","Manager"};
        ArrayAdapter<String> adapterProf = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, prof);
        profession.setAdapter(adapterProf);

        goToPreferences = (ImageButton)findViewById(R.id.btnPreference);
        goToPreferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent preferences = new Intent(NewUserActivity.this, AddPreferenceActivity.class);
                startActivity(preferences);
            }
        });


    }
}
