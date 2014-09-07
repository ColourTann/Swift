package game.grid.specials;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.assets.Gallery;
import game.grid.Tile;
import game.player.Disc;
import game.player.Disc.Direction;
import util.Draw;
import util.image.Pic;
import util.maths.Pair;

public class VerticalMirror extends Special{

	public VerticalMirror() {
		this.p=Gallery.verticalMirror;
	}

	@Override
	public void render(SpriteBatch batch, Pair position) {
		Draw.drawCentered(batch, p.get(), position.x, position.y+2);
	}

	@Override
	public void activate(Disc d) {
		d.oldDirection=null;
		switch(d.getDirection()){
		case down:
			d.direction=Direction.left;
			break;
		case left:
			d.direction=Direction.down;
			break;
		case right:
			d.direction=Direction.up;
			break;
		case up:
			d.direction=Direction.right;
			break;
		default:
			break;
		}
		d.moveForwards();
	}
	
}
