package com.example.vollylib;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class MainActivity extends AppCompatActivity {
    TextView winfo;
    EditText cityName;

    String message=null;
    String encodeCity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        winfo=findViewById(R.id.textView2);
        cityName=findViewById(R.id.editText);



    }

    public void onButtonEnter(View view) {
        try {
            encodeCity= URLEncoder.encode(cityName.getText().toString(),"UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        String URL="http://api.openweathermap.org/data/2.5/weather?q="+encodeCity+"&APPID=4664daf97c1e25e592e4b39e34d9f301";
        final RequestQueue queue= Volley.newRequestQueue(this);
        JsonObjectRequest stringRequest=new JsonObjectRequest(Request.Method.GET, URL, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray array=response.getJSONArray("weather");
                    JSONObject details=array.getJSONObject(0);
                    StringBuffer buffer=new StringBuffer();
                    buffer.append(details.getString("main")+":");
                    buffer.append(details.getString("description")+"\n");
                    message =buffer.toString();

                    JSONObject obj=response.getJSONObject("main");
                    double tp=obj.getDouble("temp")-273.15;
                    String temp=String.format("0.2f",tp);
                    winfo.setText(message+"\n temp: "+tp+"C");


                } catch (JSONException e) {
                    e.printStackTrace();
                }



            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                winfo.setText("");
                Toast.makeText(MainActivity.this,"no data to show",Toast.LENGTH_SHORT).show();

            }
        });
        queue.add(stringRequest);
        InputMethodManager manager=(InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(cityName.getWindowToken(),0);

    }
}
