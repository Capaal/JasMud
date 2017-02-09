package interfaces;

public interface Effect  {
	
	public boolean isInstanceOf(Effect otherEffect);
	
	public void doOnCreation();
	
	public void doOnDestruction();
	
}
