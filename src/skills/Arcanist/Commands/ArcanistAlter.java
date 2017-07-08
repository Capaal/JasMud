package skills.Arcanist.Commands;

import java.util.ArrayList;
import java.util.List;

import interfaces.Mobile;
import processes.SkillBook;
import processes.Skills;
import processes.UsefulCommands;
import skills.Arcanist.*;
import skills.Arcanist.Targetting.TargettingBlock;
import skills.Arcanist.Targetting.WhereTargettingBlock;
import skills.Arcanist.Targetting.WhoTargettingBlock;
import skills.Arcanist.Targetting.WhereTargettingBlock.WhereTargettingFactory;
import skills.Arcanist.Targetting.WhoTargettingBlock.WhoTargettingFactory;

public class ArcanistAlter extends Skills {
	
	private ArcanistSkillbook currentBook;
	private ArcanistBuilder currentSkill;
	private ArcanistComponentsFactory factory;
	
	public ArcanistAlter(Mobile currentPlayer, String fullCommand) {
		super("alter", "Altering the spell you are scribing: Alter [component] [desired]", currentPlayer, fullCommand);
		syntaxList.add(Syntax.SKILL);
		syntaxList.add(Syntax.TARGET);
		syntaxList.add(Syntax.ITEM);
	
	}

	@Override
	protected void performSkill() {
		if (preSkillChecks()) {
			currentSkill = factory.getBlock(currentSkill, Syntax.ITEM.getStringInfo(fullCommand, this));
			if (currentSkill == null) {
				messageSelf("Alteration unsuccessful, the component details were invalid.");				
			} else {
				messageSelf("Alteration successful.");
				if (currentSkill.isValid()) {
					messageSelf("COMPLETE to finalize this spell.");
				} else {
					messageSelf("Further alteration necessary before spell completion.");
				}
			}
		}
	}

	@Override
	protected boolean preSkillChecks() {
		currentBook = getCurrentBook();
		if (currentBook == null) {
			messageSelf("But you have no book for which to scribe!");
			System.out.println("Serious bug, player missing ArcanistSkillbook but used ALTER.");
			return false;
		}
		currentSkill = currentBook.getCurrentSkillBuilder();
		if (currentSkill == null) {
			messageSelf("You have not yet began to CREATE a new spell.");
			return false;
		}
		String componentName = Syntax.TARGET.getStringInfo(fullCommand, this);
		String desiredName = Syntax.ITEM.getStringInfo(fullCommand, this);
		if (componentName.equals("") || desiredName.equals("")) {
			messageSelf("To continue scribing: alter [component] [desired]");
			return false;
		}				
		factory = ArcanistComponentsFactory.getComponent(componentName);
		if (factory == null) {
			messageSelf("Your component to alter was invalid.");
			return false;
		}			
		return true;
	}

	@Override
	public Skills getNewInstance(Mobile currentPlayer, String fullCommand) {
		return new ArcanistAlter(currentPlayer, fullCommand);
	}
	
	private ArcanistSkillbook getCurrentBook() {
		for (SkillBook s : currentPlayer.viewSkillBooks().keySet()) {
			if (s instanceof ArcanistSkillbook) {
				return (ArcanistSkillbook)s;
			}
		}
		return null;
	}
	
	public enum ArcanistComponentsFactory {
		
		DAMAGE() {
			@Override
			public  ArcanistBuilder getBlock(ArcanistBuilder build, String details) {
				if (UsefulCommands.isInteger(details)) {				
					int desiredDamage = Integer.parseInt(details);
					if (desiredDamage >= 0 && desiredDamage <= 100) { // May not be negative nor more than 100% hitpoints.
						build.setDamage(new DamageBlock(desiredDamage, build.getDamageBlock().getAddedEffects()));
						return build;
					}
				}
				return null;
			}
			@Override
			public String describeYourself() {
				return "Alter Damage [Intensity]: Percent damage dealt. Cost: 2 x intensity";
			}
			
		},
		
		HEAL() {
			@Override
			public  ArcanistBuilder getBlock(ArcanistBuilder build, String details) {
				if (UsefulCommands.isInteger(details)) {				
					int desiredHealing = Integer.parseInt(details);
					if (desiredHealing >= 0 && desiredHealing <= 100) { // May not be negative nor more than 100% hitpoints.
						build.setDamage(new DamageBlockHeal(desiredHealing, build.getDamageBlock().getAddedEffects()));
						return build;
					}
				}
				return null;
			}
			@Override
			public String describeYourself() {
				return "Alter Heal [intensity]: Heals equal to intensity. Cost: 1.5 x Intensity.";
			}
		},
		
