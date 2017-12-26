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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

import static java.lang.Double.NaN;
import static java.lang.Double.doubleToLongBits;
import static java.lang.Double.isNaN;
import static java.lang.Double.parseDouble;

public class MainActivity extends AppCompatActivity {

    String jsonString;
    LinkedList<Integer> idList=new LinkedList<>();
    LinkedList<String>nameList=new LinkedList<>();
    LinkedList<Double>amountList=new LinkedList<>();
    LinkedList<String>dateList=new LinkedList<>();
    LinkedList<Double>ePriceList=new LinkedList<>();
    LinkedList<Double>uPriceList=new LinkedList<>();
    LinkedList<Double>pPriceList=new LinkedList<>();
    double myMoneyUSD=0,myMoneyEUR=0,myMoneyPLN=0;
    public void shared() throws JSONException {  //GET DATA FROM SHARED PREFERENCES
        SharedPreferences sharedPreferences;
        sharedPreferences=getSharedPreferences("harkor.myCrypto", Context.MODE_PRIVATE);
        jsonString=sharedPreferences.getString("jsonString","{\n" +
                "       \"cryptoList\":[\n" +
                "       {\"crypto\":\"BTC\",\"amount\":0.01448812,\"date\":\"23-12-2017\",\"ePrice\":15000.0,\"uPrice\":16000.0,\"pPrice\":60000.0},\n" +
                "       {\"crypto\":\"SAFEX\",\"amount\":164.67,\"date\":\"23-12-2017\",\"ePrice\":15000.0,\"uPrice\":16000.0,\"pPrice\":60000.0},\n" +
                "       {\"crypto\":\"IOTA\",\"amount\":0.57,\"date\":\"23-12-2017\",\"ePrice\":15000.0,\"uPrice\":16000.0,\"pPrice\":60000.0},\n" +
                "       {\"crypto\":\"ADA\",\"amount\":58.76,\"date\":\"23-12-2017\",\"ePrice\":15000.0,\"uPrice\":16000.0,\"pPrice\":60000.0},\n" +
                "       {\"crypto\":\"XVG\",\"amount\":110.3,\"date\":\"23-12-2017\",\"ePrice\":15000.0,\"uPrice\":16000.0,\"pPrice\":60000.0},\n" +
                "       {\"crypto\":\"BCC\",\"amount\":0.00409767,\"date\":\"23-12-2017\",\"ePrice\":15000.0,\"uPrice\":16000.0,\"pPrice\":60000.0},\n" +
                "       {\"crypto\":\"LSK\",\"amount\":5.0,\"date\":\"23-12-2017\",\"ePrice\":20.0,\"uPrice\":21.0,\"pPrice\":80.0}\n" +
                "       ]\n" +
                "}");











        if(jsonString=="EMPTY"){
        Log.d("SHARED: ","EMPTY");
        }else {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("cryptoList");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject cryptoObj = jsonArray.getJSONObject(i);
                /////////////////////////////////////
                idList.add(i);
                nameList.add(cryptoObj.optString("crypto"));
                amountList.add(cryptoObj.optDouble("amount"));
                dateList.add(cryptoObj.optString("date"));
                ePriceList.add(cryptoObj.optDouble("ePrice"));
                uPriceList.add(cryptoObj.optDouble("uPrice"));
                pPriceList.add(cryptoObj.optDouble("pPrice"));
                //////////////////////////////////////
            }
            Log.d("SHARED: ","OK");
        }
    }

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
    //String price=null;
    public void checkPrice(String crypto, final String currency){

        String url="https://min-api.cryptocompare.com/data/pricemulti?fsyms=";
        for(int i=0;i<nameList.size();i++){
            url+=nameList.get(i);
            url+=",";
        }
        url+="&tsyms=EUR,USD,PLN";
        final RequestQueue queue = Volley.newRequestQueue(this);
        final TextView ammount=(TextView) findViewById(R.id.ammount);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        JSONObject jsonObj;
                        try {
                            jsonObj = new JSONObject(response);
                            //double tempPrice=0;
                            for(int i=0;i<jsonObj.length();i++){
                                //JSONArray jsonArray=jsonObj.getJSONArray(nameList.get(i));
                                JSONObject jsonObject=jsonObj.getJSONObject(nameList.get(i));
                                /*
                                if(currency=="EUR"){ //FOR EUR
                                    tempPrice=(double) jsonObject.get(0);
                                }else if(currency=="USD"){ //FOR USD
                                    tempPrice=(double) jsonObject.get(1);
                                }else{//FOR PLN
                                    tempPrice=(double) jsonObject.get(2);
                                }
                                */
                                double kEUR =jsonObject.getDouble("EUR");
                                double kUSD =jsonObject.getDouble("USD");
                                double kPLN =jsonObject.getDouble("PLN");
                                double tempAmount= amountList.get(i);
                                double tempSum=kPLN*tempAmount;
                                myMoneyPLN+=tempSum;
                            }
                            ammount.setText(String.valueOf(myMoneyPLN));




                            //price =jsonObj.getString(currency);
                            //Log.d("JSON","price: "+price);
                            //double priced=parseDouble(price);
                            //String show = String.valueOf(priced)+" "+currency;
                            //ammount.setText(show);

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
        /*
        Log.d("JSON2","price: "+price);
        if(price==null){
            Log.d("Check price ressult:","Price NULL");
            return NaN;
        }else{
            double priced=parseDouble(price);
            Log.d("Check price ressult:","price git:"+price);
            return  priced;
        }
        */

    }







    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPref;
        sharedPref=getSharedPreferences("harkor.mycryptocurrency",Context.MODE_PRIVATE);
        final String currency=sharedPref.getString("currency","USD");
        //String url="https://min-api.cryptocompare.com/data/price?fsym=BTC&tsyms="+currency;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            shared();
        } catch (JSONException e) {
            e.printStackTrace();
        }


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
