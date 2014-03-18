package TargettingStrategies;

public class WhereTargettingFactory {
	
	public WhereTargettingStrategy parse(String targetting) throws IllegalArgumentException {
		switch(targetting.toLowerCase()) {
		case "here":
			return new TargetHereWhereStrategy();
		case "inventory":
			return new TargetInventoryWhereStrategy();
		case "oneawayprojectile":
			return new TargetOneAwayProjectileWhereStrategy();
		default:
			throw new IllegalArgumentException("Not a WhereTargettingStrategy: " + targetting);
		}
	}

}
