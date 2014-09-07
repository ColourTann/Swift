package game.grid;

import java.util.ArrayList;
import java.util.Collections;

import game.Main;
import game.assets.Gallery;
import game.grid.specials.Arrow;
import game.grid.specials.Center;
import game.grid.specials.Flag;
import game.grid.specials.HorizontalMirror;
import game.grid.specials.Jump;
import game.grid.specials.VerticalMirror;
import game.grid.specials.Special;
import game.player.Disc;
import game.player.Disc.Direction;
import util.Colours;
import util.Draw;
import util.assets.Font;
import util.maths.Pair;
import util.particleSystem.Particle;
import util.update.Timer;
import util.update.Timer.*;
import util.update.Updater;
import util.update.Timer.Interp;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Tile extends Updater{
	
	
	static int width=17;
	static int height=11;
	int bonusY=(int)Particle.random(0);
	public int x,y;
	
	public Special special;
	public boolean pathed;
	private float random=(float) (Math.random()*100);
	static float freq=.75f;
	public enum SpecialType{Arrow, Mirror,Jump, Portal}
	private static ArrayList<SpecialType> specials= new ArrayList<SpecialType>();
	public static boolean shaking;
	
	public static void init(){
		int arrowFreq=2;
		int mirrorFreq=7;
		int jumpFreq=3;
		
		for(int i=0;i<arrowFreq;i++)specials.add(SpecialType.Arrow);
		for(int i=0;i<mirrorFreq;i++)specials.add(SpecialType.Mirror);
		for(int i=0;i<jumpFreq;i++)specials.add(SpecialType.Jump);
		freq=.75f;
		shaking=false;
	}
	
	public static void init(int arrow, int mirror, int jump, float baseFreq){
		int arrowFreq=arrow;
		int mirrorFreq=mirror;
		int jumpFreq=jump;
		freq=baseFreq;
		for(int i=0;i<arrowFreq;i++)specials.add(SpecialType.Arrow);
		for(int i=0;i<mirrorFreq;i++)specials.add(SpecialType.Mirror);
		for(int i=0;i<jumpFreq;i++)specials.add(SpecialType.Jump);
		Collections.shuffle(specials);
	}
	
	public Tile(int x, int y){
	
		this.x=x;
		this.y=y;
		position=new Pair(getPosition().x, -500);
		
		
	
		
		if(Math.random()>freq){
			
			SpecialType type=specials.get((int) (Math.random()*specials.size()));
			switch (type){
			case Arrow:
				special=new Arrow(Direction.values()[(int) (Math.random()*4)]);
				break;
			case Jump:
				special=new Jump();
				break;
			case Mirror:
				special=Math.random()>.5?new VerticalMirror():new HorizontalMirror();
				break;
			default:
				break;
			
			}
			
		}
	
		
	}
	
	public void finalise(){
		if(special instanceof Flag)slide(getPosition(), (.4f+(float)Math.random()/4f), Interp.SANTIBOUNCE);
		else{
			Timer time=new Timer(0,1,.3f,Interp.LINEAR);
			time.addFinisher(new Finisher() {
				
				@Override
				public void finish() {
					slide(getPosition(), (.3f+(float)Math.random()/2f), Interp.SANTIBOUNCE);
				}
			});
			
		}
		
	}
	
	public void makeCenter() {
		special=new Center();
		
	}

	public Pair getPosition(){
		return new Pair(x*width+y*width, -x*height+y*height+bonusY-5);
	}
	
	public static Pair getPosition(int x, int y){
		return new Pair(x*width+y*width, -x*height+y*height-5);
	}
	
	public void render(SpriteBatch batch){
		position=position.ceil();
		
		batch.setColor(Colours.white);
		///if(pathed)batch.setColor(Colours.grey);
		Pair renderPos=position.copy();
		if(shaking){
			float freq=10;
			float amp=3;
			renderPos=renderPos.add(0, (float)Math.sin(random+Main.ticks*freq)*amp);
		}
		
		Draw.draw(batch, Gallery.tile.get(), renderPos.x-width, renderPos.y-height);
		
		if(special!=null)special.render(batch, renderPos);
	
		
		
	}
	
	public void postRender(SpriteBatch batch){
	//	Font.drawFontCentered(batch, x+":"+y, Font.small, position.x, position.y);
		batch.setColor(1,1,1,1);

		
	}
	@Override
	public void update(float delta) {
		
		
	}
	
	public void activate(Disc d){
		if(special==null||d.jump>0){
			d.moveForwards();
			return;
		}
		special.activate(d);
	}
}
