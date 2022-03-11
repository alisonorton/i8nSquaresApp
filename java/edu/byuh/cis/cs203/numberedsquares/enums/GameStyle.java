package edu.byuh.cis.cs203.numberedsquares.enums;

import android.content.res.Resources;

import java.util.List;

import edu.byuh.cis.cs203.numberedsquares.ui.NumberedSquares;

public interface GameStyle {
    String getNextLevelLabel(Resources res);
    String getTryAgainLabel(Resources res);
    List<String> getSquareLabels();
    TouchStatus getTouchStatus(NumberedSquares ns);
    void resetGame();
}
