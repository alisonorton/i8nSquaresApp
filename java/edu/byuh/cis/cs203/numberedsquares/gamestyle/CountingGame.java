package edu.byuh.cis.cs203.numberedsquares.gamestyle;

import android.content.res.Resources;

import java.util.ArrayList;
import java.util.List;

import edu.byuh.cis.cs203.numberedsquares.R;
import edu.byuh.cis.cs203.numberedsquares.enums.GameStyle;
import edu.byuh.cis.cs203.numberedsquares.ui.NumberedSquares;
import edu.byuh.cis.cs203.numberedsquares.enums.TouchStatus;

/**
 * This game style is a simple counting game where the user taps the squares in sequential order.
 */

public class CountingGame implements GameStyle {
    private int level;
    private int expectedId;
    private List<String> squareLabels;
    private Resources res;

    public CountingGame(Resources r){
        level = 1;
        expectedId = 1;
        res = r;
    }

    private void resetExpectedId(){
        expectedId = 1;
    }

    private void resetLevel(){ level = 1;
    }

    @Override
    public void resetGame(){
        resetLevel();
        resetExpectedId();
    }

    /**
     * Methods related to strings and labels in the game.
     */

    @Override
    public String toString(){
        String gameTitle = res.getString(R.string.game_title);
        return gameTitle;
    }

    @Override
    public String getNextLevelLabel(Resources res) {
        String nextLevel =  res.getString(R.string.next_level_msg) + " " + level + res.getString(R.string.exclamation);
        return nextLevel;
    }

    @Override
    public String getTryAgainLabel(Resources res) {

        String tryAgainLabel = res.getString(R.string.try_again_label);
        return tryAgainLabel;
    }

    @Override
    public List<String> getSquareLabels() {
        squareLabels = new ArrayList<>();
        for(int i = 1; i <= level; i++){
            squareLabels.add(""+i);
        }
        return squareLabels;
    }

    /**
     * This method compares "this" with the NumberedSquare being passed to determine how the
     * game should respond to the users input.
     * @param ns the NumberedSquare that we're comparing "this" to.
     * @return TouchStatus which determines how the game will respond to the user's action
     */

    @Override
    public TouchStatus getTouchStatus(NumberedSquares ns) {
        int finalLevel = 7;
        String sqrLabel = ns.getLabel();
        String expectedLabel = squareLabels.get(expectedId-1);

        if(sqrLabel.equals(expectedLabel)){
            expectedId++;
            if(expectedId > squareLabels.size()){
                resetExpectedId();
                level++;
                if(level == finalLevel){
                    return TouchStatus.GAME_OVER;
                }
                else{
                    return TouchStatus.LEVEL_COMPLETE;
                }
            }
            else{
                return TouchStatus.CONTINUE;
            }
        }
        else {
            return TouchStatus.TRY_AGAIN;
        }
    }
}
