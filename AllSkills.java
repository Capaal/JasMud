import java.io.*;

//Object for skills.

public class AllSkills implements Serializable {

	protected String skillName;
	protected String longDesc;
	protected String shortDesc;
	protected String syntax;
	protected int dmg;
	protected String type;  //Types are: Physical, magical.
	protected int skillLevel;  //Required skill level to use, 0 min
	protected int commandNum;
	protected int speed;
	
	public AllSkills(String skillName, String longDesc, String shortDesc, String syntax, 
			int dmg, String type, int skillLevel, int commandNum) {
		this.skillName = skillName;
		this.longDesc = longDesc;
		this.shortDesc = shortDesc;
		this.syntax = syntax;
		this.dmg = dmg;
		this.type = type;
		this.skillLevel = skillLevel;
		this.commandNum = commandNum;
		this.speed = 0;
	}
	
	public void setName(String name) {
		this.skillName = name;
	}
	
	public void setLongDesc(String desc) {
		this.longDesc = desc;
	}

}