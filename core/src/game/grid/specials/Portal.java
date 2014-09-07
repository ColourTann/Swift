package game.grid.specials;

import game.assets.PortalSystem;
import game.grid.Tile;
import game.player.Disc;
import util.Colours;
import util.maths.Pair;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Portal extends Special {

	PortalSystem portal;
	Portal linked;
	Tile t;
	public Portal(Tile t, boolean blue){
		portal=new PortalSystem(blue?Colours.blues3[1]:Colours.oranges2[0]);
		this.t=t;
	}
	@Override
	public void render(SpriteBatch batch, Pair position) {
		portal.specialRender(batch, position.add(1,3));
	}

	public static void link(Portal a, Portal b){
		a.linked=b;
		b.linked=a;
	}
	@Override
	public void activate(Disc d) {
		d.teleportTo(linked.t);
		//d.moveForwards();
	}
	
}
