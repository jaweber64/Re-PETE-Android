package net.webersoftwaresolutions.RePete;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import static net.webersoftwaresolutions.RePete.R.id.PETE;
import static net.webersoftwaresolutions.RePete.R.id.Re;
import static net.webersoftwaresolutions.RePete.R.id.radioButton;
import static net.webersoftwaresolutions.RePete.R.id.radioButton2;
import static net.webersoftwaresolutions.RePete.R.id.radioButton3;
import static net.webersoftwaresolutions.RePete.R.id.radioButton4;
import static net.webersoftwaresolutions.RePete.R.id.radioButton5;
import static net.webersoftwaresolutions.RePete.R.id.radioButton6;
import static net.webersoftwaresolutions.RePete.R.id.radioButton7;
import static net.webersoftwaresolutions.RePete.R.id.radioButton8;
import static net.webersoftwaresolutions.RePete.R.layout.settings;

public class Settings extends AppCompatActivity {

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_settings:
                    //mTextMessage.setText(R.string.title_settings);
                    return true;
                case R.id.navigation_play:
                    //mTextMessage.setText(R.string.title_play);
                    startActivity(new Intent(Settings.this, MainActivity.class));
                    return true;
                case R.id.navigation_instructions:
                    startActivity(new Intent(Settings.this, Instructions.class));
                    //mTextMessage.setText(R.string.title_instructions);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(settings);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);

        //navigation.setItemTextColor(R.color.colorPrimary);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //public Typeface treb_ms = Typeface.createFromAsset(this.getAssets(),"fonts/Trebuchet_MS.ttf");
        final Typeface treb_ms_b = Typeface.createFromAsset(this.getAssets(),"fonts/Trebuchet_MS_Bold.ttf");
        final Typeface treb_ms_bi = Typeface.createFromAsset(this.getAssets(),"fonts/Trebuchet_MS_Bold_Italic.ttf");
        final Typeface marker_felt = Typeface.createFromAsset(this.getAssets(),"fonts/markerfelt.ttf");

        final TextView instr_Re = (TextView) findViewById(Re);
        final TextView instr_PETE = (TextView) findViewById(PETE);
        final TextView instrLabel = (TextView) findViewById(R.id.instrLabel);
        instr_Re.setTypeface(treb_ms_b);
        instr_PETE.setTypeface(treb_ms_bi);
        instrLabel.setTypeface(treb_ms_b);

        ((TextView) findViewById(R.id.GameModeSubTxt)).setTypeface(marker_felt);
        ((TextView) findViewById(R.id.gameModeLabel)).setTypeface(marker_felt);
        ((TextView) findViewById(R.id.soundMenuLabel)).setTypeface(marker_felt);

        final RadioButton rb1 = (RadioButton)findViewById(radioButton);
        rb1.setTypeface(treb_ms_b);
        final RadioButton rb2 = (RadioButton)findViewById(radioButton2);
        rb2.setTypeface(treb_ms_b);
        final RadioButton rb3 = (RadioButton)findViewById(radioButton3);
        rb3.setTypeface(treb_ms_b);
        final RadioButton rb4 = (RadioButton)findViewById(radioButton4);
        rb4.setTypeface(treb_ms_b);
        final RadioButton rb5 = (RadioButton)findViewById(radioButton5);
        rb5.setTypeface(treb_ms_b);
        final RadioButton rb6 = (RadioButton)findViewById(radioButton6);
        rb6.setTypeface(treb_ms_b);
        final RadioButton rb7 = (RadioButton)findViewById(radioButton7);
        rb7.setTypeface(treb_ms_b);
        final RadioButton rb8 = (RadioButton)findViewById(radioButton8);
        rb8.setTypeface(treb_ms_b);

        final RadioGroup soundselected = (RadioGroup) findViewById(R.id.sound_group);
        soundselected.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener()  {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, @IdRes int i) {
                i = soundselected.getCheckedRadioButtonId();
                RadioButton sound_chosen = (RadioButton) findViewById(i);

                if (sound_chosen == rb6) {
                    SimonGame.setSoundFile(6);
                }
                if (sound_chosen == rb7) {
                    SimonGame.setSoundFile(7);
                }
                if (sound_chosen == rb8) {
                    SimonGame.setSoundFile(8);
                }
            }
        });

    }
}
