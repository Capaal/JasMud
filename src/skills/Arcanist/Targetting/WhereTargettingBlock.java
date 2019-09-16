package skills.Arcanist.Targetting;

import java.util.List;

import processes.Location;
import processes.Skills.Syntax;
import skills.Arcanist.ArcanistSkill;

public interface WhereTargettingBlock {
	
	public List<Location> findWhere(ArcanistSkill skill);

	public int determineCost();

	public StringBuilder describeOneself(StringBuilder sb);
	
	public enum WhereTargettingFactory {
		
		HERE() {
			@Override
			protected WhereTargettingBlock getImplementation() {
				return new WhereTargettingBlockHere();
			}
			@Override
			public void describeOneself(StringBuilder sb) {
				sb.append("Alter Where Here: Strikes in your current location. Cost: 0");
			}
		},
		
		PROJECTILE() {
			@Override
			protected WhereTargettingBlock getImplementation() {
				return new WhereTargettingBlockProjectile();
			}
			@Override
			public void describeOneself(StringBuilder sb) {
				sb.append("Alter Where Projectile: Strikes at every location in a straight line of indicated Direction. Cost: 15");
			}
		},
		
		ONEAWAY() {
			@Override
			protected WhereTargettingBlock getImplementation() {
				return new WhereTargettingBlockOneAway();
			}
			
			@Override
			public void describeOneself(StringBuilder sb) {
				sb.append("Alter Where Oneaway: Strikes at the location in Direction indicated. Cost: 10");
			}
		};
		
		private WhereTargettingFactory() {}
		
		protected abstract WhereTargettingBlock getImplementation();
		public abstract void describeOneself(StringBuilder sb);
		
		public static WhereTargettingBlock getWhere(String where) {
			for (WhereTargettingFactory w : WhereTargettingFactory.values()) {
				if (w.name().equalsIgnoreCase(where)) {
					return w.getImplementation();
				}
			}
			return null;
		}
	}

	public Syntax requestSyntax();	
}
