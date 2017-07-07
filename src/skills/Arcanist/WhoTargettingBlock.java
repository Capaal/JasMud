package skills.Arcanist;

import java.util.ArrayList;
import java.util.List;

import interfaces.Mobile;
import processes.Location;
import skills.Arcanist.WhereTargettingBlock.WhereTargettingFactory;

public interface WhoTargettingBlock {

	public List<Mobile> findWho(ArcanistSkill skill, List<Location> locations);

	public int determineCost();

	public StringBuilder describeOneself(StringBuilder sb);
	
	public enum WhoTargettingFactory {
		
		TARGET() {
			protected WhoTargettingBlock getImplementation() {
				return new WhoTargettingBlockTarget();
			}
			
		},
		
		ALLIES() {
			protected WhoTargettingBlock getImplementation() {
				return null;
			}
			
		},
		
		ENEMIES() {
			protected WhoTargettingBlock getImplementation() {
				return null;
			}
			
		};
		
		private WhoTargettingFactory() {}
		
		protected abstract WhoTargettingBlock getImplementation();
		
		public static WhoTargettingBlock getWho(String who) {
			for (WhoTargettingFactory w : WhoTargettingFactory.values()) {
				if (w.name().equalsIgnoreCase(who)) {
					return w.getImplementation();
				}
			}
			return null;
		}
	}

}
