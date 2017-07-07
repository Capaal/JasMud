package skills.Arcanist.Commands;

import interfaces.Mobile;
import processes.SkillBook;
import processes.Skills;
import processes.UsefulCommands;
import skills.Arcanist.*;
import skills.Arcanist.WhereTargettingBlock.WhereTargettingFactory;
import skills.Arcanist.WhoTargettingBlock.WhoTargettingFactory;

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
	
	private enum ArcanistComponentsFactory {
		
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
			
		},
		
		WHO() {
			@Override
			public  ArcanistBuilder getBlock(ArcanistBuilder build, String details) {
				WhoTargettingBlock whoBlock = WhoTargettingFactory.getWho(details);
				if (whoBlock != null) {
					build.setTargettingBlock(new TargettingBlock(whoBlock, build.getTargettingBlock().getWhere()));
					return build;
				}
				return null;
			}			
		},
		
		WHERE() {
			@Override
			public  ArcanistBuilder getBlock(ArcanistBuilder build, String details) {
				WhereTargettingBlock whereBlock = WhereTargettingFactory.getWhere(details);
				if (whereBlock != null) {
					build.setTargettingBlock(new TargettingBlock(build.getTargettingBlock().getWho(), whereBlock));
					return build;
				}
				return null;
			}			
		},
		
		EFFECT() {
			@Override
			public  ArcanistBuilder getBlock(ArcanistBuilder build, String details) {
				return null; // Will attempt to process ANOTHER enum factory containing list of possible added effects.
				// Or maybe this shouldn't be called effect, but BLLEd and POISON and HEAL and WHATEVER.
				
				// Where does COOLDOWN go? SPEED?
			}
			
		}; //TODO NAME, DESCRPTION, LOTS OF EFFECTS, INDUCTION, COOLDOWN BLAH BLAH
		
		private ArcanistComponentsFactory() {}	
		
		public abstract ArcanistBuilder getBlock(ArcanistBuilder build, String details);
		
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
