package game.grid.specials;

import game.Main;
import game.assets.Gallery;
import game.grid.Tile;
import game.player.Disc;
import game.player.Disc.Direction;
import game.screen.GameScreen;
import util.Draw;
import util.image.Pic;
import util.maths.Pair;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Arrow extends Special {

	Direction direction;
	float rotation;
	Texture[] textures;
	public Arrow(Direction direction){
		this.direction=direction;
		switch(direction){
		case down:
			
			textures=Gallery.conveyerDown.get()[0];
			break;
		case left:
			textures=Gallery.conveyerLeft.get()[0];
			break;
		case right:
			textures=Gallery.conveyerRight.get()[0];
			break;
		case up:
			textures=Gallery.conveyerUp.get()[0];
			
			break;
		default:
			break;
		
		}
	}
	@Override
	public void render(SpriteBatch batch, Pair position) {
		Draw.drawRotatedCentered(batch, textures[(int)(Main.ticks*3)%textures.length], position.x, position.y+5, rotation);
	}
	@Override
	public void activate(Disc d) {
		int x=0;
		int y=0;
		switch (direction){
		case down:
			y=1;
			break;
		case left:
			x=-1;
			break;
		case right:
			x=1;
			break;
		case up:
			y=-1;
			break;
		default:
			break;
		}
		int xTarget=d.tile.x+x;
		int yTarget=d.tile.y+y;
		Tile t=GameScreen.grid.tiles.get(xTarget+":"+yTarget);
		if(t==null){
			
			if(xTarget>GameScreen.grid.size)xTarget=-GameScreen.grid.size;
			if(xTarget<-GameScreen.grid.size)xTarget=GameScreen.grid.size;
			if(yTarget>GameScreen.grid.size)yTarget=-GameScreen.grid.size;
			if(yTarget<-GameScreen.grid.size)yTarget=GameScreen.grid.size;
			
			t=GameScreen.grid.tiles.get(xTarget+":"+yTarget);
			d.setArrowDirection(direction);
			d.ghostTo(t);
			return;
		}
		
		d.setArrowDirection(direction);
		d.moveTo(t);
		
		
	}

}
