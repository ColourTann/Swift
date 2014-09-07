package game.screen;

import java.util.ArrayList;

import game.Main;
import game.assets.Sounds;
import game.grid.Grid;
import game.player.Disc.Direction;
import game.player.Player;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;

import util.update.Screen;
import util.update.Updater;
import util.maths.Pair;

public class GameScreen extends Screen{
	public static Grid grid;
	public static ArrayList<Player> players = new ArrayList<Player>();
	public static boolean choosing=false;
	public GameScreen(){

	}



	@Override
	public void init() {
		
		players.clear();
		for(Player p:Player.players)if(p.active){
			players.add(p);
			p.resetDisc();
		}
		grid=new Grid(5);
		grid.init();
		choosing=true;
	}

	@Override
	public void update(float delta) {


	}


	@Override
	public void shapeRender(ShapeRenderer shape) {


	}

	@Override
	public void render(SpriteBatch batch) {

		for(Player p:players){
			p.renderUI(batch);
		}

		batch.setColor(1,1,1,1);
		grid.render(batch);

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
	public void postRender(SpriteBatch batch) {
	}

	@Override
	public void keyPress(int keycode) {
		/*switch (keycode){
		case Input.Keys.R:
			Updater.clearAll();
			init();
			break;
		case Input.Keys.SPACE:
			grid.moveNextPlayer();
			break;
		}*/


		//Main.changeScreen(new OtherScreen());
	}

	@Override
	public void dispose() {
	}



	@Override
	public void renderUI(SpriteBatch batch) {

	}



	public static boolean isChoosing() {
		return choosing;
	}

	public static void playerChosen(Player p, Direction direction){
		if(grid.directions.contains(direction)){
			grid.orderedPlayers.add(p);
			p.dir=direction;
			Sounds.tone.play();
			for(Player pl:players){
				if(pl.dir==null)return;
			}
			grid.finishInput(true);
		}
	}

}