		MANA() {
			@Override
			public  ArcanistBuilder getBlock(ArcanistBuilder build, String details) {
				if (UsefulCommands.isInteger(details)) {				
					int manaCost = Integer.parseInt(details);
					if (manaCost >= 0 && manaCost <= 100) { // May not be negative nor more than 100 mana.
						build.setMana(manaCost);
						return build;
					}
				}
				return null;
			}
			@Override
			public String describeYourself() {
				return "Alter Mana [Required Mana]: Sets the amount of mana to cast this spell. Cost: mana.";
			}
		},
		
		SPEED() {
			@Override
			public  ArcanistBuilder getBlock(ArcanistBuilder build, String details) {
				try {				
					double desiredDamage = Double.parseDouble(details);
					if (desiredDamage >= 0 && desiredDamage <= 60) {
						build.setSpeed(new SpeedBlock(desiredDamage));
						return build;
					}
				} catch (NumberFormatException e) {
					return null;
				}
				return null;
			}
			@Override
			public String describeYourself() {
				return "Alter Speed [Time in Seconds]: Sets resulting balance duration in seconds. Cost: +/- 20/second.";
			}
		},
		
		WHO() {
			@Override
			public  ArcanistBuilder getBlock(ArcanistBuilder build, String details) {
				WhoTargettingBlock whoBlock = WhoTargettingFactory.getWho(details);
				if (whoBlock != null) {
					build.setTargettingBlock(new TargettingBlock(whoBlock, build.getTargettingBlock().getWhere()));
					build.setSyntax(build.getTargettingBlock().getSyntax());
					return build;
				}
				return null;
			}	
			@Override
			public String describeYourself() {
				StringBuilder sb = new StringBuilder();
				for (WhoTargettingFactory b : WhoTargettingFactory.values()) {
					b.describeOneself(sb);
					sb.append(System.lineSeparator());
				}
				return sb.toString();
			}
		},
		
		WHERE() {
			@Override
			public  ArcanistBuilder getBlock(ArcanistBuilder build, String details) {
				WhereTargettingBlock whereBlock = WhereTargettingFactory.getWhere(details);
				if (whereBlock != null) {
					build.setTargettingBlock(new TargettingBlock(build.getTargettingBlock().getWho(), whereBlock));
					build.setSyntax(build.getTargettingBlock().getSyntax());
					return build;
				}
				return null;
			}	
			
			@Override
			public String describeYourself() {
				StringBuilder sb = new StringBuilder();
				for (WhereTargettingFactory b : WhereTargettingFactory.values()) {
					b.describeOneself(sb);
					sb.append(System.lineSeparator());
				}
				return sb.toString();
			}
		},
		
		BLEED() {
			@Override
			public  ArcanistBuilder getBlock(ArcanistBuilder build, String details) {
				if (UsefulCommands.isInteger(details)) {				
					int intensity = Integer.parseInt(details);
					DamageBlock currentDamageBlock = build.getDamageBlock();
					List<ArcanistBlock> effects = currentDamageBlock.getAddedEffects();
					if (effects == null) {
						effects = new ArrayList<ArcanistBlock>();
					}
					for (ArcanistBlock b : effects) {
						if (b instanceof BleedBlock) {
							effects.remove(b);
						}
					}
					effects.add(new BleedBlock(intensity));
					build.setDamage(currentDamageBlock.getNewInstance(currentDamageBlock.getDamage(), effects));
					// The Below assumes DamageBlock, what about Heal?
				//	build.setDamage(new DamageBlock(build.getDamageBlock().getDamage(), effects, build.getDamageBlock().getHeal()));
					return build;
				}				
				return null;
			}
			
			public String describeYourself() {
				return "Bleed [Starting Intensity]: Intensity decays every tick. Cost: 4 x intensity";
			//	return BleedBlock.describeYoursedl(); //?? I dunno, maybe this is smart? Or just do it here? it would be static
			}
			
		}; //TODO NAME, DESCRPTION, LOTS OF EFFECTS, INDUCTION, COOLDOWN BLAH BLAH
		
		private ArcanistComponentsFactory() {}	
		
		public abstract ArcanistBuilder getBlock(ArcanistBuilder build, String details);
		public abstract String describeYourself();
		
		public static ArcanistComponentsFactory getComponent(String componentName) {
			for (ArcanistComponentsFactory e : ArcanistComponentsFactory.values()) {
				if (e.name().equalsIgnoreCase(componentName)) {
					return e;
				}
			}
			return null;
			 
		}
	}
}
