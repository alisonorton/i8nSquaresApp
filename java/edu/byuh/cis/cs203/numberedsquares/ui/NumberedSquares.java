package edu.byuh.cis.cs203.numberedsquares.ui;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.RectF;
import android.view.View;

import edu.byuh.cis.cs203.numberedsquares.enums.HitSquares;
import edu.byuh.cis.cs203.numberedsquares.activities.Prefs;
import edu.byuh.cis.cs203.numberedsquares.R;
import edu.byuh.cis.cs203.numberedsquares.util.TimerListener;

/**
 * This is the NumberedSquares class.
 * This is where we set the ID and location of our numbers, and then draw the number.
 */

public class NumberedSquares implements TimerListener {
    private float size;
    private int serial;
    private static int counter = 1;
    private PointF velocity;
    private RectF bounds;
    private Paint boxes, numbers;
    private int speed;
    private float screenWidth;
    private float screenHeight;
    private boolean exploded;
    Bitmap gift;
    Bitmap explosion;
    String label;

    /**
     * The default constructor for NumberedSquares class.
     */

    public NumberedSquares(View parent, String label, int i, RectF r, Resources res){
        //counter
        this.label = label;
        serial = i;
        counter++;
        bounds = r;
        this.screenWidth = parent.getWidth();
        screenHeight = parent.getHeight();
        size = screenWidth*0.1f;
        speed = Prefs.getSpeed(parent.getContext());
        velocity = new PointF();
        int min = -1;
        int max = 1;
        int range = max - min;
        velocity.x = (float) (size * 0.08 - Math.random() * size * 0.16) *speed;
        velocity.y = (float) (size * 0.08 - Math.random() * size * 0.16) *speed;
        exploded = false;

        gift = BitmapFactory.decodeResource(res, R.drawable.gift);
        gift = Bitmap.createScaledBitmap(gift, (int)bounds.width(), (int)bounds.height(), true);

        explosion = BitmapFactory.decodeResource(res, R.drawable.explosion);
        explosion = Bitmap.createScaledBitmap(explosion, (int)bounds.width(), (int)bounds.height(), true);

        boxes = new Paint();
        numbers = new Paint();

        numbers.setTextSize(screenWidth*0.05f);
        numbers.setColor(Color.GREEN);
        numbers.setTextAlign(Paint.Align.CENTER);

    }

    /**
     * This method is where we reset our counter so we can draw 5 new squares and our serial
     * numbers are still 1 - 5.
     */


    public static void resetCounter() {
        counter = 1;
    }


    //Compares the serial numbers of two squares

    public boolean checkSerial(NumberedSquares ns){
        if(this.serial > ns.serial){
            return true;
        }
        else{
            return false;
        }
    }

    public int getSerial(){
        return serial;
    }

    public String getLabel(){
        return label;
    }

    public boolean getExploded(){
        return exploded;
    }

    /**
     * The getCollisionSide will determine which sides of the two squares are
     * colliding so we can find which velocities we need to exchange later
     * in the program
     * @param other second NumberedSquare for comparison
     */

    public void getCollisionSide(NumberedSquares other){
        float sqrR;
        float sqrL;
        float sqrT;
        float sqrB;

        float smallest;

        sqrR = Math.abs(this.bounds.right - other.bounds.left);
        sqrL = Math.abs(this.bounds.left - other.bounds.right);
        sqrT = Math.abs(this.bounds.top - other.bounds.bottom);
        sqrB = Math.abs(this.bounds.bottom - other.bounds.top);

        smallest = Math.min(Math.min(sqrB, sqrT), Math.min(sqrR, sqrL));

        if(smallest == sqrR){
            exchangeVelocity(HitSquares.RIGHT, other);
        }
        if(smallest == sqrL){
            exchangeVelocity(HitSquares.LEFT, other);
        }
        if(smallest == sqrT){
            exchangeVelocity(HitSquares.TOP, other);
        }
        if(smallest == sqrB){
            exchangeVelocity(HitSquares.BOTTOM, other);
        }

    }


    public RectF getBounds(){
        return bounds;
    }

    /**
     * This method checks if two squares overlap
     * @param ns the NumberedSquare object we are comparing this to.
     * @return bool if true or false.
     */

    public boolean overlaps(RectF ns){
        return RectF.intersects(this.bounds, ns);
    }

    /**
     * This is a helper method that allows us to check if two squares are overlapping.
     * @param x coordinate of square on the screen.
     * @param y coordinate of square on the screen.
     * @return
     */

    public boolean contains(float x, float y){
        return bounds.contains(x, y);
    }


    /**
     * Drawing the numSquares by their serial number at a random location.
     * @param c the canvas object our method will draw on.
     */

    public void draw(Canvas c){
        if(exploded){
            c.drawBitmap(explosion, bounds.left, bounds.top, boxes);
            c.drawText(""+label, bounds.centerX(), bounds.centerY()+size*0.2f,numbers);
        }
        else{
            c.drawBitmap(gift, bounds.left, bounds.top, boxes);
            c.drawText(""+label, bounds.centerX(), bounds.centerY()+size*0.2f,numbers);
        }
//        c.drawText(""+score, )
    }

