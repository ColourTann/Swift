package game.player;

import java.util.ArrayList;

import util.Colours;
import util.Draw;
import util.assets.Clip;
import util.assets.Font;
import util.maths.Pair;
import util.update.Updater;
import util.update.Timer.Interp;

import com.badlogic.gdx.Input;
import com.badlogic.gdx.controllers.Controller;
import com.badlogic.gdx.controllers.ControllerListener;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputListener;

import game.Main;
import game.assets.Gallery;
import game.assets.Sounds;
import game.grid.Grid;
import game.player.Disc.Direction;
import game.screen.GameScreen;
import game.screen.Lobby;

public class Player {


	public static Player p1=new Player(1);
	public static Player p2=new Player(2);
	public static Player p3=new Player(3);
	public static Player p4=new Player(4);
	public static ArrayList<Player> players = new ArrayList<Player>();
	public boolean active;
	int number;
	public Disc disc;
	public boolean started;
	public Direction dir;

	public ArrayList<Disc> discs= new ArrayList<Disc>();
	public static int maxScore=35;
	//public static int maxScore=1;
	public Color col=Colours.randomColor();
	Pair position;
	Pair start;
	public int score=0;

	public static Color winningColor=Colours.black;

	public int colNum=0;
	public static boolean xBox;
	public Controller controller;

	public Player(int number){
		this.number=number;
		position=new Pair((number-1)%2*Main.width/2,(number-1)/2*Main.height/2);
		start=position.add((number-1)%2==0?-180:-20,-Main.height/6);
		System.out.println(position);
	}

	public static void init(){
		players.add(p1);
		players.add(p2);
		players.add(p3);
		players.add(p4);
	}

	public Player(ControllerListener cl){

	}

	public void pressButton(Direction dir){
		if(GameScreen.isChoosing()){
			if(!active)return;
			pickDirection(dir);
			return;
		}

		switch (dir){
		case down:
			if(Main.inLobby&&!xBox)deactivate();
			if(Main.inLobby&&xBox)activate(xBox);
			break;
		case left:
			switchColor(-1,true);
			break;
		case right:
			switchColor(+1,true);
			break;
		case up:
			if(Main.inLobby&&!active&&!xBox){
				activate(xBox);
				return;
			}
			if(Main.inLobby&&active&&xBox) deactivate();

			break;
		default:
			break;
		}
	}

	private void switchColor(int i, boolean sound) {
		if(!active)return;
		if(sound){
		if(i==1)Sounds.up.play();
		else Sounds.down.play();
		}
		colNum+=i;
		if(colNum<0)colNum=Colours.playerCols.length-1;
		colNum=colNum%Colours.playerCols.length;
		boolean changed=true;
		while(changed){
			changed=false;
			for(Player p:players){
				if(p!=this&&p.colNum==colNum){
					colNum+=i;
					if(colNum<0)colNum=Colours.playerCols.length-1;
					
					changed=true;
				}
			}
		}
		colNum=colNum%Colours.playerCols.length;
		col=Colours.playerCols[colNum];
	}

	private void pickDirection(Direction direction) {

		if(dir==null&&GameScreen.grid.allowInput){


			GameScreen.playerChosen(this, direction);
		}


	}

	private void deactivate() {
		if(active) Sounds.deactivate.play();
		
		active=false;
	}

	public String getKey(Direction dir){
		
		if(xBox){
			switch (dir){
			case down:
				return "A";
			case left:
				return "B";
			case right:
				return "X";
			case up:
				return "Y";
			default:
				break;
			
			}
		}
		
		switch(number){
		case 1:
			switch (dir){
			case down:
				return "S";
			case left:
				return "A";
			case right:
				return "D";
			case up:
				return "W";
			default:
				break;
			}
		case 2:
			switch(dir){
			case down:
				return "G";
			case left:
				return "F";
			case right:
				return "H";
			case up:
				return "T";
			default:
				break;
			}
		case 3:
			switch(dir){
			case down:
				return "K";
			case left:
				return "J";
			case right:
				return "L";
			case up:
				return "I";
			default:
				break;
			}
		case 4:
			switch(dir){
			case down:
				return "Down";
			case left:
				return "Left";
			case right:
				return "Right";
			case up:
				return "Up";
			default:
				break;
			}
		}
		return "bbbb";
	}

