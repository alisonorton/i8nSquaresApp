package edu.byuh.cis.cs203.numberedsquares.ui;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import edu.byuh.cis.cs203.numberedsquares.enums.GameStyle;
import edu.byuh.cis.cs203.numberedsquares.activities.Prefs;
import edu.byuh.cis.cs203.numberedsquares.R;
import edu.byuh.cis.cs203.numberedsquares.util.Timer;
import edu.byuh.cis.cs203.numberedsquares.util.TimerListener;
import edu.byuh.cis.cs203.numberedsquares.enums.TouchStatus;
import edu.byuh.cis.cs203.numberedsquares.gamestyle.CountingGame;
import edu.byuh.cis.cs203.numberedsquares.gamestyle.JapaneseSpelling;
import edu.byuh.cis.cs203.numberedsquares.gamestyle.SpellingGame;

/**
 * This is the SquareView class that will run in our Activity
 */

public class SquareView extends AppCompatImageView implements TimerListener {

    private ArrayList<NumberedSquares> numSquares;

    private float w, h;
    private boolean initialized;
    private Timer timer;
    private Toast t;
    private MediaPlayer soundtrack;
    private MediaPlayer soundEffects;
    private GameStyle gs;
    private TouchStatus ts;
    private int score;
    private int highScore;
    private int totalScore;
    private Paint scoreP;


    /**
     * This is the constructor for the SquareView class.
     * It sets and instantiates our objects and sets the color and style for the squares.
     *
     * @param c our context from the MainActivity
     */
    public SquareView(Context c) {

        super(c);

        initialized = true;
        numSquares = new ArrayList<>();
        int gameType = Prefs.getGameType(c);
        switch (gameType) {
            case 1:
                gs = new CountingGame(getResources());
                break;
            case 2:
                gs = new SpellingGame(getResources());
                break;
            case 3:
                gs = new JapaneseSpelling(getResources());
        }


        try (Scanner s = new Scanner(c.openFileInput(gs.toString()))) {
            highScore = s.nextInt();
        } catch (IOException e) {
            highScore = 0;
        }

        if (Prefs.soundtrackOn(c)) {
            soundtrack = MediaPlayer.create(c, R.raw.jinglebells);
            soundtrack.start();
            soundtrack.setLooping(true);
        }

        if (Prefs.soundEffectsOn(c)) {
            soundEffects = MediaPlayer.create(c, R.raw.explosion_sound_effect);
        }
    }

    /**
     * This is the onDraw method.
     * It randomizes the location of where our number squares are drawn.
     * It also sets the size so they are in proportion to the screen of the device.
     *
     * @param c the canvas so we have something to draw on
     */

    @Override
    public void onDraw(Canvas c) {
        super.onDraw(c);

        if (initialized) {
            score = 0;
            highScore = 0;
            w = c.getWidth();
            h = c.getHeight();
            scoreP = new Paint();
            scoreP.setTextSize(w * 0.05f);
            scoreP.setColor(Color.WHITE);
            scoreP.setTextAlign(Paint.Align.RIGHT);
            timer = new Timer().getInstance();
            generateSquares();
            timer.register(this);
            initialized = false;
        }

        setImageResource(Prefs.getBackgroundImage(getContext()));
        setScaleType(ScaleType.FIT_XY);

        for (NumberedSquares ns : numSquares) {
            ns.draw(c);
        }

        String scoreMsg = getResources().getString(R.string.score);
        //CHANGE score
        c.drawText(scoreMsg + score, w - 20, 70, scoreP);

    }

    /**
     * This method is used to receive and respond to a user pushing down on the screen.
     * When a user taps the screen it will recall the generateSquares() and then redraw the
     * new squares.
     *
     * @param m our MotionEvent for user interaction with the screen.
     * @return a requirement for the onTouchEvent method
     */

    @Override
    public boolean onTouchEvent(MotionEvent m) {
        if (m.getAction() == MotionEvent.ACTION_DOWN) {
            float x = m.getX();
            float y = m.getY();

            tappedSquares(x, y);

        }

        invalidate();
        return true;
    }

    /**
     * This method will loop through potential NumberedSquares objects and compare them to one
     * another to see if they overlap.  Only object that don't overlap will be added to
     * our NumberedSquares array list.
     */

    public void generateSquares() {
        numSquares.clear();
        NumberedSquares.resetCounter();
        List<String> labels = gs.getSquareLabels();
        int n = labels.size();
        int i = 0;

        while (numSquares.size() < n) {
            float size = w * 0.2f;
            float x = (float) (Math.random() * (w - size));
            float y = (float) (Math.random() * (h - size));

            RectF ns = new RectF(x, y, x + size, y + size);
            boolean bueno = true;

            for (NumberedSquares test : numSquares) {
                if (test.overlaps(ns)) {
                    bueno = false;
                    break;
                }
            }
            if (bueno) {
                numSquares.add(new NumberedSquares(this, labels.get(i), i, ns, getResources()));
                i++;
            }

        }

        for (NumberedSquares ns : numSquares) {
            timer.register(ns);
        }
    }

