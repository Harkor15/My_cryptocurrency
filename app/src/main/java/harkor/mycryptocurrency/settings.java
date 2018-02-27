package harkor.mycryptocurrency;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

public class settings extends AppCompatActivity {

    public void back(View view){
        RadioGroup cPicker= (RadioGroup) findViewById(R.id.cPicker);
        RadioButton checkedButton=(RadioButton) findViewById(cPicker.getCheckedRadioButtonId());
        String curr= (String)checkedButton.getText();
        SharedPreferences sharedPreferences;
        sharedPreferences=getSharedPreferences("harkor.mycryptocurrency", Context.MODE_PRIVATE);
        //String currency=sharedPreferences.getString("currency","USD");
        SharedPreferences.Editor editor;
        editor=sharedPreferences.edit();
        editor.putString("currency",curr);
        editor.commit();
        System.exit(0);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        RadioGroup cPicker= (RadioGroup) findViewById(R.id.cPicker);
        SharedPreferences sharedPreferences;
        sharedPreferences=getSharedPreferences("harkor.mycryptocurrency", Context.MODE_PRIVATE);
        String currency=sharedPreferences.getString("currency","USD");
        if(currency.equals("USD")){
            cPicker.check(R.id.USD);
        }else if(currency.equals("EUR")){
            cPicker.check(R.id.EUR);
        }else{
            cPicker.check(R.id.PLN);
        }




    }
}
