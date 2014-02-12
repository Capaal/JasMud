package processes;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.HashMap;

// Would this thing be better as an enumMap? Then I'm restricting down to the few allowedEquipSlots I want.
// Or rather, a double representation of one.
public class EquipMap<Key, Value> {
	
	private HashMap<Key, Value> keyToVal = new HashMap<Key, Value>();
	private HashMap<Value, Key> valToKey = new HashMap<Value, Key>();
	
	public EquipMap() {		
	}
	
	public EquipMap(Map<Key, Value> set) {	
		for (Key k : set.keySet()) {
			equip(k, set.get(k));
		}
	}
	
	public EquipMap(EquipMap<Key, Value> set) {	
		for (Key k : set.getKeyToValMap().keySet()) {
			equip(k, set.getKeyToValMap().get(k));
		}
	}
	
	// This allows gear switching, rather than unequiping then equiping.
	public void equip(Key k, Value v) {
		if (keyToVal.containsKey(k)) {
			keyToVal.put(k, v);
			valToKey.put(v, k);		
		}
	}
	
	public void forceEquip(Key k, Value v) {
		keyToVal.put(k, v);
		valToKey.put(v, k);			
	}
	
	public void unequipSlot(Key slot) {
		equip(slot, null);
	}
	
	public void unequipItem(Value item) {
		Key key = valToKey.get(item);
		equip(key, null);
	}
	
	public Value getValue(Key key) {
		return keyToVal.get(key);
	}
	
	public Key getKey(Value val) {
		return valToKey.get(val);
	}
	
	public Collection<Value> values() {
		Collection<Value> values = getKeyToValMap().values();
		if (values.isEmpty()) {
			return new ArrayList<Value>();
		}
		return values;
	}
	
	private HashMap<Key, Value> getKeyToValMap() {
		return keyToVal;
	}
}


