package skills.Arcanist.Commands;

import java.util.Arrays;
import java.util.List;

import interfaces.Mobile;
import processes.Skills;
import processes.Skills.Syntax;
import skills.Arcanist.Commands.ArcanistAlter.ArcanistComponentsFactory;

public class ArcanistComponents extends Skills {
	
	ComponentGroups component; 

	public ArcanistComponents(Mobile currentPlayer, String fullCommand) {
		super("components", "Displays available components and their workings", currentPlayer, fullCommand);
		syntaxList.add(Syntax.SKILL);
		syntaxList.add(Syntax.COMPONENT);
	}

	@Override
	protected void performSkill() {
		if (preSkillChecks()) {
			messageSelf(component.describeType());
		}
	}

	@Override
	protected boolean preSkillChecks() {
		String componentString = Syntax.COMPONENT.getStringInfo(fullCommand, this);
		if (componentString.equals("")) {			
			messageSelf("What component category do you want to examine?");
			displayComponents();
			return false;
		}
		try {
			component = ComponentGroups.valueOf(componentString.toUpperCase());
		} catch (IllegalArgumentException e) {
			messageSelf("That was not a valid component type.");
			displayComponents();
			return false;
		}
		return true;
	}
	
	private void displayComponents() {
		StringBuilder sb = new StringBuilder();
		for (ComponentGroups c : ComponentGroups.values()) {
			sb.append(" ");
			sb.append(c);
		}			
		messageSelf(sb.toString());
	}

	@Override
	public Skills getNewInstance(Mobile currentPlayer, String fullCommand) {
		return new ArcanistComponents(currentPlayer, fullCommand);
	}
	
	private enum ComponentGroups {
		BASICS(Arrays.asList(ArcanistComponentsFactory.DAMAGE, ArcanistComponentsFactory.MANA, ArcanistComponentsFactory.SPEED)) {
			
			
		},
		
		TARGETTING(Arrays.asList(ArcanistComponentsFactory.WHO, ArcanistComponentsFactory.WHERE)) {
			
		},
		
		EFFECTS(Arrays.asList(ArcanistComponentsFactory.BLEED, ArcanistComponentsFactory.HEAL)) {
			
		};
		
		private final List<ArcanistComponentsFactory> linkedComponents;
		
		
		private ComponentGroups(List<ArcanistComponentsFactory> linkedComponents) {	
			this.linkedComponents = linkedComponents;
		}
		
		public String describeType() {
			StringBuilder sb = new StringBuilder();
			boolean first = true;	
			for (ArcanistComponentsFactory c : linkedComponents) {
				if (first) {
					sb.append(c.describeYourself());
					first = false;
				} else {
					sb.append(System.lineSeparator());
					sb.append(c.describeYourself());	
				}
			}
			return sb.toString();
		}
	}

}
