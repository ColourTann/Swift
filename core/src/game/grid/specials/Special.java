package game.grid.specials;

import game.grid.Tile;
import game.player.Disc;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.sun.org.apache.bcel.internal.generic.INSTANCEOF;

import util.image.Pic;
import util.maths.Pair;

public abstract class Special {
	public Pic p;

	public abstract void render(SpriteBatch batch, Pair position);

	public abstract void activate(Disc d);

	public  boolean isFlag() {
		return this instanceof Flag;
	}
}
