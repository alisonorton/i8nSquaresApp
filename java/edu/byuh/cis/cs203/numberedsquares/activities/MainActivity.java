package edu.byuh.cis.cs203.numberedsquares.activities;

import android.app.Activity;
import android.os.Bundle;

import edu.byuh.cis.cs203.numberedsquares.ui.SquareView;

/**
 * This is the MainActivity of our class.
 * This is where we create the Activity and allow it to communicate
 * with our SquareView class.
 */

public class MainActivity extends Activity {

    private SquareView sv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sv = new SquareView(this);
        setContentView(sv);
    }

    @Override
    public void onPause(){
        super.onPause();
        sv.appIsBackgrounded();
    }

    @Override
    public void onResume(){
        super.onResume();
        sv.appIsForegrounded();
    }
}