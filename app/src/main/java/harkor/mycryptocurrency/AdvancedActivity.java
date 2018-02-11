package harkor.mycryptocurrency;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class AdvancedActivity extends AppCompatActivity {


    //LinkedList<Integer> idList = new LinkedList<>();
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

    public void timeShared(){
        SharedPreferences sharedPreferences;
        sharedPreferences=getSharedPreferences("harkor.mycryptocurrency", Context.MODE_PRIVATE);
        currency=sharedPreferences.getString("currency","USD");
    }

    //private Context context=getApplicationContext();


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
                        TextView b4=(TextView)findViewById(R.id.b4);
                        TextView b5=(TextView)findViewById(R.id.b5);
                        TextView b6=(TextView)findViewById(R.id.b6);
                        TextView b7=(TextView)findViewById(R.id.b7);
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
                        //TODO: OTHER TV's

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
            adapter = new ArrayAdapter<>(this,
                    android.R.layout.simple_spinner_dropdown_item, nameList);


        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



}
