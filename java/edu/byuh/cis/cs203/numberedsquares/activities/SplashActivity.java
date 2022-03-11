package edu.byuh.cis.cs203.numberedsquares.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.ImageView;

import edu.byuh.cis.cs203.numberedsquares.R;

public class SplashActivity extends Activity {

    private ImageView iv;

    @Override
    public void onCreate(Bundle b) {
        super.onCreate(b);
        iv = new ImageView(this);
        iv.setImageResource(R.drawable.jollypop_splashscreen);
        iv.setScaleType(ImageView.ScaleType.FIT_XY);
        setContentView(iv);
    }

    @Override
    public boolean onTouchEvent(MotionEvent m) {
        if (m.getAction() == MotionEvent.ACTION_DOWN) {
            float x = m.getX();
            float y = m.getY();

            float w = iv.getWidth();
            float h = iv.getHeight();

            if (x > 0.75 * w && y < 0.2 * h) {
                Intent i = new Intent(this, Prefs.class);
                startActivity(i);
            } else {
                Intent i = new Intent(this, MainActivity.class);
                startActivity(i);
                finish();
            }
        }

        return true;
    }


}
