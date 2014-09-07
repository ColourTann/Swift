package game.grid.specials;

import game.assets.Gallery;
import game.grid.Tile;
import game.player.Disc;
import game.player.Disc.Direction;
import util.Draw;
import util.maths.Pair;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class HorizontalMirror extends Special{
	public HorizontalMirror(){
		this.p=Gallery.horizontalMirror;
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
			d.direction=Direction.right;
			break;
		case left:
			d.direction=Direction.up;
			break;
		case right:
			d.direction=Direction.down;
			break;
		case up:
			d.direction=Direction.left;
			break;
		default:
			break;
		}
		d.moveForwards();
	}
}
