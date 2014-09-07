package game.player;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Main;
import game.assets.Gallery;
import game.grid.Tile;
import game.player.Disc.Direction;
import game.screen.GameScreen;
import util.Colours;
import util.Draw;
import util.image.Pic;
import util.maths.Pair;
import util.update.Timer;
import util.update.Timer.*;
import util.update.Mouser;
import util.update.Updater;
import util.update.Timer.Interp;

public class Disc extends Updater{
	public enum Direction{right, left, up, down};
	public Direction direction=Direction.right;
	public Direction oldDirection=null;
	boolean noReset;
	
	public Tile tile;
	private static float moveSpeed=2f;
	public Timer jumpTimer=new Timer();
	public int jump=0;
	int order;
	
	Pic p;
	boolean instant;
	public int value;
	public Player player;
	public Disc(Player player, int order, Tile start, Direction dir, Color col){
		this.player=player;
		this.order=order;
		p=new Pic(Gallery.discs[3-order], new Color[]{Colours.white,col});
		value=5-order;
		setTile(start);
		
		this.direction=dir;
	}
	
	public Disc(Disc starter, boolean teleport){
		
		p=starter.p;
		int xx=0;
		int yy=0;
		p=starter.p;
		//direction=starter.oldDirection;
		//if(direction==null)direction=starter.direction;
		direction=starter.oldDirection;
		if(direction==null)direction=starter.direction;
		switch (starter.direction){
		case down:
			yy=1;
			break;
		case left:
			xx=-1;
			break;
		case right:
			xx=1;
			break;
		case up:
			yy=-1;
			break;
		default:
			break;
		}
		position=starter.position;
		if(!teleport)slide(Tile.getPosition(starter.tile.x+xx, starter.tile.y+yy), moveSpeed, Interp.LINEAR);
		order=starter.order;
		jumpTimer=starter.jumpTimer;
		fadeOut(moveSpeed, Interp.LINEAR);
	}
	
	public Disc(boolean instant, Tile start){
		
		this.instant=instant;
		this.tile=start;
	}
	

	public void setTile(Tile t){
		tile=t;
		position=tile.getPosition();
	}
	
	public void start(){
		tile.activate(this);
	}
	@Override
	public void update(float delta) {
	
	}
	
	public Pic getAngle(){
		Direction d=oldDirection;
		if(d==null)d=direction;
		switch(d){
		case down:
			return Gallery.directionDown;
		case left:
			return Gallery.directionLeft;
		case right:
			return Gallery.directionRight;
		case up:
			return Gallery.directionUp;
		default:
			break;
		}
		return Gallery.directionRight;
	}
	
	public void render(SpriteBatch batch){
		
		if(alpha<=0)return;
		batch.setColor(Colours.withAlpha(Colours.white, (.7f-jumpTimer.getFloat()/2)*alpha));
		
		Draw.drawCentered(batch, p.getMask(Colours.make(133,139,161)), position.x, position.y+2);
		batch.setColor(Colours.withAlpha(Colours.white, alpha));
		Draw.drawCentered(batch, p.get(), position.x, position.y+2-jumpTimer.getFloat()*30);
		Draw.drawCentered(batch, getAngle().get(), position.x, position.y-1-jumpTimer.getFloat()*30+order);
		
	}
	public void moveForwards() {
		if(oldDirection!=null){
			direction=oldDirection;
			oldDirection=null;
		}
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
		int xTarget=tile.x+x;
		int yTarget=tile.y+y;
		Tile t=GameScreen.grid.tiles.get(xTarget+":"+yTarget);
		if(t==null){
			
			if(xTarget>GameScreen.grid.size)xTarget=-GameScreen.grid.size;
			if(xTarget<-GameScreen.grid.size)xTarget=GameScreen.grid.size;
			if(yTarget>GameScreen.grid.size)yTarget=-GameScreen.grid.size;
			if(yTarget<-GameScreen.grid.size)yTarget=GameScreen.grid.size;
			
			t=GameScreen.grid.tiles.get(xTarget+":"+yTarget);
			ghostTo(t);
			
			return;
		}
		moveTo(t);
	}
	public void ghostTo(Tile t) {
		if(!instant)GameScreen.grid.ghosts.add(new Disc(this, false));
		
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
		position=Tile.getPosition(t.x-x, t.y-y);
		moveTo(t);
		alpha=0;
		fadeIn(moveSpeed, Interp.LINEAR);
	}
	
	public void teleportTo(Tile t) {
		if(!instant)GameScreen.grid.ghosts.add(new Disc(this, true));
		tile=t;
		position=t.getPosition();
		if(instant)return;
		slide(tile.getPosition(), moveSpeed, Interp.LINEAR);
		
		alpha=0;
		fadeIn(moveSpeed, Interp.LINEAR);
		Timer timer=new Timer(0,1,moveSpeed,Interp.LINEAR);
		timer.addFinisher(new Finisher() {
			
			@Override
			public void finish() {
				moveForwards();
			}
		});
		
	}

	public void moveTo(final Tile tile) {
		jump--;
		this.tile=tile;
		if(instant){
			return;
		}
		slide(tile.getPosition(), moveSpeed, Interp.LINEAR);
		Timer timer=new Timer(0,1,moveSpeed,Interp.LINEAR);
		final Disc d=this;
		timer.addFinisher(new Finisher() {
			
			@Override
			public void finish() {
				if(jump>0){
					
					moveForwards();
					return;
				}
				tile.activate(d);
				if(noReset){
					noReset=false;
					return;
				}
				else if(oldDirection!=null){
					direction=oldDirection;
					oldDirection=null;
				}
				
			}
		});
		
			
	}

	public void jump() {
		jumpTimer=new Timer(0,1,moveSpeed/2.7f,Interp.SIN);
		jump=3;
		
	}

	public void setArrowDirection(Direction direction2) {
		if(oldDirection==null)oldDirection=direction;
		
		direction=direction2;
		noReset=true;
	}

	public Direction getDirection() {
		
		return direction;
	}

	

	
}
