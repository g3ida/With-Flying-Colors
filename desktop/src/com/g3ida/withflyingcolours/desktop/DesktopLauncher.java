package com.g3ida.withflyingcolours.desktop;

import com.badlogic.gdx.backends.lwjgl3.Lwjgl3Application;
import com.badlogic.gdx.backends.lwjgl3.Lwjgl3ApplicationConfiguration;
import com.g3ida.withflyingcolours.WithFlyingColours;

public class DesktopLauncher {
	public static void main (String[] arg) {
		Lwjgl3ApplicationConfiguration config = new Lwjgl3ApplicationConfiguration();
		config.setTitle("With flying colours");
		config.setWindowedMode(800, 600);
		new Lwjgl3Application(new WithFlyingColours(), config);
	}
}
