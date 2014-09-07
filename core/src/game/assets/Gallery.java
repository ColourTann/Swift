package game.assets;


import util.image.Pic;
import util.image.SpriteSheet;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Pixmap.Format;
import com.badlogic.gdx.graphics.Texture;



public class Gallery {
	
	public static Pic tile = new Pic("tile");
	
	//Tile contents//
	public static Pic verticalMirror = new Pic("verticalmirror");
	public static Pic horizontalMirror = new Pic("horizontalmirror");
	public static Pic flag = new Pic("flag");
	
	
	
	
	public static Pic fuzzBall=new Pic("fuzz");
	
	//specials//
	public static Pic cross=new Pic("cross");
	public static Pic upRightArrow=new Pic("uprightarrow");
	public static Pic downRightArrow=new Pic("downrightarrow");
	public static Pic wing=new Pic("wing");
	//public static Pic centerTile=new Pic("centertile2");
	public static Pic centerTileUp=new Pic("centertileup");
	public static Pic centerTileLeft=new Pic("centertileleft");
	public static Pic centerTileRight=new Pic("centertileright");
	public static Pic centerTileDown=new Pic("centertiledown");
	
	
	
	
	public static Pic[] discs= new Pic[]{new Pic("disc0"),new Pic("disc1"),new Pic("disc2"), new Pic("disc3")};
	public static Pic directionUp=new Pic("directionup");
	public static Pic directionDown=new Pic("directiondown");
	public static Pic directionRight=new Pic("directionright");
	public static Pic directionLeft=new Pic("directionleft");
	
	//Lobby Stuff//
	public static Pic panel=new Pic("panel");
	
	//Runtime stuff//
	public static Pic whiteSquare;

	public static SpriteSheet conveyerUp=new SpriteSheet("upconveyer.png", 1, 4, 1);
	public static SpriteSheet conveyerDown=new SpriteSheet("downconveyer.png", 1, 4, 1);
	public static SpriteSheet conveyerLeft=new SpriteSheet("leftconveyer.png", 1, 4, 1);
	public static SpriteSheet conveyerRight=new SpriteSheet("rightconveyer.png", 1, 4, 1);
		
	public static void init(){
		
		Pixmap map = new Pixmap(1,1,Format.RGBA8888);
		map.setColor(1, 1, 1, 1);
		map.drawPixel(0, 0);
		whiteSquare=new Pic(new Texture(map));
		
	}
	
	
	
	
}

