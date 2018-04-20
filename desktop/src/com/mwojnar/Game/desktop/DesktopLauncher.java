package com.mwojnar.Game.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.mwojnar.Screens.GameScreen;
import com.mwojnar.Game.GMTKJamGame;

public class DesktopLauncher {
    public static void main (String[] arg) {
        Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
        config.setTitle("Aqua Ascent");
        config.setWindowedMode(400, 640);
        config.setWindowIcon("data/Images/icon128.png", "data/Images/icon32.png", "data/Images/icon16.png");
        new Lwjgl3Application(new GMTKJamGame(arg, GameScreen.class), config);
    }
}