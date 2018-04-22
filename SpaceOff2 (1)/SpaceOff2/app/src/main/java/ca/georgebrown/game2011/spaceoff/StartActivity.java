package ca.georgebrown.game2011.spaceoff;

import android.app.Activity;
import android.support.v7.app.AppCompatActivity;
import android.graphics.Point;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;

public class StartActivity extends Activity {

    SpaceView spaceView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Display display = getWindowManager().getDefaultDisplay();

        Point size = new Point();

        display.getSize(size);

        spaceView = new SpaceView(this, size.x, size.y);

        setContentView(spaceView);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }
    @Override
    protected void onResume() {
        super.onResume();
        spaceView.resume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        spaceView.pause();
    }
}
