package game.grid;

import game.Main;
import game.assets.Gallery;
import game.assets.Sounds;
import game.grid.specials.Arrow;
import game.grid.specials.Portal;
import game.grid.specials.Flag;
import game.player.Disc;
import game.player.Disc.Direction;
import game.player.Player;
import game.screen.GameScreen;
import game.screen.Lobby;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import util.Colours;
import util.Draw;
import util.assets.Font;
import util.assets.MusicClip;
import util.maths.Pair;
import util.particleSystem.Particle;
import util.particleSystem.ParticleSystem;
import util.update.TextWisp;
import util.update.Timer;
import util.update.TextWisp.WispType;
import util.update.Timer.Finisher;
import util.update.Updater;
import util.update.Timer.Interp;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Grid extends Updater{
	public int rounds=1; 
	public int size;
	int minDist=9;
	int maxDist=120;
	public static ArrayList<Tile> visitedTiles= new ArrayList<Tile>();
	public HashMap<String, Tile> tiles= new HashMap<String, Tile>();

	Timer alpha=new Timer(0,1,1,Interp.LINEAR);

	public ArrayList<Disc> ghosts = new ArrayList<Disc>();

	public ArrayList<Player> orderedPlayers= new ArrayList<Player>();

	public ArrayList<Direction> directions = new ArrayList<Disc.Direction>();

	int order;
	int flag;
	public boolean allowInput=false;
	private boolean allPicked;
	private boolean timerAdded;
	private boolean won;
	public Timer fadeTimer=new Timer();

	public enum Gimmick{Mirror, Jump, Quake, Hardcore, BradyHaran};

	public Grid(int size){		
		this.size=size;
	}

	public void init(){
		Gimmick gimmick=null;
		if(rounds%3==0)gimmick=Gimmick.values()[(int) (Math.random()*(Gimmick.values().length))];

		rounds++;

		order=0;
		for(Player p:GameScreen.players){
			p.dir=null;
		}
		timerAdded=false;
		allowInput=false;
		won=false;
		Tile.init();
		minDist=9;
		Main.setNotSpinning();

		if(gimmick!=null){
			switch(gimmick){
			case Hardcore:
				minDist=20;
				break;
			case Jump:
				Tile.init(1,3,20,.4f);
				minDist=13;
				break;
			case Mirror:
				Tile.init(3,90,2,.4f);
				minDist=11;
				break;
			case Quake:
				Tile.shaking=true;
				break;
			case BradyHaran:
				Main.setSpinning();
				break;
			default:
				break;

			}
		}


		while(!binit()){

		}

		if(gimmick!=null){
			String s="";
			switch(gimmick){
			case Hardcore:
				s="Hardcore Mode";
				break;
			case Jump:
				s="Jump-pad sale";
				break;
			case Mirror:
				s="Hall of mirrors";
				break;
			case Quake:
				s="Earthquake!";
				break;
			case BradyHaran:
				s="Brady Style";
				break;
			default:
				break;

			}
			TextWisp.wisps.add(new TextWisp(s, Font.big, new Pair(0,60),WispType.Regular));

		}

		for(Tile t:tiles.values())t.finalise();
		allPicked=false;
		Timer t=new Timer(0,1,.19f,Interp.LINEAR);
		t.addFinisher(new Finisher() {

			@Override
			public void finish() {
				Sounds.tension.play();
				Sounds.tension.stopSongFading();

				allowInput=true;
				Timer t=new Timer(0,1,1/22f,Interp.LINEAR);
				t.addFinisher(new Finisher() {

					@Override
					public void finish() {
						allowInput=false;
						finishInput(false);
					}



				});

			}
		});


	}

	public boolean binit() {



		flag=0;
		Updater.clearAll();
		ParticleSystem.clearAll();
		activate();
		ghosts.clear();
		orderedPlayers.clear();
		directions.clear();
		directions.add(Direction.up);
		directions.add(Direction.down);
		directions.add(Direction.left);
		directions.add(Direction.right);

		Collections.shuffle(directions);

		while(GameScreen.players.size()<directions.size())directions.remove(0);

		for(int x=-size;x<=size;x++){
			for(int y=-size;y<=size;y++){
				tiles.put(x+":"+y, new Tile(x,y));
			}	
		}

		tiles.get("0:0").makeCenter();

		setupPortalPair(true);
		setupPortalPair(false);

		visitedTiles.clear();
		int tested=0;
		for(Player p:GameScreen.players){
			if(!test(directions.get(tested))){
				tiles.clear();
				return false;
			}
			tested++;
		}
		return true;




	}

	private boolean test(Direction dir) {





		Disc d= new Disc(true, tiles.get("0:0"));
		d.direction=dir;

		for(int i=0;i<minDist;i++){

			if(!visit(d, d.tile))return false;

		}

		for(int i=0;i<maxDist&&(d.tile.special!=null||d.jump>0||visitedTiles.contains(d.tile));i++){

			if(!visit(d, d.tile))return false;
		}

		if(d.tile.special!=null||visitedTiles.contains(d.tile)||d.jump>0)return false;

		makeFlag(d.tile);

		return true;

	}

	public boolean visit(Disc d, Tile t){

		if(d.jump<=0&&d.tile.special!=null&&d.tile.special.isFlag())return false;
		if(d.jump<=0){
			d.tile.pathed=true;
			visitedTiles.add(d.tile);
		}
		d.tile.activate(d);
		if(d.tile.special instanceof Portal&&d.jump<=0){
			if(d.jump<=0&&d.tile.special!=null&&d.tile.special.isFlag())return false;
			if(d.jump<=0){
				d.tile.pathed=true;
				visitedTiles.add(d.tile);
			}
			d.tile.activate(d);
			if(d.jump<=0&&d.tile.special!=null&&d.tile.special.isFlag())return false;
			if(d.jump<=0){
				d.tile.pathed=true;
				visitedTiles.add(d.tile);
			}
			d.moveForwards();
		}
		return true;
	}



	private void makeFlag(Tile t) {

		t.special=new Flag(GameScreen.players.get(flag));
		flag++;

	}

	public void setupPortalPair(boolean blue){
		Tile a=tiles.get((int) (-size+Math.random()*size*2)+":"+(int) (-size+Math.random()*size*2));
		while(a.special!=null){
			a=tiles.get((int) (-size+Math.random()*size*2)+":"+(int) (-size+Math.random()*size*2));
		}
		a.special=new Portal(a, blue);

		Tile b=tiles.get((int) (-size+Math.random()*size*2)+":"+(int) (-size+Math.random()*size*2));
		while(b.special!=null){
			b=tiles.get((int) (-size+Math.random()*size*2)+":"+(int) (-size+Math.random()*size*2));
		}
		b.special=new Portal(b,blue);

		Portal.link((Portal)a.special, (Portal)b.special);

	}

	public void render(SpriteBatch batch){
		for(int x=size;x>=-size;x--){
			for(int y=-size;y<=size;y++){

				Tile t=tiles.get(x+":"+y);

				t.render(batch);
			}
		}
		for(int x=size;x>=-size;x--){
			for(int y=-size;y<=size;y++){
				Tile t=tiles.get(x+":"+y);
				t.postRender(batch);
			}
		}
		for(Direction d:directions){
			switch(d){
			case down:
				Draw.drawCentered(batch, Gallery.centerTileDown.get(), 0, 0);
				break;
			case left:
				Draw.drawCentered(batch, Gallery.centerTileLeft.get(), 0, 0);
				break;
			case right:
				Draw.drawCentered(batch, Gallery.centerTileRight.get(), 0, 0);
				break;
			case up:
				Draw.drawCentered(batch, Gallery.centerTileUp.get(), 0, 0);
				break;
			default:
				break;
			}
		}

		for(Player p:GameScreen.players) p.renderDisc(batch);

		for(Disc d:ghosts)d.render(batch);

		batch.setColor(Colours.withAlpha(Player.winningColor, fadeTimer.getFloat()));
		Draw.drawScaled(batch, Gallery.whiteSquare.get(), -Main.width/2, -Main.height/2,Main.width,Main.height);

	}

	@Override
	public void update(float delta) {

		if(timerAdded||won)return;
		for(Player p:GameScreen.players){
			if(p.disc!=null)return;
		}
		if(allPicked&&orderedPlayers.size()==0){
			timerAdded=true;
			allPicked=false;
			ArrayList<Player> winningPlayers=new ArrayList<Player>();
			for(Player p:GameScreen.players){
				if(p.score>=Player.maxScore){
					winningPlayers.add(p);
				}
			}

			if(winningPlayers.size()>0){
				Player victor=winningPlayers.get(0);
				for(Player p:winningPlayers){
					if(p.score>victor.score)victor=p;
				}
				won=true;
				Player.winningColor=victor.col;

				fadeTimer=new Timer(0,1,.5f,Interp.LINEAR);
				System.out.println("adding victory timer");
				Timer t=new Timer(0,1,.2f,Interp.LINEAR);

				Main.fadeInSpeed=.1f;
				t.addFinisher(new Finisher() {

					@Override
					public void finish() {
						Main.changeScreen(new Lobby());
					}
				});


				return;

			}







			System.out.println("adding timer because need to make new round");
			Timer slide=new Timer(0,1,1,Interp.LINEAR);
			slide.addFinisher(new Finisher() {
				
				@Override
				public void finish() {
					for(Tile tile: tiles.values()){
						tile.slide(new Pair(tile.getPosition().x, 500), (float)Math.random()/4f+.1f, Interp.SQUARE);
					}
				}
			});




			Timer t=new Timer(0,1,.3f,Interp.LINEAR);
			t.addFinisher(new Finisher() {

				@Override
				public void finish() {
					Updater.clearAll();
					init();
				}
			});

		}

	}

	public void moveNextPlayer() {
		if(orderedPlayers.size()==0)return;
		Sounds.lowTone.play();
		orderedPlayers.remove(0).start(order);
		order++;
	}

	public void finishInput(boolean fast){
		allPicked=true;
		if(fast)MusicClip.currentMusic.fadeOut(2);
		for(int i=0;i<orderedPlayers.size();i++){
			Timer t=new Timer(0,1,1/(float)(i+2),Interp.LINEAR);
			t.addFinisher(new Finisher() {

				@Override
				public void finish() {
					moveNextPlayer();
				}
			});
		}
	}

}
