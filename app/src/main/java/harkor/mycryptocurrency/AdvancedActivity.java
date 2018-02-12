package harkor.mycryptocurrency;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
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
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import static harkor.mycryptocurrency.R.id.b3;

public class AdvancedActivity extends AppCompatActivity {

    DecimalFormat decim = new DecimalFormat("##.##");
    DecimalFormat forBtc = new DecimalFormat("##############0.00000000");
    LinkedList<String> nameList = new LinkedList<>();
    LinkedList<Double> amountList = new LinkedList<>();
    LinkedList<String> dateList = new LinkedList<>();
    LinkedList<Double> ePriceList = new LinkedList<>();
    LinkedList<Double> uPriceList = new LinkedList<>();
    LinkedList<Double> pPriceList = new LinkedList<>();
    LinkedList<String> show=new LinkedList<>();
    String jsonString;
    ArrayAdapter<String> adapter;
    String currency;

    public String currencyTag(String cur){
        if(currency.equals("USD")){
            String tmp2="$"+cur;
            cur=tmp2;
        }else if(currency.equals("EUR")){
            cur=cur+"€";
        }else{
            cur=cur+"zł";
        }
        return cur;
    }
    public double getBuyPrice(int id){
        if(currency.equals("USD")){
            return uPriceList.get(id);
        }else if(currency.equals("EUR")){
            return ePriceList.get(id);
        }else {
            return pPriceList.get(id);
        }
    }


    public void timeShared(){
        SharedPreferences sharedPreferences;
        sharedPreferences=getSharedPreferences("harkor.mycryptocurrency", Context.MODE_PRIVATE);
        currency=sharedPreferences.getString("currency","USD");
    }
    public void sharedIn()throws JSONException{

        timeShared();
        SharedPreferences sharedPreferences;
        sharedPreferences=this.getSharedPreferences("harkor.myCrypto", Context.MODE_PRIVATE);
        jsonString=sharedPreferences.getString("jsonString","{\"cryptoList\":[]}");
        JSONObject jsonObject= new JSONObject(jsonString);
        JSONArray jsonArray=jsonObject.getJSONArray("cryptoList");
        for(int i=0;i<jsonArray.length();i++){
           JSONObject cryptoObj = jsonArray.getJSONObject(i);
           /////////////////////////////////////
           //idList.add(i);
           nameList.add(cryptoObj.optString("crypto"));
           amountList.add(cryptoObj.optDouble("amount"));
           dateList.add(cryptoObj.optString("date"));
           ePriceList.add(cryptoObj.optDouble("ePrice"));
           uPriceList.add(cryptoObj.optDouble("uPrice"));
           pPriceList.add(cryptoObj.optDouble("pPrice"));
            show.add(nameList.get(i)+": "+amountList.get(i));
           Log.d("wczytanie danych: ","TAK");
           //////////////////////////////////////
           }
    }
    public void checkPrice(String crp,final double amo,final int id) {
        String url = "https://min-api.cryptocompare.com/data/price?fsym=";//https://min-api.cryptocompare.com/data/price?fsym=ETH&tsyms=BTC,USD,EUR
        url = url + crp + "&tsyms="+currency+",BTC";
        final RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                JSONObject jsonObj;
                try {
                    jsonObj = new JSONObject(response);
                    TextView b4=(TextView)findViewById(R.id.b4);
                    double actPrice=jsonObj.getDouble(currency);
                    String tmp=""+actPrice;
                    tmp=currencyTag(tmp);
                    b4.setText(tmp);
                    TextView b5=(TextView)findViewById(R.id.b5);
                    double actValue=amo*actPrice;
                    b5.setText(currencyTag(decim.format(actValue)));
                    TextView b6=(TextView)findViewById(R.id.b6);
                    double result=actPrice-getBuyPrice(id);
                    b6.setText(currencyTag(decim.format(result)));
                    TextView b7=(TextView)findViewById(R.id.b7);
                    if(result>=0){
                        b6.setTextColor(Color.GREEN);
                        b7.setTextColor(Color.GREEN);
                    }else{
                        b6.setTextColor(Color.RED);
                        b7.setTextColor(Color.RED);
                    }
                    double resultPer=((actPrice/getBuyPrice(id))-1)*100;
                    String resPer=decim.format(resultPer)+"%";
                    b7.setText(resPer);
                    double btcPrice=jsonObj.getDouble("BTC");
                    TextView b8= (TextView)findViewById(R.id.b8);
                    TextView b9= (TextView)findViewById(R.id.b9);
                    b8.setText(forBtc.format(btcPrice)+"BTC");
                    b9.setText(forBtc.format(btcPrice*amo)+"BTC");
                } catch (JSONException e) {
                    Log.d("ERROR","JSON");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                queue.stop();
                Log.d("ERROR","CONNECTION");
                Toast.makeText(getApplicationContext(),R.string.checkConn,Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }
    public void onClick(View v){
        new AlertDialog.Builder(this)
                .setTitle(R.string.chooseCrypto)
                .setAdapter(adapter, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {



                        Button selectCr=(Button)findViewById(R.id.selectCrp);
                        selectCr.setText(nameList.get(which)+": "+amountList.get(which));
                        TextView b1=(TextView)findViewById(R.id.b1);
                        TextView b2=(TextView)findViewById(R.id.b2);
                        TextView b3=(TextView)findViewById(R.id.b3);

                        Log.d("Select:",show.get(which));
                        //ADD TIME
                        b1.setText(dateList.get(which));
                        //ADD PRICE
                        if(currency.equals("USD")){
                            b2.setText("$"+uPriceList.get(which));
                        }else if(currency.equals("EUR")){
                            b2.setText(ePriceList.get(which)+"€");
                        }else{
                            b2.setText(pPriceList.get(which)+"zł");
                        }
                        //ADD VALUE

                        if(currency.equals("USD")){
                            b3.setText("$"+((uPriceList.get(which))*amountList.get(which)));
                        }else if(currency.equals("EUR")){
                            b3.setText(((ePriceList.get(which))*amountList.get(which)+"€"));
                        }else{
                            b3.setText(((pPriceList.get(which))*amountList.get(which)+"zł"));
                        }

                        TextView b4=(TextView)findViewById(R.id.b4);
                        TextView b5=(TextView)findViewById(R.id.b5);
                        TextView b6=(TextView)findViewById(R.id.b6);
                        TextView b7=(TextView)findViewById(R.id.b7);
                        TextView b8=(TextView)findViewById(R.id.b8);
                        TextView b9=(TextView)findViewById(R.id.b9);
                        b4.setText("---");
                        b5.setText("---");
                        b6.setText("---");
                        b6.setTextColor(Color.BLACK);
                        b7.setTextColor(Color.BLACK);
                        b7.setText("---");
                        b8.setText("---");
                        b9.setText("---");


                        checkPrice(nameList.get(which),amountList.get(which),which);
                        dialog.dismiss();
                    }
                }).create().show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advanced);
        try {
            sharedIn();
            adapter = new ArrayAdapter<>(this,android.R.layout.simple_spinner_dropdown_item, nameList);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



}
