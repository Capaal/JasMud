package skills.Arcanist;

public interface ArcanistBlock {
	
	public void perform(ArcanistSkill skill);
	public int determineCost();
	public StringBuilder describeOneself(StringBuilder sb);
	public boolean isValid();

}
