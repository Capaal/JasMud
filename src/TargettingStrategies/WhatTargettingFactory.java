package TargettingStrategies;

public class WhatTargettingFactory {
	public WhatTargettingStrategy parse(String targetting) throws IllegalArgumentException {
		switch(targetting.toLowerCase()) {
		case "self":
			return new TargetSelfWhatStrategy();
		case "all":
			return new TargetAllWhatStrategy();
		case "others":
			return new TargetOthersWhatStrategy();
		case "target":
			return new TargetTargetWhatStrategy();
		default:
			throw new IllegalArgumentException("Not a valid WhatTargettingStrategy:" + targetting);
		}
	}
}
