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
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedList;

import static java.lang.Double.NaN;
import static java.lang.Double.doubleToLongBits;
import static java.lang.Double.isNaN;
import static java.lang.Double.parseDouble;

public class MainActivity extends AppCompatActivity {
    double myMoneyUSD=0,myMoneyEUR=0,myMoneyPLN=0;

    String jsonString;
    LinkedList<Integer> idList=new LinkedList<>();
    LinkedList<String>nameList=new LinkedList<>();
    LinkedList<Double>amountList=new LinkedList<>();
    //LinkedList<String>dateList=new LinkedList<>();
    //LinkedList<Double>ePriceList=new LinkedList<>();
    //LinkedList<Double>uPriceList=new LinkedList<>();
    //LinkedList<Double>pPriceList=new LinkedList<>();

    public void sharedAdd(String tag,Double amo){
        int len=nameList.size();
        if(len==0){
            idList.add(0);
            nameList.add(tag);
            amountList.add(amo);
        }else{
            int poz=sharedAdd2(tag);
            if(poz<0){
                idList.add(len);
                nameList.add(tag);
                amountList.add(amo);
            }else{
                Double tmp=amountList.get(poz);
                tmp+=amo;
                amountList.set(poz,tmp);
            }
        }
    }
    public int sharedAdd2(String tag){
        int len=nameList.size();
        for(int i=0;i<len;i++){
            if(nameList.get(i).equals(tag)){
            return i;
            }
        }
        return -1;
    }
    public void shared() throws JSONException {  //GET DATA FROM SHARED PREFERENCES
        SharedPreferences sharedPreferences;
        sharedPreferences=getSharedPreferences("harkor.myCrypto", Context.MODE_PRIVATE);
        jsonString=sharedPreferences.getString("jsonString","{\"cryptoList\":[]}");
        Log.d("JSONstring: ",jsonString);
        if(jsonString=="{\"cryptoList\":[]}"){
            Log.d("SHARED","EMPTY");
            Toast.makeText(MainActivity.this,"Nie ma krypto!",Toast.LENGTH_SHORT).show();
        }else {
            JSONObject jsonObject = new JSONObject(jsonString);
            JSONArray jsonArray = jsonObject.getJSONArray("cryptoList");
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject cryptoObj = jsonArray.getJSONObject(i);
                /////////////////////////////////////
                sharedAdd(cryptoObj.optString("crypto"),cryptoObj.optDouble("amount"));
                //idList.add(i);
                //nameList.add(cryptoObj.optString("crypto"));
                //Log.d("Crypto name: ", cryptoObj.optString("crypto"));
                //amountList.add(cryptoObj.optDouble("amount"));
                //dateList.add(cryptoObj.optString("date"));
                //ePriceList.add(cryptoObj.optDouble("ePrice"));
                //uPriceList.add(cryptoObj.optDouble("uPrice"));
                //pPriceList.add(cryptoObj.optDouble("pPrice"));
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
    public void setPrice(){
        final TextView ammount=(TextView) findViewById(R.id.ammount);
        SharedPreferences sharedPref;
        sharedPref=getSharedPreferences("harkor.mycryptocurrency",Context.MODE_PRIVATE);
        String currency=sharedPref.getString("currency","USD");
        DecimalFormat decim = new DecimalFormat("##.##");
        String showStr;
        Log.d("ActualCurr: ",currency);
        if(currency.equalsIgnoreCase("EUR")){ //FOR EUR
            showStr=String.valueOf(decim.format(myMoneyEUR))+"€";
            Log.d("Curr: ","EUR");
        }else if(currency.equalsIgnoreCase("USD")){ //FOR USD
            showStr=String.valueOf(decim.format(myMoneyUSD))+"$";
            Log.d("Curr: ","USD");
        }else{//FOR PLN
            showStr=String.valueOf(decim.format(myMoneyPLN))+"zł";
            Log.d("Curr: ","PLN");
        }
        ammount.setText(showStr );
    }
    public void checkPrice(){
        myMoneyUSD=0;myMoneyEUR=0;myMoneyPLN=0;
        String url="https://min-api.cryptocompare.com/data/pricemulti?fsyms=";
        for(int i=0;i<nameList.size();i++){
            url+=nameList.get(i);
            url+=",";
        }
        url+="&tsyms=EUR,USD,PLN";
        Log.d("URL: ",url);
        final RequestQueue queue = Volley.newRequestQueue(this);
        final TextView ammount=(TextView) findViewById(R.id.ammount);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response){
                        JSONObject jsonObj;

                        Log.d("Response: ",response);
                        try {
                            jsonObj = new JSONObject(response);
                            for(int i=0;i<jsonObj.length();i++){
                                JSONObject jsonObject=jsonObj.getJSONObject(nameList.get(i));
                                double kEUR =jsonObject.getDouble("EUR");
                                double kUSD =jsonObject.getDouble("USD");
                                double kPLN =jsonObject.getDouble("PLN");
                                double tempAmount= amountList.get(i);
                                myMoneyPLN+=kPLN*tempAmount;
                                myMoneyUSD+=kUSD*tempAmount;
                                myMoneyEUR+=kEUR*tempAmount;

                            }
                            Toast.makeText(getApplicationContext(),"REFRESH!",Toast.LENGTH_SHORT).show();
                            setPrice();
                            timerSet();
                        } catch (JSONException e) {
                            ammount.setText("Error");
                            e.printStackTrace();
                        }catch (IndexOutOfBoundsException e){
                            Log.d("I wyjebało","tak");
                            Toast.makeText(MainActivity.this,R.string.noData,Toast.LENGTH_SHORT).show();
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
    }
    public void refresh(View view){
        checkPrice();

    }
    public void advancedAct(View view){
        Intent advancedAct=new Intent(this,AdvancedActivity.class);
        startActivity(advancedAct);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        //String url="https://min-api.cryptocompare.com/data/price?fsym=BTC&tsyms="+currency;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        try {
            shared();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        checkPrice();
    }
}
