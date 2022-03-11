package edu.byuh.cis.cs203.numberedsquares.activities;

import android.content.Context;
import android.os.Bundle;
import android.preference.ListPreference;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.preference.SwitchPreference;

import edu.byuh.cis.cs203.numberedsquares.R;

public class Prefs extends PreferenceActivity {

    @Override
    public void onCreate (Bundle b){
        super.onCreate(b);
        PreferenceScreen ps = getPreferenceManager().createPreferenceScreen(this);
        SwitchPreference backgroundMusic = new SwitchPreference(this);
        String bkgrdMusic = getResources().getString(R.string.background_music);
        String bkgrdMusicOn = getResources().getString(R.string.bkgrd_image_summary);
        String bkgrdMusicOff = getResources().getString(R.string.bkgrd_music_off);

        backgroundMusic.setTitle(bkgrdMusic);
        backgroundMusic.setSummaryOn(bkgrdMusicOn);
        backgroundMusic.setSummaryOff(bkgrdMusicOff);
        backgroundMusic.setDefaultValue(true);
        backgroundMusic.setKey("BACKGROUND_MUSIC_PREF");
        ps.addPreference(backgroundMusic);

        SwitchPreference soundEffects = new SwitchPreference(this);
        String soundFx = getResources().getString(R.string.sound_effects);
        String soundFxOn = getResources().getString(R.string.sound_effects_on);
        String soundFxOff = getResources().getString(R.string.sound_effects_off);

        soundEffects.setTitle(soundFx);
        soundEffects.setSummaryOn(soundFxOn);
        soundEffects.setSummaryOff(soundFxOff);
        soundEffects.setDefaultValue(true);
        soundEffects.setKey("SOUND_EFFECTS_PREF");
        ps.addPreference(soundEffects);

        ListPreference speed = new ListPreference(this);
        String squareSpeed = getResources().getString(R.string.speed_pref);
        String squareSpeedSummary = getResources().getString(R.string.speed_pref_summary);
        speed.setTitle(squareSpeed);
        speed.setSummary(squareSpeedSummary);
        speed.setKey("SQUARE_SPEED_PREF");
//        String[] entries = {"Fast", "Medium", "Slow"};
        speed.setEntries(R.array.speed_entries);
        String[] values = {"3", "2", "1"};
        speed.setEntryValues(values);
        speed.setDefaultValue("2");
        ps.addPreference(speed);

        ListPreference backgroundImage = new ListPreference(this);
        String bkgrdImage = getResources().getString(R.string.bkgrd_image);
        String bkgrdImageSummary = getResources().getString(R.string.bkgrd_image_summary);
        backgroundImage.setTitle(bkgrdImage);
        backgroundImage.setSummary(bkgrdImageSummary);
        backgroundImage.setKey("BKGRD_IMAGE_PREF");
//        String[] imgEntries = {"Winter Wonderland", "Tropical Beach", "Out Of This World"};
        backgroundImage.setEntries(R.array.bkgrd_image_options);
        backgroundImage.setEntryValues(R.array.bkgrd_image_options);
        ps.addPreference(backgroundImage);

        ListPreference gameType = new ListPreference(this);
        String gameStyle = getResources().getString(R.string.game_style);
        String gameStyleSummary = getResources().getString(R.string.game_style_summary);
        gameType.setTitle(gameStyle);
        gameType.setSummary(gameStyleSummary);
        gameType.setKey("GAME_TYPE");
//        String[] selectGameType = {"Counting Game", "Spelling Game", "Japanese Spelling Game"};
        gameType.setEntries(R.array.game_style_options);
        gameType.setEntryValues(R.array.game_style_options);
        ps.addPreference(gameType);


        setPreferenceScreen(ps);

    }

    public static boolean soundtrackOn(Context c){
        return PreferenceManager.getDefaultSharedPreferences(c).getBoolean("BACKGROUND_MUSIC_PREF", true);
    }

    public static boolean soundEffectsOn(Context c){
        return PreferenceManager.getDefaultSharedPreferences(c).getBoolean("SOUND_EFFECTS_PREF", true);
    }

    public static int getSpeed(Context c){
        return Integer.parseInt(PreferenceManager.getDefaultSharedPreferences(c).getString("SQUARE_SPEED_PREF", "2"));
    }

    public static int getBackgroundImage(Context c){
        String bkgrdImage = PreferenceManager.getDefaultSharedPreferences(c).getString("BKGRD_IMAGE_PREF", "Winter Wonderland");
        int imageId = 0;

        switch (bkgrdImage){
            case "Winter Wonderland":
                imageId = R.drawable.winter_wonderland;
                break;
            case "Tropical Beach":
                imageId = R.drawable.tropical_beach;
                break;
            case "Out Of This World":
                imageId = R.drawable.outer_space;
        }

        return imageId;
    }

    public static int getGameType(Context c){
        String gameType = PreferenceManager.getDefaultSharedPreferences(c).getString("GAME_TYPE", "Counting Game");
        int gameId = 0;
        switch(gameType){
            case "Counting Game":
                gameId = 1;
                break;
            case "Spelling Game":
                gameId = 2;
                break;
            case "Japanese Spelling Game":
                gameId = 3;
        }
        return gameId;
    }

}
