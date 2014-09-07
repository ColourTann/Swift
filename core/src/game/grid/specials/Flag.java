package game.grid.specials;

import game.assets.Gallery;
import game.player.Disc;
import game.player.Player;
import util.Colours;
import util.Draw;
import util.image.Pic;
import util.maths.Pair;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Flag extends Special{
	Player player;
	Pic pic;
	public Flag(Player p){
		this.player=p;
		pic=new Pic(Gallery.flag, new Color[]{Colours.white, player.col});
	}
	@Override
	public void render(SpriteBatch batch, Pair position) {
		
		Draw.drawCentered(batch, pic.get(), position.x+4, position.y-1);
	}

	@Override
	public void activate(Disc d) {
		player.addDisc(d);
	}

}
