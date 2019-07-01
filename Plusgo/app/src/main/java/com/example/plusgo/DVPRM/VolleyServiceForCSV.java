package com.example.plusgo.DVPRM;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request.Method;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.Request;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.plusgo.BaseContent;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class VolleyServiceForCSV {

    private BaseContent BASECONTENT = new BaseContent();
    private String RATING_URL = BASECONTENT.IpAddress + "/ratings/personals/";
    private String CSVWRITING_URL = BASECONTENT.pythonIpAddress + "/update/";

    IResult mResultCallback = null;
    Context mContext;
    private JsonArrayRequest request;
    private RequestQueue requestQueue;

    VolleyServiceForCSV(IResult resultCallback, Context context) {
        mResultCallback = resultCallback;
        mContext = context;
    }

    //Retrieve average Rating
    public void jsonrequestforRating(String UID) {
        final String userID = UID;
        String JSON_URL = RATING_URL + UID;

        Log.e("JSONREQUEST", "started");
        Log.e("JSON_URL_FIRST", JSON_URL);
        final String finalJSON_URL = JSON_URL;

        request = new JsonArrayRequest(JSON_URL, new Response.Listener<JSONArray>() {
            public void onResponse(JSONArray response) {
                Log.e("JSON_URL", finalJSON_URL);
                JSONObject jsonObject = null;
                if (response.length() > 0) {
                    try {
                        jsonObject = response.getJSONObject(0);
                        String Rating = jsonObject.getString("Rating");
                        Log.e("Rating", Rating);

                        WriteToCSV(userID, Rating);

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Log.e("JSONREQUEST", "ERROR");
                    }
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Log.e("JSONREQUEST_ERROR", error.toString());
            }
        });
        request.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(request);
    }


    //Save the average rating to CSV
    private void WriteToCSV(String UID, String Rating) {
        String JSON_URL = CSVWRITING_URL + UID + "/" + Rating;

        Log.e("JSONREQUEST_WRITETOCSV", "started");
        Log.e("JSON_URL_FIRST", JSON_URL);
        final String finalJSON_URL = JSON_URL;
        StringRequest stringRequest = new StringRequest(
                Request.Method.GET,
                finalJSON_URL,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("JSONREQUEST_SUCCESS",response.toString() );
                        requestQueue.stop();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        requestQueue.stop();
                        Log.e("JSONREQUEST_ERROR", error.toString());
                    }
                }
        );
        stringRequest.setRetryPolicy(new DefaultRetryPolicy(0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        requestQueue = Volley.newRequestQueue(mContext);
        requestQueue.add(stringRequest);
    }
}
