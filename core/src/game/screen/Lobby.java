package game.screen;

import java.util.ArrayList;

import game.Main;
import game.assets.Gallery;
import game.assets.Sounds;
import game.player.Player;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import util.Colours;
import util.Draw;
import util.update.Screen;
import util.assets.Font;
import util.maths.Pair;
import util.update.SimpleButton;
import util.update.SimpleButton.Code;

public class Lobby extends Screen {
	SimpleButton button; 
	public static int border=10;
	@Override
	public void init() {
		Main.inLobby=true;
		for(Player p:Player.players){
			p.reset();
		}
	}
	
	@Override
	public void update(float delta) {
	}

	@Override
	public void shapeRender(ShapeRenderer shape) {
	}

	@Override
	public void render(SpriteBatch batch) {
		
	}

	@Override
	public void postRender(SpriteBatch batch) {
	}

	@Override
	public void keyPress(int keycode) {
		switch(keycode){
		case Input.Keys.ENTER:
			startGame();
			break;
		
		}
		
	}

	@Override
	public void keyUp(int keyCode) {
	}

	@Override
	public void mousePressed(Pair location, boolean left) {
		
	}

	@Override
	public void scroll(int amount) {
	}

	@Override
	public void dispose() {
	}

	@Override
	public void renderUI(SpriteBatch batch) {
		batch.setColor(Colours.blues3[1]);
		Draw.drawScaledCentered(batch, Gallery.whiteSquare.get(), Main.width/2, Main.height/2, Main.width, border);
		Draw.drawScaledCentered(batch, Gallery.whiteSquare.get(), Main.width/2, Main.height/2, border, Main.height);
		for(Player p:Player.players)p.renderLobby(batch);
		
		batch.setColor(Colours.blues3[1]);
		Draw.drawScaledCentered(batch, Gallery.whiteSquare.get(), Main.width/2, Main.height/2, 155, 20);
		Font.small.setColor(Colours.bg);
		Font.drawFontCentered(batch, "Press "+(Player.xBox?"Start":"Enter")+" to play", Font.small, Main.width/2, Main.height/2);
	}
	
	public static void startGame(){
		int players=0;
		for(Player p:Player.players){
			if(p.active)players++;
		}
		if(players>1){
			Sounds.activate.play();
			Main.changeScreen(new GameScreen());
		}
		
	}
	

	

}
