package com.mwojnar.Game.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.mwojnar.Screens.GameScreen;
import com.mwojnar.Game.GMTKJamGame;

public class DesktopLauncher {
    public static void main (String[] arg) {
        LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.title = "Aqua Ascent";
        config.height = 640;
        config.width = 400;
        config.addIcon("data/Images/icon128.png", FileType.Internal);
        config.addIcon("data/Images/icon32.png", FileType.Internal);
        config.addIcon("data/Images/icon16.png", FileType.Internal);
        //config.fullscreen = true;
        new LwjglApplication(new GMTKJamGame(arg, GameScreen.class), config);
    }
}