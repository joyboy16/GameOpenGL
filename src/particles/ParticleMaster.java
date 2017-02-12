package particles;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.lwjgl.util.vector.Matrix4f;
import org.newdawn.slick.opengl.LoadableImageData;

import entities.Camera;
import renderEngine.Loader;

public class ParticleMaster {

	private static Map<ParticleTexture, List<Particle>> particle_list = new HashMap<ParticleTexture, List<Particle>>();
	private static ParticleRenderer particleRenderer;
	
	public static void init(Loader loader, Matrix4f projection){
		particleRenderer = new ParticleRenderer(loader, projection);
	}
	
	public static void update(Camera cam){
		Iterator<Entry<ParticleTexture,List<Particle>>> itr = particle_list.entrySet().iterator();
		while (itr.hasNext()) {
			Entry<ParticleTexture, List<Particle>> particle = itr.next();
			List<Particle> listP = particle.getValue();
			Iterator<Particle> iterator = listP.iterator();
			while (iterator.hasNext()) {
				Particle p = iterator.next();
				boolean notDead = p.updateParticle(cam);
				if(!notDead){
					iterator.remove();
					if(listP.isEmpty()){
						itr.remove();
					}
				}
					
			}
			if (!particle.getKey().useAdditiveBlending()) {
				InsertionSort.sortHighToLow(listP);
			}
		}
		
	}
	
	public static void renderParticles(Camera cam){
		particleRenderer.render(particle_list, cam);
	}
	
	public static void clean(){
		particleRenderer.cleanUp();
	}
	
	public static void addParticles(Particle p){
		List<Particle> list = particle_list.get(p.getTexture());
		if (list == null) {
			list = new ArrayList<Particle>();
			particle_list.put(p.getTexture(), list);
		}
		list.add(p);
	}
}
