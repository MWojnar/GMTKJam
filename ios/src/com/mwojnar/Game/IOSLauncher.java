package com.mwojnar.Game;

import org.robovm.apple.coregraphics.CGRect;
import org.robovm.apple.foundation.NSAutoreleasePool;
import org.robovm.apple.foundation.NSNumber;
import org.robovm.apple.uikit.UIApplication;
import org.robovm.apple.uikit.UIWindow;

import com.badlogic.gdx.backends.iosrobovm.IOSApplication;
import com.badlogic.gdx.backends.iosrobovm.IOSApplicationConfiguration;
import com.mwojnar.Screens.GameScreen;
import com.mwojnar.Game.GMTKJamGame;

public class IOSLauncher extends IOSApplication.Delegate {
    @Override
    protected IOSApplication createApplication() {
        IOSApplicationConfiguration config = new IOSApplicationConfiguration();
        config.orientationPortrait = false;
        config.orientationLandscape = true;
        return new IOSApplication(new GMTKJamGame(GameScreen.class), config);
    }

    public static void main(String[] argv) {
    	NSAutoreleasePool pool = new NSAutoreleasePool();
        UIApplication.main(argv, null, IOSLauncher.class);
        pool.close();
    }
    
}