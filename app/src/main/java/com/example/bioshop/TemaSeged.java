package com.example.bioshop;

import android.content.Context;
import android.content.SharedPreferences;

public class TemaSeged {
    private static final String PREF_NAME = "theme_prefs";
    private static final String KEY_THEME = "current_theme";

    public static final int THEME_LIGHT = 0;
    public static final int THEME_DARK = 1;

    public static void temaValaszt(Context context) {
        int theme = getMentettTema(context);
        if (theme == THEME_DARK) {
            context.setTheme(R.style.Base_Theme_BioShop_Dark);
        } else {
            context.setTheme(R.style.Base_Theme_BioShop);
        }
    }

    public static void temaValto(Context context) {
        int currentTheme = getMentettTema(context);
        int newTheme = (currentTheme == THEME_DARK) ? THEME_LIGHT : THEME_DARK;
        temaMentes(context, newTheme);
    }

    public static int getMentettTema(Context context) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        return prefs.getInt(KEY_THEME, THEME_LIGHT); // Default: Light
    }

    public static void temaMentes(Context context, int theme) {
        SharedPreferences prefs = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE);
        prefs.edit().putInt(KEY_THEME, theme).apply();
    }
}
