package ca.georgebrown.game2011.spaceoff;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;
import android.widget.Button;


public class MainActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);

        //Setting up Start Button to be clickable
        Button play = (Button) findViewById(R.id.PlayBtn);
        PlayButtonListener playListener = new PlayButtonListener();
        play.setOnClickListener(playListener);

        //Setting up Quit Button to be clickable
        Button quit = (Button) findViewById(R.id.QuitBtn);
        QuitButtonListener quitListener = new QuitButtonListener();
        quit.setOnClickListener(quitListener);
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume(){
        super.onResume();
    }

    @Override
    protected void onPause(){
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    @Override
    protected void onStop(){
        super.onStop();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
    }

    //Play Button calls StartPlay() when clicked
    private class PlayButtonListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            StartPlay();
        }
    }

    //Function to start PlayActivity
    private void StartPlay() {
        Intent intent = new Intent(this, StartActivity.class);
        startActivity(intent);
    }

    //Quit Button calls this when clicked
    private class QuitButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            finish();
        }
    }
}
