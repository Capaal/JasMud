package skills;

import interfaces.Mobile;

import java.util.Iterator;
import java.util.Map;

import processes.SkillBook;
import processes.Skills;
import processes.UsefulCommands;

public class SkillList extends Skills {
	
	public SkillList(Mobile currentPlayer, String fullCommand) {
		super("skills", "Inventory of your knowledge.", currentPlayer, fullCommand);
		super.syntaxList.add(Syntax.SKILL);
		super.syntaxList.add(Syntax.TARGET);
		super.syntaxList.add(Syntax.ITEM);
	}
	
	@Override
	protected void performSkill() {
		Map<SkillBook, Integer> books = currentPlayer.viewSkillBooks();
		String s = Syntax.TARGET.getStringInfo(fullCommand, this).toLowerCase();
		books = currentPlayer.viewSkillBooks();
		if (s.equals("")) {
			displayBooks(books);
		} else {
			String specific = Syntax.ITEM.getStringInfo(fullCommand, this).toLowerCase();
			SkillBook sb = bookFound(books, s);
			if (sb != null) {
				if (specific.equals("")) {
					displaySkillsInBook(sb);
				} else {
					displaySkillInfo(sb, specific);
				}
			}
		}

	}
		
	private void displayBooks(Map<SkillBook, Integer> books) {
		messageSelf(UsefulCommands.ANSI.CYAN + "Your skillbooks: " + UsefulCommands.ANSI.SANE);
		for (SkillBook b : books.keySet()) {
			messageSelf(b.getName());
		}
		messageSelf("SKILLS [BookName] for a list of skills in each book.");
	}
	
	private void displaySkillsInBook(SkillBook sb) {
		messageSelf(UsefulCommands.ANSI.CYAN + sb.getName() + " skills :" + UsefulCommands.ANSI.SANE);
		for (Skills sk : sb.viewSkills().values()) {
			if (sk.getName() != "skills") {
				messageSelf(sk.getName());
			}
		}
		messageSelf("SKILLS [BookName] [SkillName] for more info on a specific skill.");
	}
	
	private void displaySkillInfo(SkillBook sb, String specific) {
		Skills toInfo = sb.getSkill(specific);
		if (toInfo == null) {
			messageSelf("That's not a valid " + sb.getName() + " skill.");
		} else {
			messageSelf(toInfo.getDescription());
	//		messageSelf("Syntax: " + toInfo.displaySyntax());
		}
	}
	
	private SkillBook bookFound(Map<SkillBook, Integer> books, String s) {
		Iterator<SkillBook> i = books.keySet().iterator();
		SkillBook sb = null;
		while (i.hasNext() && sb == null) {
			sb = i.next();
			if (!sb.getName().equals(s)) {
				sb = null;
			}
		}
		if (sb == null) {
			messageSelf("You don't have that skillbook.");
		}
		return sb;
	}

	@Override
	protected boolean preSkillChecks() {
		return true;
	}
}
