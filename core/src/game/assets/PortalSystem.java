package game.assets;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import util.Draw;
import util.Draw.BlendType;
import util.maths.Pair;
import util.particleSystem.Particle;
import util.particleSystem.ParticleSystem;

public class PortalSystem extends ParticleSystem{
	float frequency=20;
	Color col;
	public PortalSystem(Color blues3){
		col=blues3;
		ParticleSystem.systems.add(this);
	}
	@Override
	public void update(float delta) {
		ticks+=delta*frequency;
		if(ticks>1){
			ticks--;
			particles.add(new PortalParticle(col));
		}
		for(Particle p:particles)p.update(delta);
	}


	public void specialRender(SpriteBatch batch, Pair position){
	//	Draw.setBlend(batch, BlendType.Additive);
		for(Particle p:particles){
			((PortalParticle)p).specialRender(batch,position);
		}
		//Draw.setBlend(batch, BlendType.Normal);
	}

	@Override
	public void render(SpriteBatch batch) {
	}

}
