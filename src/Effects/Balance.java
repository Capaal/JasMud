package effects;

import interfaces.Effect;

public class Balance implements Effect {	

	public Balance() {
	}
	@Override
	public boolean isInstanceOf(Effect otherEffect) {
		if (otherEffect.getClass() == Balance.class) {
			return true;
		}
		return false;
	}
}
