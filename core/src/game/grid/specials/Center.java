package game.grid.specials;

import game.assets.Gallery;
import game.grid.Tile;
import game.player.Disc;
import util.Draw;
import util.maths.Pair;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Center extends Special {
	float ticks=0;
	@Override
	public void render(SpriteBatch batch, Pair position) {
		
	}
	@Override
	public void activate(Disc d) {
		d.moveForwards();
	}

}
