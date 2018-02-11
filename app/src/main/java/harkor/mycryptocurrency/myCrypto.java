package harkor.mycryptocurrency;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;


public class myCrypto extends AppCompatActivity {
    //String jsonString;
    //Context context;
    LinkedList<Integer> idList = new LinkedList<>();
    LinkedList<String> nameList = new LinkedList<>();
    LinkedList<Double> amountList = new LinkedList<>();
    LinkedList<String> dateList = new LinkedList<>();
    LinkedList<Double> ePriceList = new LinkedList<>();
    LinkedList<Double> uPriceList = new LinkedList<>();
    LinkedList<Double> pPriceList = new LinkedList<>();

    public void addNewCrypto(final String tag,final double amount) {
        //wyslij zapytanie do serwera
        String url = "https://min-api.cryptocompare.com/data/pricemulti?fsyms=" + tag + "&tsyms=EUR,USD,PLN";
        Log.d("url",url);
        final RequestQueue queue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject jsonObject;
                        Log.d("Mamy objekt!","TRUE!");

                        try {
                            jsonObject = new JSONObject(response);
                            Log.d("WCZYTANO NOWE KRYPTO: ",""+jsonObject.length()); //7 to błąd 1-to dobrze
                            Log.d("WCZYTANO NOWE KRYPTO: ",response);
                            if(jsonObject.length()==1){
                                //Jest ok dodajemy
                                JSONObject jsonObj=jsonObject.getJSONObject(tag);
                                double eur =jsonObj.getDouble("EUR");
                                double usd =jsonObj.getDouble("USD");
                                double pln =jsonObj.getDouble("PLN");
                                addNewFinally(tag,amount,eur,usd,pln);
                            }else{
                                //Jest źle
                                Toast.makeText(myCrypto.this,R.string.checkTag,Toast.LENGTH_SHORT).show();
                            }
                            //Biore się za rozkodowanie JSONA i wpisanie go do shared ale to jutro bo dzisiaj już do spania... ^_^


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Log.d("WCZYTANO NOWE KRYPTO: ","NIE!");
                            Toast.makeText(myCrypto.this,R.string.errorJSON,Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                queue.stop();
                Log.d("WCZYTANO NOWE KRYPTO: ","ERROR!");
                /// brak internetu
                Toast.makeText(myCrypto.this,R.string.checkConn,Toast.LENGTH_SHORT).show();

            }
        });
        Log.d("do boju"," wymaszerować");
        queue.add(stringRequest);
        //queue.stop();

