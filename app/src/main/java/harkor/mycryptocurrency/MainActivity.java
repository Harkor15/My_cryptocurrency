package harkor.mycryptocurrency;

import android.app.DownloadManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import static java.lang.Double.NaN;
import static java.lang.Double.doubleToLongBits;
import static java.lang.Double.isNaN;
import static java.lang.Double.parseDouble;

public class MainActivity extends AppCompatActivity {

    public void settingsss(View view){
        Intent settingss= new Intent(this,settings.class);
        startActivity(settingss);
    }
    public void myCryptooo(View view){
        Intent myCryptoo= new Intent(this,myCrypto.class);
        startActivity(myCryptoo);
    }
    public void timerSet(){
        final TextView time=(TextView) findViewById(R.id.time);
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd-MM-yyyy");
        String formattedTime = sdf.format(now);
        time.setText(formattedTime);
    }
    String price=null;
    public double checkPrice(String crypto, final String currency){

        String url="https://min-api.cryptocompare.com/data/price?fsym="+crypto+"&tsyms="+currency;
        final RequestQueue queue = Volley.newRequestQueue(this);
        final TextView ammount=(TextView) findViewById(R.id.ammount);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){


                    @Override
                    public void onResponse(String response){
                        JSONObject jsonObj = null;
                        try {
                            jsonObj = new JSONObject(response);
                            price =jsonObj.getString(currency);
                            Log.d("JSON","price: "+price);
                            double priced=parseDouble(price);
                            String show = String.valueOf(priced)+" "+currency;
                            ammount.setText(show);
                            timerSet();
                        } catch (JSONException e) {
                            ammount.setText("Error");
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                error.printStackTrace();
                queue.stop();
            }
        });
        queue.add(stringRequest);
        Log.d("JSON2","price: "+price);
        if(price==null){
            Log.d("Check price ressult:","Price NULL");
            return NaN;
        }else{
            double priced=parseDouble(price);
            Log.d("Check price ressult:","price git:"+price);
            return  priced;
        }

    }







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPref;
        sharedPref=getSharedPreferences("harkor.mycryptocurrency",Context.MODE_PRIVATE);
        final String currency=sharedPref.getString("currency","USD");
        //String url="https://min-api.cryptocompare.com/data/price?fsym=BTC&tsyms="+currency;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        checkPrice("BTC",currency);
       /* if(prc!=NaN){ // WE HAVE DATA FROM URL



        }
        else{ //ERROR WHEN ACCESING TO URL

        }
        */





 /*       final RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    String price="Error";
                    @Override
                    public void onResponse(String response){
                        JSONObject jsonObj = null;
                        try {
                            jsonObj = new JSONObject(response);
                            price=jsonObj.getString(currency);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        //JSONObject c = contacts.getJSONObject(1);

                        double priced=parseDouble(price);
                        String show = String.valueOf(priced)+" "+currency;
                       // String show=toString(priced);//+" "+currency;
                        ammount.setText(show);

                        ////////// TIME
                        //Date currentTime = Calendar.getInstance().getTime();
                        //int showTime=
                        /*Calendar rightNow = Calendar.getInstance();

                        String currentTime=String.valueOf(rightNow.HOUR_OF_DAY)+":"+
                                String.valueOf(Calendar.MINUTE)+" "+
                                String.valueOf(Calendar.YEAR);


                        timerSet();


                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                ammount.setText("ERROR");
                error.printStackTrace();
                queue.stop();
            }
        });
        queue.add(stringRequest);*/
    }


}
