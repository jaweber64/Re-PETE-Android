package net.webersoftwaresolutions.RePete;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.webkit.WebView;
import android.widget.TextView;

import static net.webersoftwaresolutions.RePete.R.id.PETE;
import static net.webersoftwaresolutions.RePete.R.id.Re;


/**
 * Created by jaWeber on 10/29/17.
 */

public class Instructions extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_settings:
                    //mTextMessage.setText(R.string.title_settings);
                    startActivity(new Intent(Instructions.this, Settings.class));
                    return true;
                case R.id.navigation_play:
                    //mTextMessage.setText(R.string.title_play);
                    startActivity(new Intent(Instructions.this, MainActivity.class));
                    return true;
                case R.id.navigation_instructions:
                    //startActivity(new Intent(MainActivity.this, Instructions.class));
                    //mTextMessage.setText(R.string.title_instructions);
                    return true;
            }
            return false;
        }

    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.instructions);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);

        //navigation.setItemTextColor(R.color.colorPrimary);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //public Typeface treb_ms = Typeface.createFromAsset(this.getAssets(),"fonts/Trebuchet_MS.ttf");
        final Typeface treb_ms_b = Typeface.createFromAsset(this.getAssets(),"fonts/Trebuchet_MS_Bold.ttf");
        final Typeface treb_ms_bi = Typeface.createFromAsset(this.getAssets(),"fonts/Trebuchet_MS_Bold_Italic.ttf");

        final TextView instr_Re = (TextView) findViewById(Re);
        final TextView instr_PETE = (TextView) findViewById(PETE);
        final TextView instrLabel = (TextView) findViewById(R.id.instrLabel);
        instr_Re.setTypeface(treb_ms_b);
        instr_PETE.setTypeface(treb_ms_bi);
        instrLabel.setTypeface(treb_ms_b);

        final WebView instr_wv = (WebView) findViewById(R.id.instr_wv);
        instr_wv.loadUrl("file:///android_asset/MySimonInstructions.html");
    }
}
