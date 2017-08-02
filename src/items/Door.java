package items;

// TODO door location, id, description, and so on. Is it a stdItem? no. Keys?
public class Door {
	
	private boolean isOpen = true;
	private boolean isLocked = false;
//	private ArrayList<Key> validKeys = new ArrayList<Key>();
	
	/**
	 * Alters isOpen to the given boolean state if door is not locked.
	 * @param doorState boolean desired state (true = open).
	 * @return boolean true if state is changed, false if locked.
	 */
	public boolean alterDoor(boolean doorState) {
		// Closed and locked
		if (isLocked && !isOpen) {
			return false; // Needs to be unlocked first
		} else { // Closed and unlocked, or open in any state.
			isOpen = doorState;
			return true;
		}
	}
	
	public boolean isOpen() {
		return isOpen;
	}
	
	public boolean isDoorLocked() { 
		return isLocked;
	}
	
	// returns true if state changed, else false
	// Does not care if locking locked door (or unlocking unlocked door) TODO
//	public boolean alterLock(boolean lockState, Key key) {
//		if (validKeys.contains(key)) {
//			isLocked = lockState;
//			return true;
//		}
//		return false;
//	}
	
	// Returns true if key successfully bonded to door, else false
	// TODO require gear and supplies to create a key, or a skill that does this?
//	public Key addKey(Key oldKey) {
//		if (validKeys.contains(oldKey)) {
//			Key newKey = Key.newKey(this);
//			validKeys.add(newKey);
//			return newKey;		
//		}
//		return null;
//	}
	
//	public boolean removeKey(Key key) {
//		if (this.equals(key.getDoor()) && validKeys.contains(key)) {
//			key.removeFromWorld();
//			validKeys.remove(key);
//			return true;
//		}
//		return false;
//	}
	
	//TODO methods to return keys bonded to it?
	// TODO method for picking lock/locking the lock without key OR this a skill on it's own?
	// TODO remove the lock from the door? install new lock? or is that just strange way of whiping all keys?
}
