package skills.Arcanist.Commands;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

import effects.PassiveCondition;
import interfaces.Mobile;
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
		// Case of DESCRIPTION
		if (factory.equals(ArcanistComponentsFactory.DESCRIPTION)) {
			String[] split = fullCommand.split(" ", 3);
			if (split.length > 2) {
				currentSkill = factory.getBlock(currentSkill, split[2]);
				messageSelf("Alteration successful.");
			} else {
				messageSelf("Alteration unsuccessful, the component details were invalid.");
			}
			// All other cases.
		} else {
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
		currentBook = ArcanistSkillbook.getCurrentBook(currentPlayer);
		if (currentBook == null) {
			messageSelf("But you have no book for which to scribe!");
			System.out.println("Serious bug, player missing ArcanistSkillbook but used ALTER.");
			return false;
		}
		currentSkill = currentBook.getCurrentSkillBuilder();
		if (currentSkill == null) {
			messageSelf("You have not yet begun to CREATE a new spell.");
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
	
	// Notes about adding new block:
	// Components skills: Add to a type so it displays.
	//
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
					Iterator<ArcanistBlockRequired> iter = build.getRequiredBlocks().iterator();
					while (iter.hasNext()) {
					    if (iter.next() instanceof ManaBlock) {
					        iter.remove();
					    }
					}
					build.addRequiredBlock(new ManaBlock(manaCost));
					return build;
				//	if (manaCost >= 0 && manaCost <= 100) { // May not be negative nor more than 100 mana.
				//		build.setMana(manaCost);
				//		return build;
				//	}
				}
				return null;
			}
			@Override
			public String describeYourself() {
				return "Alter Mana [Required Mana]: Sets the amount of mana to cast this spell. Cost: -mana.";
			}
		},
		
		BLOODCOST() {
			@Override
			public  ArcanistBuilder getBlock(ArcanistBuilder build, String details) {
				if (UsefulCommands.isInteger(details)) {				
					int lifeCost = Integer.parseInt(details);
					
					// Lambda version
					build.setRequiredBlocks(build.getRequiredBlocks().stream()
							.filter(x -> x instanceof BloodBlock)
							.collect(Collectors.toList()));
					
		//		build.getRequiredBlocks().removeAll(build.getRequiredBlocks()
		//			.stream().filter(x -> x instanceof BloodBlock)
		//			.collect(Collectors.toList()));
					
					// Regular version
				//	Iterator<ArcanistBlockRequired> iter = build.getRequiredBlocks().iterator();
				//	while (iter.hasNext()) {
				//	    if (iter.next() instanceof BloodBlock) {
				//	        iter.remove();
				//	    }
				//	}
					build.addRequiredBlock(new BloodBlock(lifeCost));
					return build;
				}
				return null;
			}
			@Override
			public String describeYourself() {
				return "Alter Bloodcost [Required Life]: Sets the amount of life that must be paid. Cost: +life.";
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
				return "Alter Speed [Time in Seconds]: Sets resulting balance duration in seconds. Cost: High";
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
		
		DESCRIPTION() {
			@Override
			public  ArcanistBuilder getBlock(ArcanistBuilder build, String details) {
				if (!details.equals("")) {
					build.setDescription(details);
					return build;
				}
				return null;
			}
			
			public String describeYourself() {
				return " Alter Description [description]: Alters your personal description. Cost: Free!.";
			}
			
		},
		
		// TODO Very tenative. Generic, not balanced, can cause balance, sleep, and other stuff.
		// Does NOT apply a limited duration, so if a condi that is usually very temparary is added...
		CONDITION() {
			@Override
			public  ArcanistBuilder getBlock(ArcanistBuilder build, String details) {
				if (!details.equals("")) {
					PassiveCondition condi;
					try {
						condi = PassiveCondition.valueOf(details.toUpperCase());
						List<ArcanistBlock> effects = build.getDamageBlock().getAddedEffects();
						if (effects == null) {
							effects = new ArrayList<ArcanistBlock>();
						}
						Iterator<ArcanistBlock> iter = effects.iterator();
						while (iter.hasNext()) {
							ArcanistBlock block = iter.next();
						    if (block instanceof PassiveConditionBlock && ((PassiveConditionBlock)block).condiToApply.equals(condi)) {
						    	iter.remove();
						    }
						}
						effects.add(new PassiveConditionBlock(condi));
						build.setDamage(build.getDamageBlock().getNewInstance(build.getDamageBlock().getDamage(), effects));
						return build;
					} catch (IllegalArgumentException e) {
						return null;
					}
				}
				return null;
			}
			
			public String describeYourself() {
				return " Alter Description [description]: Alters your personal description. Cost: Free!.";
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
					Iterator<ArcanistBlock> iter = effects.iterator();
					while (iter.hasNext()) {
					    if (iter.next() instanceof BleedBlock) {
					        iter.remove();
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
				return " Alter Bleed [Starting Intensity]: Intensity decays every tick. Cost: High, scales with intensity.";
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
