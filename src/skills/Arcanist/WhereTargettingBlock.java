package skills.Arcanist;

import java.util.List;

import processes.Location;

public interface WhereTargettingBlock {
	
	public List<Location> findWhere(ArcanistSkill skill);

	public int determineCost();

	public StringBuilder describeOneself(StringBuilder sb);
	
	public enum WhereTargettingFactory {
		
		HERE() {
			protected WhereTargettingBlock getImplementation() {
				return new WhereTargettingBlockHere();
			}
			
		},
		
		PROJECTILE() {
			protected WhereTargettingBlock getImplementation() {
				return null;
			}
			
		},
		
		ONEAWAY() {
			protected WhereTargettingBlock getImplementation() {
				return null;
			}
			
		};
		
		private WhereTargettingFactory() {}
		
		protected abstract WhereTargettingBlock getImplementation();
		
		public static WhereTargettingBlock getWhere(String where) {
			for (WhereTargettingFactory w : WhereTargettingFactory.values()) {
				if (w.name().equalsIgnoreCase(where)) {
					return w.getImplementation();
				}
			}
			return null;
		}
	}

	
}
