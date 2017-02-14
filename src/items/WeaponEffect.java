package items;

import effects.Bleed;
import effects.PassiveCondition;
import effects.Regen;
import interfaces.Mobile;
import processes.Type;

public class WeaponEffect extends StdItem {

	public WeaponEffect(ItemBuilder build) {
		super(build);
		// TODO Auto-generated constructor stub
	}
	

	public enum EffectType {
		
		DEFENSE() {
			@Override public String applyEffect(Mobile currentPlayer) {
				if (currentPlayer.addPassiveCondition(PassiveCondition.DEFENCE, 10000)) {
					return "Defense added.";
				}
				return failedApply();
			}
		},

		BLEED() {
			@Override public String applyEffect(Mobile currentPlayer) {
				if (currentPlayer.addActiveCondition(new Bleed(currentPlayer), 10000, 5)) {
					return "Bleeding caused.";
				}
				return failedApply();
			}
		},	
		
		FASTBALANCE() {
			@Override public String applyEffect(Mobile currentPlayer) {
				return "";
			}
			
		};
	
		private EffectType() {}
		
		public String failedApply() {
			return "The effect doesn't seem to do anything.";
		}
		
		public String applyEffect(Mobile currentPlayer) {
			return "Wrong method, tells the coders they screwed up.";
		}
		
	}
	

}
