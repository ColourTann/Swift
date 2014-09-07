package util.assets;

import game.Main;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;

import util.Option;
import util.update.Mouser;
import util.update.Updater;

public class MusicClip extends Updater{
	public static Option musicLevel=new Option(.7f);
	

	float fadeSpeed=0;
	float volumeMultiplier=1;
	String path;
	Music music;
	public static MusicClip currentMusic;
	public MusicClip(String path){
		layer=Layer.ALL;
		deactivate();
		this.path=path;
	}
	public Music get(){
		if(music==null)music=Gdx.audio.newMusic(Gdx.files.internal(path));
		
		
		return music;
	}
	public void play(){
		get().stop();
		
		currentMusic=this;
		get().setVolume(getVolume());
		music.setLooping(false);
		get().play();
	}
	
	public void playSeparate(){
		get().setVolume(getVolume());
		music.setLooping(true);
		get().play();
	}
	
	public void mute(){
		volumeMultiplier=0;
		update(0);
		get().setVolume(getVolume());
	}
	
	public float getVolume(){
		//if(Main.debug)return 0;
		return musicLevel.getFloat()*volumeMultiplier;
	}
	public void fadeOut(float speed){
		this.fadeSpeed=-speed;
		activate();
	}
	public void fadeIn(float speed){
		volumeMultiplier=0;
		play();
		this.fadeSpeed=speed;
		activate();
	}
	public void crossFadeTo(MusicClip clip, float speed){
		fadeOut(speed*2);
		clip.fadeIn(speed);
	}
	
	private void reVolume() {
		get().setVolume(getVolume());
	}
	
	public static void refresh(){
		
		if(currentMusic==null)return;
		currentMusic.reVolume();
	}
	
	
	@Override
	public void update(float delta) {
		get();
		if(fadeSpeed==0)return;
		volumeMultiplier+=fadeSpeed*delta;
		if(volumeMultiplier<=0){
			volumeMultiplier=0;
		}
		if(volumeMultiplier>=1){
			volumeMultiplier=1;
		}
		get().setVolume(getVolume());
	}
	
	
	public void setVolume(int i) {
		volumeMultiplier=1;
	}
	public void stopSongFading() {
		this.fadeSpeed=0;
		get().setVolume(1);
	}
	public void load() {
		get();
	}
}
