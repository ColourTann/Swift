package util;



import com.badlogic.gdx.graphics.Color;

public class Colours {
	// Event Horizon Colour palette
	public static Color white=new Color(1,1,1,1);
	public static Color black=new Color(0,0,0,1);
	public static Color faded = new Color(.4f, .4f, .4f, 1);
	
	public static Color light=new Color(make(222,238,214));
	public static Color grey=new Color(make(133,149,161));
	public static Color bg=new Color(make(48,52,109));
	
	//public static Color blues3[]=new Color[]{make(0,87,132),make(49,162,242),make(178,220,239)};
	public static Color blues3[]=new Color[]{make(48,52,109),make(89,125,206),make(109,194,202)};
	//public static Color[] oranges2=new Color[]{make(235,137,49), make(164,100,34)};
	public static Color[] oranges2=new Color[]{make(210,125,44), make(133,76,48)};
	
	public static Color transparent=new Color(0,0,0,0);
	

	public static Color[] playerCols=new Color[]{make(208,70,72),make(109,170,44),make(210,125,44),make(109,194,202),make(210,170,153), make(133,149,161)};
	
	public static Color shiftedTowards(Color source, Color target, float amount){
		if(amount>1) amount=1;
		if(amount<0) amount=0;
		System.out.println(amount);
		float r=source.r+((target.r-source.r)*amount);
		float g=source.g+(target.g-source.g)*amount;
		float b=source.b+(target.b-source.b)*amount;
		return new Color(r, g, b, 1);
	}
	public static Color multiply(Color source, Color target){
		return new Color(source.r*target.r,source.g*target.g,source.b*target.b,1);
	}
	public static Color withAlpha(Color source, float alphaMult){
		return new Color(source.r,source.g,source.b,source.a*alphaMult);
	}
	public static Color randomColor(){
		return new Color((float)Math.random(),(float)Math.random(),(float)Math.random(),1);
	}
	public static Color make(int r, int g, int b){
		return new Color((float)(r/255f),(float)(g/255f),(float)(b/255f),1);
	}
	public static Color monochrome(Color c){
		float brightness=(c.r+c.g+c.b)/3;
		return new Color(brightness,brightness,brightness,c.a);
	}
	public static boolean equals(Color a, Color b){
		return a.a==b.a&&a.r==b.r&&a.g==b.g&&a.b==b.b;
	}
	public static boolean wigglyEquals(Color a, Color aa){
		float r=Math.abs(a.r-aa.r);
		float g=Math.abs(a.g-aa.g);
		float b=Math.abs(a.b-aa.b);
		float wiggle=.00001f;
		return r<wiggle&&g<wiggle&&b<wiggle;
	}
}