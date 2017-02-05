package interfaces;

public interface Weapon {
	
	public double getDamageMult();
	public double getBalanceMult();
//	public int getMaxDurability();
// int getCurrentDurability();
	public void equip(Mobile player);
}
