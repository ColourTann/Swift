package game;

import game.assets.Gallery;
import game.assets.Sounds;
import game.grid.Tile;
import game.player.Player;
import game.player.Disc.Direction;
import game.screen.EscapeMenu;
import game.screen.Lobby;
import game.screen.GameScreen;
import game.screen.UI;

import java.util.ArrayList;

import util.Draw;
import util.assets.Clip;
import util.assets.MusicClip;
import util.Colours;
import util.Draw.BlendType;
import util.assets.Font;
import util.maths.Pair;
import util.particleSystem.ParticleSystem;
import util.update.InputHandler;
import util.update.Mouser;
import util.update.Screen;
import util.update.TextWisp;
import util.update.Timer;
import util.update.XBox;
import util.update.Timer.Interp;
import util.update.Updater;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.controllers.Controllers;
import com.badlogic.gdx.controllers.PovDirection;
import com.badlogic.gdx.controllers.mappings.Ouya;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.GL30;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;



public class Main extends ApplicationAdapter  {

	public static float fadeInSpeed=2;

	public static boolean debug=false;

	public static int width=400;
	public static int height=250;


	public static SpriteBatch batch;
	public static ShapeRenderer shape;
static float rotation=0;
	//SCREENS//

	public static Screen nextScreen;
	public static Screen currentScreen;
	public static Timer fadeTimer=new Timer();

	public static OrthographicCamera uiCam;
	public static OrthographicCamera mainCam;
	public static float rccd;
	public static float ticks;
	private static Pair baseCam;
	private static Pair shakeBonus=new Pair();
	private static float shakeDrag=.015f;
	private static float shakeFrequency=100;
	private static float shakeAmplitude;

	public static Color fadeColor=Colours.black;
	public static Timer camMoveTimer;

	public static boolean inLobby;

	private static boolean spinning;

	@Override
	public void create () {
		init();

	}
	public static void init(){

		Sounds.tension.load();

		Gdx.input.setInputProcessor(new InputHandler());
		Font.init();
		Gallery.init();
		Tile.init();
		Player.init();
		batch = new SpriteBatch();
		shape = new ShapeRenderer();
		Color bg=Colours.make(48,52,109);
		Gdx.gl.glClearColor(bg.r,bg.g,bg.b,1);
		currentScreen=new Lobby();
		currentScreen.init();
		uiCam=new OrthographicCamera(Main.width, Main.height);
		uiCam.setToOrtho(true);
		uiCam.translate(-Main.width/2, -Main.height/4);

		mainCam=new OrthographicCamera(Main.width, Main.height);
		mainCam.setToOrtho(true);
		mainCam.translate(-Main.width/2, -Main.height/2);

		baseCam=getCam().copy();
		camMoveTimer=new Timer(getCam(), getCam(), 0, Interp.LINEAR);
		int scale=4;
		Gdx.graphics.setDisplayMode(Main.width*scale, Main.height*scale, false);


		for(Controller c:Controllers.getControllers()){

			c.addListener(new ControllerListener() {

				@Override
				public boolean ySliderMoved(Controller controller, int sliderCode,
						boolean value) {
					return false;
				}

				@Override
				public boolean xSliderMoved(Controller controller, int sliderCode,
						boolean value) {
					return false;
				}

				@Override
				public boolean povMoved(Controller controller, int povCode,
						PovDirection value) {
					return false;
				}

				@Override
				public void disconnected(Controller controller) {
				}

				@Override
				public void connected(Controller controller) {
				}

				@Override
				public boolean buttonUp(Controller controller, int buttonCode) {
					return false;
				}

				@Override
				public boolean buttonDown(Controller controller, int buttonCode) {
					
					if(buttonCode==XBox.BUTTON_START) Lobby.startGame();

					Direction dir=null;

					switch(buttonCode){
					case XBox.BUTTON_A:
						dir=Direction.down;
						break;
					case XBox.BUTTON_B:
						dir=Direction.right;
						break;
					case XBox.BUTTON_X:
						dir=Direction.left;
						break;
					case XBox.BUTTON_Y:
						dir=Direction.up;
						break;

					}
					if(dir==null)return false;
					Player.xBoxPress(controller, dir);

					return false;
				}

				@Override
				public boolean axisMoved(Controller controller, int axisCode, float value) {
					return false;
				}

				@Override
				public boolean accelerometerMoved(Controller controller,
						int accelerometerCode, Vector3 value) {
					return false;
				}
			});
		}
	}	




	public void update(float delta){

		
		if(spinning){
			mainCam.rotate(delta*20);
			rotation+=delta*20;
		}
		ticks+=delta;
		Updater.updateAll(delta);
		currentScreen.update(delta);
		updateScrenShake(delta);
		updateCam();
		if(nextScreen!=null){
			if(fadeTimer.getFloat()==1){
				Updater.clearAll();
				nextScreen.init();
				fadeTimer=new Timer(1, 0, 2, Interp.LINEAR);

				currentScreen=nextScreen;
				nextScreen=null;			}
			return;
		}


	}

	public void updateCam(){
		
		setCam(camMoveTimer.getPair().add(shakeBonus));
	}

	public static void addShake(float amount){
		shakeAmplitude+=amount;
	}

