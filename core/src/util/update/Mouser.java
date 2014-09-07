package util.update;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import game.Main;
import util.maths.Collider;
import util.maths.Pair;
import util.particleSystem.ParticleSystem;
import util.update.Timer.Interp;

public abstract class Mouser extends Updater{	
	public static ArrayList<Mouser> mousers = new ArrayList<Mouser>();
	
	public Collider collider;
	public boolean moused=false;
	public static Pair currentMoused;

	//Call this to add something to the mouse list//
	public void mousectivate(Collider collider){
		if(collider!=null)this.collider=collider;
		mousers.remove(this);
		mousers.add(this);
	}

	public void demousectivate(){
		mousers.remove(this);
		moused=false;
	}

	public static void updateMoused(){
		
		updateMousePosition();
		boolean found=false;
		for(int i=0;i<mousers.size();i++){

			Mouser mouseCheck= mousers.get(i);
			if(mouseCheck.dead){
				mousers.remove(i);
				i--;
				continue;
			}
			if(mouseCheck.layer!=getLayer()&&mouseCheck.layer!=Layer.ALL)continue;

			if(found){
				mouseCheck.deMouse();
				continue;
			}

			found=mouseCheck.checkMoused(currentMoused);

		}
	}	

	private static void updateMousePosition(){
		currentMoused=new Pair(
				Gdx.input.getX()/(float)Gdx.graphics.getWidth()*(float)Main.width,
				(Gdx.input.getY()/(float)Gdx.graphics.getHeight()*(float)Main.height));
	}

	public static Pair getMousePosition(){
		if(currentMoused==null)currentMoused=new Pair(
				Gdx.input.getX()/(float)Gdx.graphics.getWidth()*(float)Main.width,
				(Gdx.input.getY()/(float)Gdx.graphics.getHeight()*(float)Main.height));
		return currentMoused;
	}

	private boolean checkMoused(Pair s){
		if(collider.collidePoint(s)){
			if(!moused){
				moused=true;
				mouseDown();
			}
			return true;
		}
		if(moused){
			mouseUp();
			moused=false;
		}
		return false;
	}

	private void deMouse(){
		if(moused){
			mouseUp();
			moused=false;
		}
	}

	private boolean checkClicked(Pair s, boolean left){
		if(collider.collidePoint(s)){
			mouseClicked(left);
			return true;
		}
		return false;
	}

	public void moveToTop() {
		if(Mouser.mousers.remove(this))Mouser.mousers.add(0,this);
	}

	public abstract void mouseDown();
	public abstract void mouseUp();
	public abstract void mouseClicked(boolean left);

	public void debugRender(SpriteBatch batch){
		if(collider==null)return;
		batch.end();
		collider.debugDraw();
		batch.begin();
	}

	//Must call from InputListener as it deals with polled events//
	public static boolean updateClicked(boolean left){
		updateMousePosition();
		for(int i=0;i<mousers.size();i++){
			Mouser checkMoused=mousers.get(i);
			if(checkMoused.layer!=getLayer()&&checkMoused.layer!=Layer.ALL)continue;
			if(checkMoused.checkClicked(currentMoused, left))return true;
		}
		return false;
	}


	//debug render is very laggy and will mess up if you're using cameras, might fix if can be bothered//
	public static void debugRenderAll(SpriteBatch batch) {
		batch.end();
		for(Mouser b:mousers){
			b.collider.debugDraw();
		}
		batch.begin();
	}


	protected static void clearAllDefaultMousers() {
		for(int i=0;i<mousers.size();i++){
			if(mousers.get(i).layer==Layer.Default){
				mousers.remove(i);
				i--;
			}
		}
	}

}