	private void activate(boolean xBox) {
		if(!active)Sounds.activate.play();
		active=true;
		switchColor(1, false);
	}

	public void renderLobby(SpriteBatch batch){

		batch.setColor(Colours.light);


		if(!active){
			Font.drawFontCentered(batch, "Press "+(xBox?"A":getKey(Direction.up))+" to join", Font.medium, position.x+Main.width/4, position.y+Main.height/4);
		}
		else {

			batch.setColor(col);
			Draw.drawScaled(batch, Gallery.whiteSquare.get(), position.x>100?position.x+Lobby.border/2:position.x, position.y>100?position.y+Lobby.border/2:position.y, Main.width/2-Lobby.border/2, Main.height/2-Lobby.border/2);

			float gap = 20;
			Font.drawFontCentered(batch, getKey(Direction.up)+": up", Font.medium, position.x+Main.width/4, position.y-gap+Main.height/4);
			Font.drawFontCentered(batch, getKey(Direction.left)+": left", Font.medium, position.x+Main.width/4, position.y+gap+Main.height/4);
			Font.drawFontCentered(batch, getKey(Direction.down)+": down", Font.medium, position.x+Main.width/4, position.y+Main.height/4);
			Font.drawFontCentered(batch, getKey(Direction.right)+": right", Font.medium, position.x+Main.width/4, position.y+gap*2+Main.height/4);
		}
	}



	public void renderDisc(SpriteBatch batch) {
		if(disc!=null)disc.render(batch);
		for(Disc d:discs)d.render(batch);
	}

	public void start(int order) {
		started=true;
		if(disc==null){
			disc=new Disc(this, order, GameScreen.grid.tiles.get("0:0"), dir, col);
		}
		disc.start();
	}

	public void renderUI(SpriteBatch batch) {
		//if(!started)return;
		batch.setColor(col);

		Draw.drawScaled(batch, Gallery.whiteSquare.get(), position.x-Main.width/2, position.y-Main.height/2, Main.width/2, Main.height/2);
		if(dir!=null){
			Font.big.setColor(Colours.light);
			Font.drawFontCentered(batch, "OK!", Font.big, number%2==0?80:-80, number<3?-100:100);//number>2?10:Main.height-10);
		}
		batch.setColor(Colours.light);

		Draw.drawScaled(batch, Gallery.whiteSquare.get(), start.x-10, start.y-maxScore-2, 1, maxScore+11);
		Draw.drawScaled(batch, Gallery.whiteSquare.get(), start.x-12, start.y-maxScore-2, 5, 1);
		Draw.drawScaled(batch, Gallery.whiteSquare.get(), start.x-12, start.y+9, 5, 1);
	}

	public void resetDisc(){
		disc=null;
		started=false;
		dir=null;
	}

	public void addDisc(Disc d){
		Clip c=d.player==this?Sounds.good:Sounds.bad;
		c.play();

		d.player.disc=null;
		discs.add(d);

		//d.position=d.position.add(new Pair(Main.width/2, Main.height/2));
		d.slide(start.add(0,-score), 1f, Interp.CUBE);
		score+=d.value;
	}

	public void reset(){
		//	active=false;

		disc=null;
		started=false;
		dir=null;

		discs.clear();
		col=Colours.randomColor();

		score=0;
		winningColor=Colours.black;
		GameScreen.choosing=false;
		colNum=0;
	}
	
	public static void xBoxPress(Controller c, Direction dir){
		xBox=true;
		
		for(Player p:players){
			if(p.controller==c){
				p.pressButton(dir);
				return;
			}
		}
		
		System.out.println("adding new controller");
		if(dir!=Direction.down)return;
		for(Player p:players){
			if(p.controller==null){
				p.controller=c;
				p.pressButton(dir);
				return;
			}
		}
		
		
	}

}
