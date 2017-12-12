package net.webersoftwaresolutions.RePete;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;

//import com.testfairy.TestFairy;

public class MainActivity extends AppCompatActivity {

//    Context mainActivityContext = this;
    MediaPlayer mp;
    SimonGame myGame;
//    private TextView mTextMessage;

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_settings:
                    //mTextMessage.setText(R.string.title_settings);
                    startActivity(new Intent(MainActivity.this, Settings.class));
                    return true;
                case R.id.navigation_play:
//                    mTextMessage.setText(R.string.title_play);
                    startActivity(new Intent(MainActivity.this, MainActivity.class));
                    return true;
                case R.id.navigation_instructions:
                    startActivity(new Intent(MainActivity.this, Instructions.class));
                    //mTextMessage.setText(R.string.title_instructions);
                    return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
//        TestFairy.begin(this,"a39d0c9eed3693153bc7c4f5a0cadf5926d4d6a0");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        //navigation.setItemTextColor(R.color.colorPrimary);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        myGame = new SimonGame(MainActivity.this, this);
        myGame.init();
        myGame.setupButtons();

    }

    protected void onDestroy() {
        super.onDestroy();
        cleanUp();
    }

    public void cleanUp() {
        if (mp != null) {
            if (mp.isPlaying()) {
                mp.stop();
                mp.release();
            }
        }
        mp = null;
    }

}