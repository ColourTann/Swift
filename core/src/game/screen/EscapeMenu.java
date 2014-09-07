package game.screen;

import util.Colours;
import util.update.Screen;
import game.Main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import util.assets.Clip;
import util.assets.Font;
import util.assets.MusicClip;
import util.maths.Pair;
import util.update.Mouser;
import util.update.SimpleButton;
import util.update.SimpleSlider;
import util.update.Timer;
import util.update.SimpleButton.Code;
import util.update.Timer.Interp;
import util.update.Updater.Layer;

public class EscapeMenu extends Screen{
	public static EscapeMenu me;
	public boolean active;
	public Timer alphaTimer=new Timer();



	int sliderWidth=100;

	SimpleSlider sound= new SimpleSlider(new Pair(Main.width/2-sliderWidth/2,20), sliderWidth, 50, Colours.make(85,77,178), Colours.make(134,203,211), 5, Clip.soundLevel);
	SimpleSlider music= new SimpleSlider(new Pair(Main.width/2-sliderWidth/2,80), sliderWidth, 50, Colours.make(85,77,178), Colours.make(134,203,211), 5, MusicClip.musicLevel);

	public EscapeMenu(){
		sound.layer=Layer.Escape;
		music.layer=Layer.Escape;

	}
	
	@Override
	public void init() {
	}

	public static EscapeMenu get() {
		if(me==null)me=new EscapeMenu();
		return me;
	}

	@Override
	public void update(float delta) {
	}

	public void specialUpdate(float delta){

		alphaTimer.update(delta);
	}

	@Override
	public void shapeRender(ShapeRenderer shape) {


	}

	@Override
	public void render(SpriteBatch batch) {
		if(alphaTimer.getFloat()==0)return;
		batch.setColor(1, 1, 1, alphaTimer.getFloat());

		sound.render(batch);
		music.render(batch);



		Font.big.setColor(Colours.withAlpha(Colours.make(247, 247, 239),alphaTimer.getFloat()));
		Font.drawFontCentered(batch, "SFX", Font.big, sound.position.x+sliderWidth/2, sound.position.y+25);
		Font.drawFontCentered(batch, "Music", Font.big, music.position.x+sliderWidth/2, music.position.y+25);
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

	public void cycle() {
		if(active)deactivate();
		else activate();
	}

	public void activate(){
		Mouser.setLayer(Layer.Escape);
		fadeIn();
		active=true;
	}

	public void deactivate(){
		Mouser.setLayer(Layer.Default);
		fadeOut();
		active=false;
	}

	public void fadeIn(){
		Main.fadeTimer=new Timer(Main.fadeTimer.getFloat(), .8f, 3, Interp.LINEAR);
		Main.fadeTimer.layer=Layer.ALL;
		alphaTimer=new Timer(alphaTimer.getFloat(), 1, 3, Interp.LINEAR);
		alphaTimer.layer=Layer.ALL;
	}
	public void fadeOut(){
		Main.fadeTimer=new Timer(Main.fadeTimer.getFloat(), 0, 3, Interp.LINEAR);
		Main.fadeTimer.layer=Layer.ALL;
		alphaTimer=new Timer(alphaTimer.getFloat(), 0, 3, Interp.LINEAR);
		alphaTimer.layer=Layer.ALL;
	}

	@Override
	public void dispose() {
	}

	@Override
	public void renderUI(SpriteBatch batch) {
	}

	






}
