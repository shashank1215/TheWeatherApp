package com.codewithshashank.whatsweather;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
    Button button;boolean input=false;
    EditText editText;
    TextView textView;
    public  void function(View view) {
        if (input == false) {
            textView.setVisibility(View.VISIBLE);
            editText.setVisibility(View.INVISIBLE);
            button.setText("Back");input=true;
        }
        else{
            textView.setVisibility(View.INVISIBLE);
            editText.setVisibility(View.VISIBLE);
            button.setText("GO");input=false;


        }

        String data="";int temp=0;String description="";String windSpeed="";int cloudPercent=0;
        WeatherFinder finder=new WeatherFinder();
        try {
            data=finder.execute("http://api.openweathermap.org/data/2.5/weather?q="+editText.getText().toString()+"&APPID=*********************************").get();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        try {
            JSONObject object=new JSONObject(data);
            JSONObject main=object.getJSONObject("main");
            JSONObject wind=object.optJSONObject("wind");
            JSONObject clouds=object.optJSONObject("clouds");
            windSpeed=wind.getString("speed");
            cloudPercent=clouds.getInt("all");
           temp=main.getInt("temp");
           temp-=273;
            JSONArray weather=object.getJSONArray("weather");
                JSONObject obj= (JSONObject) weather.get(0);
            description=obj.getString("description");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if(description=="")
        {
            description="City not Found.\nMake sure city name is correct and you are connected to Internet."; textView.setText(description);
        }
        else textView.setText("Location:"+editText.getText().toString()+"\n"+description+"\nTemperature:"+temp+" C"+"\nWind speed:"+windSpeed+" m/s"+"\nCloud:"+cloudPercent+"%");

    }
    public class WeatherFinder extends AsyncTask<String,Void,String> {


        @Override
        protected String doInBackground(String... strings) {
            int i;
            char ch;
            String api = "";
            try {
                URL url = new URL(strings[0]);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream in = httpURLConnection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                i = reader.read();
                while (i != -1) {
                    ch = (char) i;
                    api += ch;
                    i=reader.read();
                }
                return api;
            } catch (Exception e) {
                e.printStackTrace();
                return "Failed";
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        button=findViewById(R.id.button);
        editText=findViewById(R.id.editText);
        textView=findViewById(R.id.textView);

    }
}
