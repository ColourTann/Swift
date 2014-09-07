package game.assets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import util.Colours;
import util.Draw;
import util.maths.Pair;
import util.particleSystem.Particle;

public class PortalParticle extends Particle{
	float distance=0;
	float speed=2;
	float rotationSpeed=2+random(3);
	float randomX=(float)Math.random();
	float randomY=(float)Math.random();
	Color col;
	public PortalParticle(Color col){
		this.col=col;
		ticks=(float) (Math.random()*10f);
		maxLife=4;
		life=maxLife;

	}
	@Override
	public void update(float delta) {

		distance+=delta*speed;
		ticks+=delta;
		life-=delta;
		if(life<=0)dead=true;
		ratio=life/maxLife;
		position=new Pair(
				(float)(Math.sin(ticks*rotationSpeed)*distance),
				(float)(Math.cos(ticks*rotationSpeed)*distance)
				);
	}

	@Override
	public void render(SpriteBatch batch) {
	}
	public void specialRender(SpriteBatch batch, Pair position2) {
		if(dead)return;
		batch.setColor(Colours.withAlpha(col, ratio/2f));
		Draw.drawScaledCentered(batch, Gallery.cross.get(), position2.x+position.x, position2.y+position.y,1,1);
	}
}
