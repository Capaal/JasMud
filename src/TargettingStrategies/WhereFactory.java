package TargettingStrategies;

public class WhereFactory {
	
	public WhereStrategyInterface parse(String targetting) throws IllegalArgumentException {
		switch(targetting.toLowerCase()) {
		case "HERE":
			return new WhereStrategyHere();
		case "SELFINVENTORY":
			return new WhereStrategySelfInventory();
		case "ONEAWAY":
			return new WhereStrategyOneAway();
		default:
			throw new IllegalArgumentException("Not a WhereStrategy: " + targetting);
		}
	}

}
