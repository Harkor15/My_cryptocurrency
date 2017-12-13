package harkor.mycryptocurrency;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class myCrypto extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_crypto);
        // LISTY
        List<String> cryptoCurrency=new LinkedList<>();
        List<Double> cryptoCurrency=new LinkedList<>();



        cryptoCurrency.add("Andrzej");
        cryptoCurrency.add("Kazek");
        cryptoCurrency.add("Stefan");





    }
}
