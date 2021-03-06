package com.application.android.verilerialmakap;

import androidx.appcompat.app.AppCompatActivity;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class MainActivity extends AppCompatActivity
{
    TextView description, temp, pressure,humidity, tempMin,tempMax,mainDescription,Feels_Like;
    EditText editText;
    String cityName,temperature,basinc,aciklama,nem,minumum,maximum;
    Button search;
    ImageView instantweathericon;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        description = findViewById(R.id.description);
        temp = findViewById(R.id.temperature);
        pressure = findViewById(R.id.pressure);
        humidity = findViewById(R.id.humidity);
        tempMin = findViewById(R.id.tempmin);
        tempMax = findViewById(R.id.tempmax);
        mainDescription = findViewById(R.id.main);
        Feels_Like = findViewById(R.id.tempmax2);
        instantweathericon = findViewById(R.id.weathericon);
        instantweathericon.setImageResource(R.drawable.cloudcomputing);

        editText = findViewById(R.id.editText);
        cityName = editText.getText().toString();

        editText.setText("istanbul");

    }

    public void MyFavoriteScreen(View view){
        Intent intent = new Intent(MainActivity.this,MyFavoritesActivity.class);
        startActivity(intent);
    }

    public void AddMyFavorite(View view){
        GetTextView();


    }

    public void GetTextView(){

        cityName = editText.getText().toString();
        temperature = temp.getText().toString();
        basinc = pressure.getText().toString();
        aciklama = description.getText().toString();
        nem = humidity.getText().toString();
        minumum = tempMin.getText().toString();
        maximum = tempMax.getText().toString();

    }

    public void TakeData(View view)
    {
        editText = findViewById(R.id.editText);
        cityName = editText.getText().toString();

        if (cityName.equals(""))
        {
            Toast.makeText(MainActivity.this,"enter a city name",Toast.LENGTH_LONG).show();
        }
        else {
            cityName = editText.getText().toString();
        }

        GetDataFromService getDataFromService = new GetDataFromService();
        getDataFromService.execute();

    }

    @SuppressLint("StaticFieldLeak")
    class GetDataFromService extends AsyncTask<Void, Void, String>
    {
        @Override
        protected String doInBackground(Void... voids)
        {
            try {
                URL url = new URL("http://api.openweathermap.org/data/2.5/weather?q=" + cityName +
                        "&appid=d51e2d9730e8f6acf7470373383b324a");
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(httpURLConnection.getInputStream()));

                StringBuilder result = new StringBuilder();
                String line = "";

                while ((line = bufferedReader.readLine()) != null)
                {
                    result.append(line);
                }
                bufferedReader.close();
                httpURLConnection.disconnect();
                return result.toString();
            }
            catch (MalformedURLException e)
            {
                System.out.println("hata : " + e.getMessage());
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {

                JSONObject jsonObject = new JSONObject(s);
                String weatherData = jsonObject.getString("weather");
                JSONArray jsonArray = new JSONArray(weatherData);
                String main = " ";
                String description1 = " ";

                for (int i =0; i<jsonArray.length(); i++)
                {
                    JSONObject weatherPart = jsonArray.getJSONObject(i);
                    main = weatherPart.getString("main");
                    description1 = weatherPart.getString("description");
                 }

                JSONObject results = jsonObject.getJSONObject("main");
                String temperature = results.getString("temp");
                String pressure1 = results.getString("pressure");
                String humidity1 = results.getString("humidity");
                String temp_min1 = results.getString("temp_min");
                String temp_max1 = results.getString("temp_max");
                String feels_like = results.getString("feels_like");

                float kelvin = Float.parseFloat(temperature);
                float newTemp = kelvin - 273 ;
                int newTemp2 = (int) newTemp;
                String temperature2 = String.valueOf(newTemp2);
                temp.setText("Now:"+" "+temperature2 + "˚C");

                if (newTemp2<0){
                    instantweathericon.setImageResource(R.drawable.igloo);
                }
                else if (newTemp2 > 0 && newTemp2 < 5) {
                    instantweathericon.setImageResource(R.drawable.cold);
                }
                else if (mainDescription.getText().toString().equals("Weather:Clouds")) {
                    instantweathericon.setImageResource(R.drawable.cloudyone);
                }
                else if (mainDescription.getText().toString().equals("Weather:Rain")) {
                    instantweathericon.setImageResource(R.drawable.rainy);
                }
                else if (newTemp2 > 5 && newTemp2 < 10){
                    instantweathericon.setImageResource(R.drawable.cloudy);
                }
                else if (newTemp2 > 10 && newTemp2 < 15){
                    instantweathericon.setImageResource(R.drawable.rain);
                }
                else if (newTemp2 > 15 && newTemp2 < 20 ){
                    instantweathericon.setImageResource(R.drawable.rainy);
                }
                else if (newTemp2 > 20) {
                    instantweathericon.setImageResource(R.drawable.sun);
                }

                float kelvin1 = Float.parseFloat(temp_min1);
                float newTemp_Min = kelvin1 - 273.15F;
                int newTemp_Min2 = (int) newTemp_Min;
                String temp_Min2 = String.valueOf(newTemp_Min2);
                tempMin.setText("Minumum temperature:"+" "+ temp_Min2 + "˚C");

                float kelvin2 = Float.parseFloat(temp_max1);
                float newTemp_Max = kelvin2 - 273.15F;
                int newTemp_Max2 = (int) newTemp_Max;
                String temp_Max2 = String.valueOf(newTemp_Max2);
                tempMax.setText("Maximum temperature:"+" "+temp_Max2 + "˚C");

                float kelvin3 = Float.parseFloat(feels_like);
                float newFellsLike = kelvin3 - 273.15F;
                int newFellsLike2 = (int) newFellsLike;
                String Feels_Like2 = String.valueOf(newFellsLike2);

                Feels_Like.setText("Feels Like:"+" "+Feels_Like2 + "˚C");
                humidity.setText("Humidity:"+" "+"%" + humidity1);
                pressure.setText("Pressure:"+pressure1 + "hPa");
                description.setText("Description:" + description1);
                mainDescription.setText("Weather:" + main);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

}