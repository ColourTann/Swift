package game.screen;

import game.Main;
import game.assets.Gallery;

import java.util.ArrayList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import util.Colours;
import util.Draw;
import util.update.Screen;
import util.assets.Font;
import util.maths.Pair;
import util.update.SimpleButton;
import util.update.SimpleButton.Code;

public class UI extends Screen{
	private static UI me;
	

	public UI(){

	}

	@Override
	public void init() {
	}
	public static UI get(){
		if(me==null)me=new UI();
		return me;
	}
	
	public static void reset(){
		me=null;
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
	}




}
