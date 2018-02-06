package harkor.mycryptocurrency;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

/**
 * Created by Harkor on 2018-01-26.
 */

public class MyCustomAdapter  extends BaseAdapter implements ListAdapter{

    private Context context;
    String jsonString;
    private LinkedList<String>dateList=new LinkedList<>();
    private LinkedList<Double>ePriceList=new LinkedList<>();
    private LinkedList<Double>uPriceList=new LinkedList<>();
    private LinkedList<Double>pPriceList=new LinkedList<>();
    private LinkedList<Integer> idList =new LinkedList<>();
    private LinkedList<String> nameList =new LinkedList<>();
    private LinkedList<Double> amountList =new LinkedList<>();


    public void loadData() throws JSONException {

        SharedPreferences sharedPreferences;
        sharedPreferences=context.getSharedPreferences("harkor.myCrypto",Context.MODE_PRIVATE);
        jsonString=sharedPreferences.getString("jsonString","{\"cryptoList\":[]}");
        JSONObject jsonObject= new JSONObject(jsonString);
        JSONArray jsonArray=jsonObject.getJSONArray("cryptoList");
        for(int i=0;i<jsonArray.length();i++){
            JSONObject cryptoObj = jsonArray.getJSONObject(i);
            /////////////////////////////////////
           // idList.add(i);
            nameList.add(cryptoObj.optString("crypto"));
            amountList.add(cryptoObj.optDouble("amount"));
            dateList.add(cryptoObj.optString("date"));
            ePriceList.add(cryptoObj.optDouble("ePrice"));
            uPriceList.add(cryptoObj.optDouble("uPrice"));
            pPriceList.add(cryptoObj.optDouble("pPrice"));
            Log.d("wczytanie danych: ","TAK");
            //////////////////////////////////////
        }
    }

    public void exportData(){
        SharedPreferences sharedPreferences;
        sharedPreferences=context.getSharedPreferences("harkor.myCrypto", Context.MODE_PRIVATE);
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


        System.exit(0);

    }

    //public MyCustomAdapter(LinkedList<Integer>idList,LinkedList<String>nameList,LinkedList<Double>amountList,Context context){
    public MyCustomAdapter(LinkedList<Integer>idList,Context context) throws JSONException {
                this.idList=idList;

        //this.nameList=nameList;
        //this.amountList=amountList;
        this.context=context;
        loadData();
        Log.d("Inicjacja: ","TAK");


    }

    @Override
    public int getCount() {
        return nameList.size();
    }

    @Override
    public String getItem(int position) {
        return nameList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, final View convertView, ViewGroup parent) {
        View view=convertView;
        if(view==null){
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.my_crypto_element, null);
        }

        TextView tag=(TextView) view.findViewById(R.id.currencyTAG);
        TextView amount=(TextView) view.findViewById(R.id.currencyAmount);
        final Button delButton=(Button) view.findViewById(R.id.buttonDelete);
        tag.setText(nameList.get(position));
        amount.setText(String.valueOf(amountList.get(position)));

        //Wczytywanie zasobów tu bedzie!!!



        delButton.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){

                AlertDialog.Builder builder=new AlertDialog.Builder(context);
                builder.setTitle(R.string.sureDel);
                builder.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //String txt=position+"X";
                        //delButton.setText(txt);
                        idList.remove(position);
                        nameList.remove(position);
                        amountList.remove(position);
                        dateList.remove(position);
                        ePriceList.remove(position);
                        uPriceList.remove(position);
                        pPriceList.remove(position);
                        exportData();


                    }
                });
                builder.setNegativeButton(R.string.no,null);
                builder.create();
                builder.show();





            }
        });


        return view;
    }



}