    /**
     * Looping and comparing each square on the screen to see if they're colliding.
     */

    public void checkForCollisions() {
        for (NumberedSquares a : numSquares) {
            for (NumberedSquares b : numSquares) {
                if (a.checkSerial(b)) {
                    if (a.overlaps(b.getBounds())) {
                        a.getCollisionSide(b);
                    }
                }
            }
            a.move();
        }
    }

    /**
     * This method does the logic for if a square has been tapped and if the user has progressed to
     * the next level.
     *
     * @param x the x coordinate of where the user touched the screen
     * @param y the y coordinate of where the user touched the screen
     */

    public void tappedSquares(float x, float y) {
        for (NumberedSquares ns : numSquares) {
            if (ns.contains(x, y) && ns.getExploded() != true) {
                ts = gs.getTouchStatus(ns);
                if (ts == TouchStatus.CONTINUE) {
                    ns.tapped();
                    playSoundEffect();
                    increaseScore();
                } else if (ts == TouchStatus.LEVEL_COMPLETE) {
                    increaseScore();
                    restartLevel();
                    generateSquares();
                    Toast t = Toast.makeText(getContext(), gs.getNextLevelLabel(getResources()), Toast.LENGTH_SHORT);
                    t.show();
                } else if (ts == TouchStatus.TRY_AGAIN) {
                    decreaseScore();
                    if (t != null) {
                        t.cancel();
                    }
                    Toast t = Toast.makeText(getContext(), gs.getTryAgainLabel(getResources()), Toast.LENGTH_SHORT);
                    t.show();
                } else {
                    ns.tapped();
                    showEndGameDialog();


                }
            }
        }
    }

    /**
     * showEndDialog is the method that will show the dialog message for the end of the game and
     * determines if the user got a new high score or not.
     */

    private void showEndGameDialog() {
        String greetingMessage = "";
        int oldScore;

        try (FileInputStream fis = getContext().openFileInput(gs.toString())){
//            FileInputStream fis = getContext().openFileInput(gs.toString());
            Scanner s = new Scanner(fis);
            oldScore = s.nextInt();
        } catch (IOException e) {
            oldScore = 0;
        }

//            catch(FileNotFoundException e){
//            oldScore = 0;
//        }

        if (score > oldScore) {
            //CHANGE congratulations message
            String congratsMsg = getResources().getString(R.string.congrats_msg);
            greetingMessage += congratsMsg;
            try (FileOutputStream fos = getContext().openFileOutput(gs.toString(), Context.MODE_PRIVATE)) {
                fos.write(("" + score).getBytes());
//                fos.close();
            } catch (IOException e) {
                Log.d("CS203", "This should never need to see this message.");
            }
        }
        //CHANGE play again
        String playAgainMsg = getResources().getString(R.string.play_again);
//        greetingMessage += " Play again?";
        greetingMessage += playAgainMsg;
        AlertDialog.Builder ab = new AlertDialog.Builder(getContext());
        //CHANGE game over
        String gameOver = getResources().getString(R.string.game_over);
        greetingMessage += gameOver;
//        ab.setTitle("GAME OVER!");
        ab.setTitle(gameOver);
        ab.setMessage(greetingMessage);
        ab.setCancelable(false);

        //CHANGE yes
        String positiveButton = getResources().getString(R.string.positive_button);
        ab.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface d, int i) {
                resetScore();
                gs.resetGame();
                generateSquares();

            }
        });

        //CHANGE no
        String negativeButton = getResources().getString(R.string.negative_button);
        ab.setNegativeButton(negativeButton, (d, i) -> {
            ((Activity) getContext()).finish();
        });

        AlertDialog box = ab.create();
        box.show();

    }

    /**
     * tappedSquares helper method
     */
    public void restartLevel() {

        //unregister all previously existing squares from the Timer class
        for (NumberedSquares ns : numSquares) {
            timer.unRegister(ns);
        }

        generateSquares();
    }

    /**
     * helper methods for playing the soundtrack
     */

    private void playSoundEffect() {
        if (soundEffects != null) {
            soundEffects.start();
        }
    }

    private void pauseMusic() {
        if (soundtrack != null) {
            soundtrack.pause();
        }
    }

    private void unPauseMusic() {
        if (soundtrack != null) {
            soundtrack.start();
        }
    }

    public void appIsBackgrounded() {
        if (soundtrack != null) {
            pauseMusic();
        }
    }

    public void appIsForegrounded() {
        if (soundtrack != null) {
            unPauseMusic();
        }
    }

    public void cleanUpBeforeShutdown() {
        if (soundtrack != null) {
            soundtrack.release();
        }
    }

    @Override
    public void update() {
        checkForCollisions();
        invalidate();
    }

    /**
     * Helper methods for keeping score
     */

    private void increaseScore() {
        score += 10;
    }

    private void decreaseScore() {
        score -= 5;
    }

    private void resetScore() {
        score = 0;
    }

}


