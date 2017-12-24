package harkor.mycryptocurrency;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class myCrypto extends AppCompatActivity {
    String jsonString;
    LinkedList<Integer>idList=new LinkedList<>();
    LinkedList<String>nameList=new LinkedList<>();
    LinkedList<Double>amountList=new LinkedList<>();
    LinkedList<String>dateList=new LinkedList<>();
    LinkedList<Double>ePriceList=new LinkedList<>();
    LinkedList<Double>uPriceList=new LinkedList<>();
    LinkedList<Double>pPriceList=new LinkedList<>();

   /* public void btn(View v){

        idList.remove(1);
        nameList.remove(1);
        amountList.remove(1);

       // private OnClickListener onItemDeleteClickListener = new OnClickListener() {
           // public void onClick(View v) {
                //int position = (Integer) v.getTag();
                //showToast(„Delete: ” + (position+1) + ” pozycja”);
        //Toast.makeText(getApplicationContext(),"SIZE: "+idList.size()+"Pozycja: "+position,Toast.LENGTH_SHORT).show();

    }*/

   public void shared() throws JSONException {  //GET DATA FROM SHARED PREFERENCES
       SharedPreferences sharedPreferences;
       sharedPreferences=getSharedPreferences("harkor.myCrypto", Context.MODE_PRIVATE);
       jsonString=sharedPreferences.getString("jsonString","{\n" +
               "       \"cryptoList\":[\n" +
               "       {\"crypto\":\"BTC\",\"amount\":0.20,\"date\":\"23-12-2017\",\"ePrice\":15000.0,\"uPrice\":16000.0,\"pPrice\":60000.0},\n" +
               "       {\"crypto\":\"BTC\",\"amount\":0.10,\"date\":\"23-12-2017\",\"ePrice\":15000.0,\"uPrice\":16000.0,\"pPrice\":60000.0},\n" +
               "       {\"crypto\":\"LSK\",\"amount\":5.00,\"date\":\"23-12-2017\",\"ePrice\":20.0,\"uPrice\":21.0,\"pPrice\":80.0}\n" +
               "       ]\n" +
               "}");
       /*
{
       "cryptoList":[
       {"crypto":"BTC","amount":0.2,"date":"23-12-2017","ePrice":15000.0,"uPrice":16000.0,"pPrice":60000.0},
       {"crypto":"BTC","amount":0.1,"date":"23-12-2017","ePrice":15000.0,"uPrice":16000.0,"pPrice":60000.0},
       {"crypto":"LSK","amount":5.0,"date":"23-12-2017","ePrice":20.0,"uPrice":21.0,"pPrice":80.0}
       ]
}
       */
       //Pobranie shared pref Stringu z JSON'em
       //String json_str = "{\"query\":\"Pizza\",\"locations\":[94043,90210]}";
       JSONObject jsonObject= new JSONObject(jsonString);
       JSONArray jsonArray=jsonObject.getJSONArray("cryptoList");
        for(int i=0;i<jsonArray.length();i++){
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



      // String poka=cur+": "+String.valueOf(amm);
       //Toast.makeText(getApplicationContext(),poka,Toast.LENGTH_SHORT).show();

   }

   public void sharedOff(){ //Export list to shared pref
       SharedPreferences sharedPreferences;
       sharedPreferences=getSharedPreferences("harkor.myCrypto", Context.MODE_PRIVATE);
       String savedString="{\"cryptoList\":[";

       for(int i=0; i<nameList.size();i++){
           savedString+="{\"crypto\":\""+nameList.get(i)+
                   "\",\"amount\":"+String.valueOf(amountList.get(i))+
                   ",\"date\":\""+dateList.get(i)+
                   "\",\"ePrice\":"+String.valueOf(ePriceList.get(i))+
                   ",\"uPrice\":"+String.valueOf(uPriceList.get(i))+
                   ",\"pPrice\":"+String.valueOf(pPriceList.get(i))+
                   "}";
            if(i!=nameList.size()-1){
                savedString+=",";
            }
       }
       savedString=savedString+"]}";
       SharedPreferences.Editor editor;
       editor=sharedPreferences.edit();
       editor.putString("jsonString",savedString);
       editor.commit();
       Log.d("JSON: ",jsonString);
   }

    public void buttonAct(View v){
        sharedOff();
        //System.exit(0);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_crypto);
        ListView resultListView= (ListView) findViewById(R.id.listCrypto);
        // LISTY
        /*
        List<String> cryptoCurrency=new LinkedList<>();
        List<Double> cryptoCurrency=new LinkedList<>();
        cryptoCurrency.add("Andrzej");
        cryptoCurrency.add("Kazek");
        cryptoCurrency.add("Stefan");
        */

/*
        HashMap<String, String> nameAddresses=new HashMap<>();
        nameAddresses.put("BTC: ","0.001748");
        nameAddresses.put("LSK: ","5.001748");
        nameAddresses.put("ADA: ","58.001748");
        nameAddresses.put("AsA: ","58.001748");
        nameAddresses.put("AdA: ","58.001748");
        nameAddresses.put("AfA: ","58.001748");
        nameAddresses.put("AgA: ","58.001748");
        nameAddresses.put("AhA: ","58.001748");
        nameAddresses.put("AjA: ","58.001748");
        nameAddresses.put("AkA: ","58.001748");
        nameAddresses.put("AlA: ","58.001748");
        nameAddresses.put("AtA: ","58.001748");
        nameAddresses.put("ArA: ","58.001748");
        nameAddresses.put("AeA: ","58.001748");
        nameAddresses.put("AwA: ","58.001748");

        List<HashMap<String,String>> listItems=new ArrayList<>();
        SimpleAdapter adapter = new SimpleAdapter(this,listItems, R.layout.my_crypto_element,
                new String[]{"First Line", "Second Line"},
                new int []{R.id.currencyTAG, R.id.currencyAmount});
        Iterator it =nameAddresses.entrySet().iterator();
        while (it.hasNext()){
            HashMap<String,String>resultMap=new HashMap<>();
            Map.Entry pair = (Map.Entry)it.next();
            resultMap.put("First Line", pair.getKey().toString());
            resultMap.put("Second Line", pair.getValue().toString());
            listItems.add(resultMap);
        }


*/

        try {
            shared();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //////////////////////////////////////
        /*
        idList.add(0);
        nameList.add("BTC");
        amountList.add(0.01748);
        dateList.add("17-12-2017");
        ePriceList.add(1000.0);
        uPriceList.add(1000.0);
        pPriceList.add(1000.0);

        idList.add(1);
        nameList.add("LSK");
        amountList.add(5.0001568);
        dateList.add("17-12-2017");
        ePriceList.add(10.0);
        uPriceList.add(10.0);
        pPriceList.add(10.0);
        */
        ///////////////////////////////////////


        List<HashMap<String,String>> listItems=new ArrayList<>();
        SimpleAdapter adapter = new SimpleAdapter(this,listItems, R.layout.my_crypto_element,
                new String[]{"Crypto", "Amount","Delete"},
                new int []{R.id.currencyTAG, R.id.currencyAmount,R.id.buttonDelete});


        for(int i=0;i<idList.size();i++){
            HashMap<String,String>resultMap=new HashMap<>();
            resultMap.put("Crypto", nameList.get(i));
            resultMap.put("Amount",String.valueOf(amountList.get(i)));
            resultMap.put("Delete",String.valueOf(idList.get(i)));
            listItems.add(resultMap);
        }
        resultListView.setAdapter(adapter);



        //Iterator it =nameAddresses.entrySet().iterator();
        //while (it.hasNext()){
           // Map.Entry pair = (Map.Entry)it.next();
           // resultMap.put("Crypto","CVC");
            //resultMap.put("Amount","0.55");
            //resultMap.put("Delete","del");
       // HashMap<String,String>resultMap=new HashMap<>();
        //resultMap.put("Crypto","CVC");
        //resultMap.put("Amount","0.55");
        //listItems.add(resultMap);
        //}
    }
}
