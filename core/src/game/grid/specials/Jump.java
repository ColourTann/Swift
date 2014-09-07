package game.grid.specials;

import game.assets.Gallery;
import game.grid.Tile;
import game.player.Disc;
import util.Draw;
import util.maths.Pair;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Jump extends Special{

	@Override
	public void render(SpriteBatch batch, Pair position) {
		Draw.drawCentered(batch, Gallery.wing.get(), position.x, position.y+5);
	}

	@Override
	public void activate(Disc d) {
		d.jump();
		d.moveForwards();
	}

}
