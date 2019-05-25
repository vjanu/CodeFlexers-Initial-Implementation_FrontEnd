/*
 * *
 *  * Created by Viraj Wickramasinghe
 *  * Copyright (c) 2019 . All rights reserved.
 *  * Last modified 5/8/19 11:14 PM
 *
 */

package com.example.plusgo.UPM;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.plusgo.R;

public class VehicleActivity extends AppCompatActivity {
    private Spinner brand, model, manYear, regYear, fuelType, tType, capacity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vehicle);

        brand = findViewById(R.id.brand);
        String[] brandV = new String[]{"Alto"};
        ArrayAdapter<String> adapterBrand = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, brandV);
        brand.setAdapter(adapterBrand);

        model = findViewById(R.id.model);
        String[] modelV = new String[]{"Maruti Alto 800"};
        ArrayAdapter<String> adapterModel = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, modelV);
        model.setAdapter(adapterModel);

        manYear = findViewById(R.id.mYear);
        String[] manYearV = new String[]{"1980"};
        ArrayAdapter<String> adapterManYear = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, manYearV);
        manYear.setAdapter(adapterManYear);

        regYear = findViewById(R.id.rYear);
        String[] regYearV = new String[]{"1980"};
        ArrayAdapter<String> adapterRegYear = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, regYearV);
        regYear.setAdapter(adapterRegYear);

        fuelType = findViewById(R.id.fuel);
        String[] fuelTypeV = new String[]{"Petrol"};
        ArrayAdapter<String> adapterFuelTypeV = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, fuelTypeV);
        fuelType.setAdapter(adapterFuelTypeV);

        tType = findViewById(R.id.transmission);
        String[] tTypeV = new String[]{"Auto"};
        ArrayAdapter<String> adapterTTypeV = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, tTypeV);
        tType.setAdapter(adapterTTypeV);

        capacity = findViewById(R.id.engine);
        String[] capacityV = new String[]{"1000cc"};
        ArrayAdapter<String> adapterCapacity= new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, capacityV);
        capacity.setAdapter(adapterCapacity);
    }
}