        //odśwież stronkę... ;) i jak ogarniesz jak odświerzać to dodaj też do usuwania.. ;)
    }
    public void addNewFinally(String tag,Double amount,Double eP,Double uP,Double pP){
        //Toast.makeText(myCrypto.this,tag+": "+amount+" "+eP+" €, "+uP+"$, "+pP+" zł",Toast.LENGTH_SHORT).show();

        SharedPreferences sharedPref;
        sharedPref=getSharedPreferences("harkor.myCrypto",Context.MODE_PRIVATE);
        String myShared=sharedPref.getString("jsonString","{\"cryptoList\":[]}");//   {"cryptoList":[]}  //To pusty string
        String newCrypto="";
        if(!myShared.equals("{\"cryptoList\":[]}")){
            newCrypto=",";
        }
        Log.d("SHARE",myShared);
        //Log.d("SHARE.length()",""+myShared.length());
        myShared=myShared.substring(0,myShared.length()-2); //]}   wywalam potem dodam.
        Log.d("SHARE",myShared);
        Date now = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm dd-MM-yyyy");
        String formattedTime = sdf.format(now);
        //,{"crypto":"SAFEX","amount":164.67,"date":"23-12-2017","ePrice":15000.0,"uPrice":16000.0,"pPrice":60000.0} WZÓR

        newCrypto+="{\"crypto\":\""+tag+"\",\"amount\":"+amount+",\"date\":\""+formattedTime+"\",\"ePrice\":"+eP+",\"uPrice\":"+uP+",\"pPrice\":"+pP+"}]}";
        myShared=myShared+newCrypto;

        SharedPreferences.Editor editor;
        editor=sharedPref.edit();
        editor.putString("jsonString",myShared);
        editor.commit();
        System.exit(0);//EXITTT!@!!!@!@!@!@!@!@!@!@!@!@



    }
    public void shared() throws JSONException {  //GET DATA FROM SHARED PREFERENCES
       /*
       SharedPreferences sharedPreferences;
       sharedPreferences=getSharedPreferences("harkor.myCrypto", Context.MODE_PRIVATE);
       jsonString=sharedPreferences.getString("jsonString","{\n" +
               "       \"cryptoList\":[\n" +
               "       {\"crypto\":\"BTC\",\"amount\":0.01448812,\"date\":\"23-12-2017\",\"ePrice\":15000.0,\"uPrice\":16000.0,\"pPrice\":60000.0},\n" +
               "       {\"crypto\":\"SAFEX\",\"amount\":164.67,\"date\":\"23-12-2017\",\"ePrice\":15000.0,\"uPrice\":16000.0,\"pPrice\":60000.0},\n" +
               "       {\"crypto\":\"IOTA\",\"amount\":0.57,\"date\":\"23-12-2017\",\"ePrice\":15000.0,\"uPrice\":16000.0,\"pPrice\":60000.0},\n" +
               "       {\"crypto\":\"ADA\",\"amount\":58.76,\"date\":\"23-12-2017\",\"ePrice\":15000.0,\"uPrice\":16000.0,\"pPrice\":60000.0},\n" +
               "       {\"crypto\":\"XVG\",\"amount\":110.3,\"date\":\"23-12-2017\",\"ePrice\":15000.0,\"uPrice\":16000.0,\"pPrice\":60000.0},\n" +
               "       {\"crypto\":\"BCH\",\"amount\":0.00409767,\"date\":\"23-12-2017\",\"ePrice\":15000.0,\"uPrice\":16000.0,\"pPrice\":60000.0},\n" +
               "       {\"crypto\":\"LSK\",\"amount\":5.0,\"date\":\"23-12-2017\",\"ePrice\":20.0,\"uPrice\":21.0,\"pPrice\":80.0}\n" +
               "       ]\n" +
               "}");
       /*
{
       "cryptoList":[
       {"crypto":"BTC","amount":0.01448812,"date":"23-12-2017","ePrice":15000.0,"uPrice":16000.0,"pPrice":60000.0},
       {"crypto":"SAFEX","amount":164.67,"date":"23-12-2017","ePrice":15000.0,"uPrice":16000.0,"pPrice":60000.0},
       {"crypto":"IOTA","amount":0.57,"date":"23-12-2017","ePrice":15000.0,"uPrice":16000.0,"pPrice":60000.0},
       {"crypto":"ADA","amount":58.76,"date":"23-12-2017","ePrice":15000.0,"uPrice":16000.0,"pPrice":60000.0},
       {"crypto":"XVG","amount":110.3,"date":"23-12-2017","ePrice":15000.0,"uPrice":16000.0,"pPrice":60000.0},
       {"crypto":"BCC","amount":0.00409767,"date":"23-12-2017","ePrice":15000.0,"uPrice":16000.0,"pPrice":60000.0},
       {"crypto":"LSK","amount":5.0,"date":"23-12-2017","ePrice":20.0,"uPrice":21.0,"pPrice":80.0}
       ]
}
       /*
       //Pobranie shared pref Stringu z JSON'em
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
        */
       //TUTAJ BĘDIZE NOWE SHARED Z LICZBĄ!
       SharedPreferences sharedPreferences;
       sharedPreferences=getSharedPreferences("harkor.myCrypto", Context.MODE_PRIVATE);
       String jsonString=sharedPreferences.getString("jsonString","{\"cryptoList\":[]}");
       if(jsonString=="{\"cryptoList\":[]}"){
           Log.d("SHARED","EMPTY");
           Toast.makeText(myCrypto.this,"Nie ma krypto!",Toast.LENGTH_SHORT).show();
       }else {
           JSONObject jsonObject = new JSONObject(jsonString);
           JSONArray jsonArray = jsonObject.getJSONArray("cryptoList");
           for (int i = 0; i < jsonArray.length(); i++) {
               //JSONObject cryptoObj = jsonArray.getJSONObject(i);
               idList.add(i);
           }
           Log.d("SHARED: ","OK");

       }




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
       //Log.d("JSON: ",jsonString);
   }
    public void buttonAct(View v){
        System.exit(0);
    }



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_crypto);
        Button addGen=(Button)findViewById(R.id.addGeneral);
        addGen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //final Context con=getApplicationContext();
                AlertDialog.Builder builder = new AlertDialog.Builder(myCrypto.this);
                View dialView=getLayoutInflater().inflate(R.layout.add_dialog,null);
                final EditText addName=(EditText) dialView.findViewById(R.id.etName);
                final EditText addAmount=(EditText) dialView.findViewById(R.id.etAmount);
                //addName.setHint("BTC");
                //addName.setInputType(InputType.TYPE_CLASS_TEXT);
                //addAmount.setInputType(InputType.TYPE_NUMBER_FLAG_DECIMAL);
                //addAmount.setHint(R.string.amount);
                builder.setPositiveButton(R.string.addNew, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Editable crypTag=addName.getText();
                        Editable crypAmo=addAmount.getText();
                        String crypStr=crypTag.toString();
                        crypStr=crypStr.toUpperCase();
                        // Tu robie... Trzeba kontrole błędów po wprowadzeniu tekstu zamiast ilości krypto... Pozdro ide spać...
                        try {
                            double crypDou=Double.parseDouble(crypAmo.toString());
                            addNewCrypto(crypStr,crypDou);
                        }catch(NumberFormatException e){
                            Toast.makeText(myCrypto.this,R.string.errorDouble,Toast.LENGTH_SHORT).show();
                        }







                        //Toast.makeText(myCrypto.this,crypStr+": "+crypDou,Toast.LENGTH_SHORT).show();
                    }
                });
                builder.setNegativeButton(R.string.cancel,null);
                builder.setView(dialView);
                AlertDialog dialog = builder.create();
                dialog.show();



            }
        });






        try {
            shared();
        } catch (JSONException e) {
            e.printStackTrace();
        }


        //MyCustomAdapter adapter  = new MyCustomAdapter(idList,nameList,amountList,this);
        MyCustomAdapter adapter  = null;
        try {
            adapter = new MyCustomAdapter(idList,this);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ListView lView = (ListView)findViewById(R.id.listCrypto);
        lView.setAdapter(adapter);
        //List<HashMap<String,String>> listItems=new ArrayList<>();
        //SimpleAdapter adapter = new SimpleAdapter(this,listItems, R.layout.my_crypto_element,
         //       new String[]{"Crypto", "Amount","Delete"},

    }
}
