package edu.byuh.cis.cs203.numberedsquares.gamestyle;

import android.content.res.Resources;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import edu.byuh.cis.cs203.numberedsquares.R;
import edu.byuh.cis.cs203.numberedsquares.enums.GameStyle;
import edu.byuh.cis.cs203.numberedsquares.ui.NumberedSquares;
import edu.byuh.cis.cs203.numberedsquares.enums.TouchStatus;

/**
 * A basic Japanese spelling game to learn Hiragana and Kanji
 */

public class JapaneseSpelling implements GameStyle {

    private String[] words;
    private int levelIndex;
    private int expectedIndex;
    private List<String> squareLabels;
    private String currentWord;
    private Resources res;
//    private int finalLevel;

    public JapaneseSpelling(Resources r){
        expectedIndex = 0;
        levelIndex = 0;
        words = new String[]{"いぬ", "さかな", "こひつじ", "アマガエル", "犬魚子羊雨蛙","これは特別なレベル"};
        currentWord = "";
        squareLabels = new ArrayList<>();
        res = r;
//        finalLevel = 6;

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
        currentWord = "";
    }

    /**
     * Methods related to strings and labels in the game.
     */

    @Override
    public String toString(){
        String gameTitle = res.getString(R.string.japanese_game_title);
        return gameTitle;

//        return "Japanese Spelling";
    }

    @Override
    public String getNextLevelLabel(Resources res) {
        String levelLabel;
        if(levelIndex > 4){
            levelLabel = (levelIndex + 1) + res.getString(R.string.kanji_message);
        }
        else{
            levelLabel = (levelIndex + 1) + res.getString(R.string.next_level_msg)  + " "+ words[levelIndex] + res.getString(R.string.spelling_next_level_msg_2);
        }
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
            squareLabels.add(""+currentWord.charAt(i));
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
