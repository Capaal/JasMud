package TargettingStrategies;

/* Must be updated for any new WhatStrategies
 * 
 */
public class WhatFactory {
	public WhatStrategyInterface parse(String targetting) throws IllegalArgumentException {
		switch(targetting.toLowerCase()) {
		case "SELF":
			return new WhatStrategySelf();
		case "ALL":
			return new TargetAllWhatStrategy();
		case "OTHERMOBILES":
			return new WhatStrategyOtherMobiles();
		case "TARGET":
			return new WhatStrategyTarget();
		case "ALLTARGETS":
			return new WhatStrategyAllTargets();
		default:
			throw new IllegalArgumentException("Not a valid WhatTargettingStrategy:" + targetting);
		}
	}
}
