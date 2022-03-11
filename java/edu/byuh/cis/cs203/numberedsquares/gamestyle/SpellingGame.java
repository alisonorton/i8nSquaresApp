package edu.byuh.cis.cs203.numberedsquares.gamestyle;

import android.content.res.Resources;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.byuh.cis.cs203.numberedsquares.R;
import edu.byuh.cis.cs203.numberedsquares.enums.GameStyle;
import edu.byuh.cis.cs203.numberedsquares.ui.NumberedSquares;
import edu.byuh.cis.cs203.numberedsquares.enums.TouchStatus;

public class SpellingGame implements GameStyle {
    private String[] words;
    private int levelIndex;
    private int expectedIndex;
    private List<String> squareLabels;
    private String currentWord;
    private Resources res;

    public SpellingGame(Resources r){
        expectedIndex = 0;
        levelIndex = 0;
        res = r;
        words = new String[]{"Dog", "Fish", "Shady", "People"};
        currentWord = "";
        squareLabels = new ArrayList<>();

    }

    private void resetExpectedIndex(){
        expectedIndex = 0;
    }

    private void resetLevelIndex(){
        levelIndex = 0;
    }

    @Override
    public void resetGame(){
        resetExpectedIndex();
        resetLevelIndex();
    }

    /**
     * Methods related to strings and labels in the game.
     */

    @Override
    public String toString(){
        String gameTitle = res.getString(R.string.spelling_game_title);
        return gameTitle;
    }

    @Override
    public String getNextLevelLabel(Resources res) {
        String levelLabel = res.getString(R.string.spelling_next_level_msg)  + " " + (levelIndex+1)
                + res.getString(R.string.exclamation) + " " + res.getString(R.string.spelling_next_level_msg_2)
                + " " + words[levelIndex];
        return levelLabel;
    }

    @Override
    public String getTryAgainLabel(Resources res) {
        String tryAgainLabel = res.getString(R.string.try_again_label);
        return tryAgainLabel;
    }

    @Override
    public List<String> getSquareLabels() {
        currentWord = words[levelIndex];
        squareLabels.clear();

        for(int i = 0; i < currentWord.length(); i++){
            System.out.println(squareLabels);
            squareLabels.add(""+currentWord.charAt(i));
            Log.d("SQR_LABELS", squareLabels.get(i));
        }

        Log.d("SQR_LABELS", String.valueOf(squareLabels.size()));
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
        String sqrLabel = ns.getLabel();
        String expectedLabel = ""+currentWord.charAt(expectedIndex);

        if(sqrLabel.equals(expectedLabel)){
            expectedIndex++;
            if(expectedIndex == currentWord.length()){
                resetExpectedIndex();
                levelIndex++;
                if(levelIndex > words.length-1){
                    return TouchStatus.GAME_OVER;
                }
                else {
                    return TouchStatus.LEVEL_COMPLETE;
                }

            }
            else{
                return TouchStatus.CONTINUE;
            }
        }
        else{
            return TouchStatus.TRY_AGAIN;
        }
    }
}
