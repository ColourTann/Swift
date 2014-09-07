package game.desktop;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;

import game.Main;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width=Main.width;
		config.height=Main.height;
		config.vSyncEnabled=!Main.debug;
		config.foregroundFPS=0;
		config.title="Swift";
		config.addIcon("icon.png", FileType.Internal);
		new LwjglApplication(new Main(), config);
	}
}