	private void updateScrenShake(float delta){
		shakeAmplitude*=Math.pow(shakeDrag, delta);
		shakeBonus = new Pair(
				Math.sin(ticks*shakeFrequency)*shakeAmplitude,
				Math.sin((ticks*1.1f+100)*shakeFrequency)*shakeAmplitude
				);
	}

	@Override
	public void render () {
		float delta = Gdx.graphics.getDeltaTime();
		delta=Math.min(.1f, delta);
		update(delta);

		Draw.setBlend(batch, BlendType.Normal);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		batch.setProjectionMatrix(mainCam.combined);

		batch.begin();
		ParticleSystem.renderAll(batch);
		batch.end();

		shape.setProjectionMatrix(mainCam.combined);
		shape.setColor(1, 1, 1, 1);
		currentScreen.shapeRender(shape);

		batch.setProjectionMatrix(mainCam.combined);
		batch.begin();
		batch.setColor(Colours.white);
		currentScreen.render(batch);

		currentScreen.postRender(batch);
		for(TextWisp t:TextWisp.wisps)t.render(batch);



		//UI//
		batch.end();
		batch.setProjectionMatrix(uiCam.combined);
		batch.begin();
		currentScreen.renderUI(batch);
		//UI.get().render(batch);
		if(debug){
			Font.small.setColor(Colours.white);
			Font.small.draw(batch, "FPS: "+(int)(1/delta), 0, 0);
		}

		//FADING//
		batch.setColor(Colours.withAlpha(fadeColor, fadeTimer.getFloat()));
		Draw.drawScaled(batch, Gallery.whiteSquare.get(), 0, 0, width, height);



		EscapeMenu.get().render(batch);
		batch.end();


	}




	public static void changeScreen(Screen next){
		nextScreen=next;


		fadeColor=Player.winningColor;

		fadeTimer=new Timer(fadeTimer.getFloat(), 1, 2, Interp.LINEAR);
	}



	public static Pair getCam(){
		return new Pair(mainCam.position.x,mainCam.position.y);
	}

	public static void setCam(Pair cam){
		mainCam.position.set((float)Math.round(cam.x), (float)Math.round(cam.y), 0);
		mainCam.update();
	}

	public static Screen getCurrentInputScreen(){
		if(EscapeMenu.get().active)return EscapeMenu.get();
		return currentScreen;
	}

	public static void keyPress(int keycode) {
		int scale=1;
		switch(keycode){
		case Input.Keys.ESCAPE:
			EscapeMenu.get().cycle();
			break;

			//P1
		case Input.Keys.W:
			Player.p1.pressButton(Direction.up);
			break;
		case Input.Keys.A:
			Player.p1.pressButton(Direction.left);
			break;
		case Input.Keys.S:
			Player.p1.pressButton(Direction.down);
			break;
		case Input.Keys.D:
			Player.p1.pressButton(Direction.right);
			break;

			//P2
		case Input.Keys.T:
			Player.p2.pressButton(Direction.up);
			break;
		case Input.Keys.F:
			Player.p2.pressButton(Direction.left);
			break;
		case Input.Keys.G:
			Player.p2.pressButton(Direction.down);
			break;
		case Input.Keys.H:
			Player.p2.pressButton(Direction.right);
			break;

			//P3
		case Input.Keys.I:
			Player.p3.pressButton(Direction.up);
			break;
		case Input.Keys.J:
			Player.p3.pressButton(Direction.left);
			break;
		case Input.Keys.K:
			Player.p3.pressButton(Direction.down);
			break;
		case Input.Keys.L:
			Player.p3.pressButton(Direction.right);
			break;

			//P4
		case Input.Keys.UP:
			Player.p4.pressButton(Direction.up);
			break;
		case Input.Keys.LEFT:
			Player.p4.pressButton(Direction.left);
			break;
		case Input.Keys.DOWN:
			Player.p4.pressButton(Direction.down);
			break;
		case Input.Keys.RIGHT:
			Player.p4.pressButton(Direction.right);
			break;
		case Input.Keys.NUM_1:
			scale=1;
			Gdx.graphics.setDisplayMode(Main.width*scale, Main.height*scale, false);
			break;
		case Input.Keys.NUM_2:
			scale=2;
			Gdx.graphics.setDisplayMode(Main.width*scale, Main.height*scale, false);
			break;
		case Input.Keys.NUM_3:
			scale=3;
			Gdx.graphics.setDisplayMode(Main.width*scale, Main.height*scale, false);
			break;
		case Input.Keys.NUM_4:
			scale=4;
			Gdx.graphics.setDisplayMode(Main.width*scale, Main.height*scale, false);
			break;
		case Input.Keys.NUM_5:
			scale=5;
			Gdx.graphics.setDisplayMode(Main.width*scale, Main.height*scale, false);
			break;
		}



		getCurrentInputScreen().keyPress(keycode);
		mainCam.update();
	}

	public static void keyUp(int keyCode) {
		getCurrentInputScreen().keyUp(keyCode);
	}
	public static void touchDown(Pair location, boolean left) {
		getCurrentInputScreen().mousePressed(location, left);
	}

	public static void scrolled(int amount){
		getCurrentInputScreen().scroll(amount);
	}
	public static void setNotSpinning() {
		mainCam.rotate(-rotation);
		rotation=0;
		spinning=false;
	}
	public static void setSpinning() {
	
		spinning=true;
	}




}