    /**
     * How the square changes visually and in terms of velocity when it's tapped by the user.
     */

    public void tapped(){
        exploded = true;
        velocity.x = 0;
        velocity.y = 0;
        boxes.setColor(Color.WHITE);
        numbers.setColor(Color.BLACK);

    }

    /**
     * The move method uses boolean logic to see if a square has reached the edge of
     * the screen and then reverses that squares velocity. This makes the square "bounce"
     * off the edge of the screen.
     */

    public void move(){
        if(bounds.left < 0 || bounds.right > screenWidth){
            velocity.x = velocity.x*-1;
            if(bounds.left < 0){
                setLeft(1);
            }
            else{
                setRight(screenWidth - 1);
            }
        }
        if(bounds.top < 0 || bounds.bottom > screenHeight){
            velocity.y = velocity.y*-1;
            if(bounds.top < 0){
                setTop(1);
            }
            else{
                setBottom(screenHeight - 1);
            }
        }

        bounds.offset(velocity.x, velocity.y);

    }

    /**
     * This method keeps squares from getting stuck together by moving them apart
     * one pixel each in the opposite direction of the square they're colliding with
     * @param other the other NumberedSquare object used for comparison
     * @param hs the side of this square that was hit.
     */

    public void forceApart(NumberedSquares other, HitSquares hs) {
        RectF myBounds = new RectF(this.bounds);
        RectF otherBounds = new RectF(other.bounds);
        if (this.exploded == true || other.exploded == true) {
            switch (hs) {
                case RIGHT:
                    if(this.exploded){
                        other.setLeft(myBounds.right + 1);
                    }
                    else{
                        this.setRight(otherBounds.left - 1);
                    }
                    break;
                case LEFT:
                    if(this.exploded){
                        other.setRight(myBounds.left - 1);
                    }
                    else{
                        this.setLeft(otherBounds.right + 1);
                    }
                    break;
                case TOP:
                    if(this.exploded){
                        other.setBottom(myBounds.top - 1);
                    }
                    else{
                        this.setTop(otherBounds.bottom + 1);
                    }
                    break;
                case BOTTOM:
                    if(this.exploded){
                        other.setTop(myBounds.bottom + 1);
                    }
                    else{
                        this.setBottom(otherBounds.top - 1);
                    }
            }
        }
        else{
            switch (hs) {
                case RIGHT:
                    this.setRight(otherBounds.left - 1);
                    other.setLeft(myBounds.right + 1);
                    break;
                case LEFT:
                    this.setLeft(otherBounds.right + 1);
                    other.setRight(myBounds.left - 1);
                    break;
                case TOP:
                    this.setTop(otherBounds.bottom + 1);
                    other.setBottom(myBounds.top - 1);
                    break;
                case BOTTOM:
                    this.setBottom(otherBounds.top - 1);
                    other.setTop(myBounds.bottom + 1);
            }

        }

    }

    private void setBottom(float b){
        float dy = b - bounds.bottom;
        bounds.offset(0, dy);
    }

    private void setRight(float b){
        float dx = b - bounds.right;
        bounds.offset(0, dx);
    }

    private void setLeft(float lf) {
        bounds.offsetTo(lf, bounds.top);
    }

    private void setTop(float t) {
        bounds.offsetTo(bounds.left, t);
    }


    /**
     * The changeVelocity method is where we exchange velocity based on which
     * sides of the two squares are colliding
     * @param hs which side of the square that was hit
     * @param otherNs the square we're exchanging velocities with
     */

    public void exchangeVelocity(HitSquares hs, NumberedSquares otherNs){
        float v;
        forceApart(otherNs, hs);
        if(hs == HitSquares.RIGHT || hs == HitSquares.LEFT){
            if(this.exploded == true || otherNs.exploded == true){
                reverseVelocity(hs,otherNs);
            }
            else {
                v = this.velocity.x;
                this.velocity.x = otherNs.velocity.x;
                otherNs.velocity.x = v;
            }
        }
        if(hs == HitSquares.TOP || hs == HitSquares.BOTTOM){
            if(this.exploded == true || otherNs.exploded == true){
                reverseVelocity(hs,otherNs);
            }
            else {
                v = this.velocity.y;
                this.velocity.y = otherNs.velocity.y;
                otherNs.velocity.y = v;
            }
        }
    }

    /**
     * Reversing the velocity of squares that collide with a frozen square.
     * @param hs the side of this square that's being hit
     * @param other the other NumberedSquare object we're comparing this to.
     */
    public void reverseVelocity(HitSquares hs, NumberedSquares other){

        NumberedSquares test;

        if(this.exploded == true){
            test = other;
        }
        else{
            test = this;
        }

        if(hs == HitSquares.RIGHT || hs == HitSquares.LEFT){
            test.velocity.x *=-1;
        }

        if(hs == HitSquares.TOP || hs == HitSquares.BOTTOM){
            test.velocity.y *=-1;
        }
    }

    @Override
    public void update() {
        move();
    }

}
