package skills.Arcanist.Targetting;

import java.util.List;

import interfaces.Mobile;
import processes.Location;
import processes.Skills.Syntax;
import skills.Arcanist.ArcanistSkill;

public interface WhoTargettingBlock {

	public List<Mobile> findWho(ArcanistSkill skill, List<Location> locations);
	public int determineCost();
	public Syntax requestSyntax();
	public StringBuilder describeOneself(StringBuilder sb);
	
	public enum WhoTargettingFactory {		
		TARGET() {
			@Override
			protected WhoTargettingBlock getImplementation() {
				return new WhoTargettingBlockTarget();
			}
			@Override
			public void describeOneself(StringBuilder sb) {
				sb.append("Alter Who Target: Strikes your target. Cost: 5.");
			}
			
		},		
		
		ALL() {
			@Override
			protected WhoTargettingBlock getImplementation() {
				return new WhoTargettingBlockAll();
			}
			@Override
			public void describeOneself(StringBuilder sb) {
				sb.append("Alter Who All: Strikes everyone. Cost: 20.");
			}
		},
		
		SELF() {
			@Override
			protected WhoTargettingBlock getImplementation() {
				return new WhoTargettingBlockSelf();
			}
			@Override
			public void describeOneself(StringBuilder sb) {
				sb.append("Alter Who Self: Strikes only yourself. Cost: -5.");
			}
		},
		
		// NOT IMPLETMENTED
		ALLIES() {
			@Override
			protected WhoTargettingBlock getImplementation() {
				return null;
			}
			@Override
			public void describeOneself(StringBuilder sb) {
				sb.append("Alter Who Allies: Strikes only your allies. Cost: Not Implemented.");
			}
		},
		
		// NOT IMPLETMENTED
		ENEMIES() {
			@Override
			protected WhoTargettingBlock getImplementation() {
				return null;
			}
			
			@Override
			public void describeOneself(StringBuilder sb) {
				sb.append("Alter Who Enemies: Strikes only your enemies. Cost: Not Implemented.");
			}
		};
		
		private WhoTargettingFactory() {}
		
		protected abstract WhoTargettingBlock getImplementation();
		public abstract void describeOneself(StringBuilder sb);
		
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
