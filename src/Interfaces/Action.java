package interfaces;

import java.util.ArrayList;
import java.util.HashMap;

import interfaces.Container;
import processes.Skill;
import processes.Skill.Syntax;
import processes.WorldServer;

public abstract class Action {
	
	protected int id;

	public abstract boolean activate(Skill s);
	
	public boolean save(int position) {	
		HashMap<String, Object> blockView = selectOneself(position);
		if (blockView.size() == 0) {
			insertOneself(position);
			blockView = selectOneself(position);
		}
		this.id = (int) blockView.get("BLOCKID");
		return true;
	}
	
	protected abstract void insertOneself(int position);
	public abstract HashMap<String, Object> selectOneself(int position);
	
	public int getId() {
		return id;
	}
	
	public void setId(int id) {
		this.id = id;
	}
	
	public abstract Action newBlock(Mobile player);
	
	public abstract void explainOneself(Mobile player);
	
	/* Questions about Targetting:
	 * A skill will certainly be used.
	 * We may know the specific target to effect (ourselves, Sastri, goblin12345)
	 * We may not know the target (fireball here blows up everything in this Container)
	 * We may know both specific and not (fireball north goblin12345 hits that goblin and anything in that Container.
	 * We might target only allies or only enemies, or not allies or not enemies.
	 * The spell will be limited to its range, only in current Container, 1 Container away, 3 Containers away, projectile in a direction.
	 * damage 10 doesn't need to know whether it is allowed or not, there just needs to be a check beforehand that cancels skill if it fails.
	 * damage 10 does need to know it's target, should it be sent in a mobile and that is it's target? very hard to link actions like this.
	 * We could set up skill to be "damage 10 self" or "damage 10 target" or "damage 10 I HAVE NO IDEA"
	 * We could have the check for if damage can hit target, then have all actions return true false for whether they are allowed or not.
	 * But then we need to send in even more information, maybe something like "damage 10 self here" or "damage 10 target projectile(north)" 
	 * 
	 * OK LETS TRY TO EXAMPLES
	 * 
	 * Blood magic spell: cast bloodburn jason12345
	 * 	check mana 10
	 * 	damage 10 self
	 * 	damage 50 target here
	 * 
	 * firebolt magic spell: cast firebolt jason12345 north
	 * 	check mana 5
	 * 	damage 25 target projectile north
	 * 
	 * Multimissle magic spell: cast multimissle jason12345 north
	 * 	check mana 25
	 * 	damage 10 target projectile north
	 * 	damage 10 target projectile north
	 * 	damage 10 target projectile north
	 * 
	 * Fireball magic spell: cast fireball here
	 * 	check mana 20
	 * 	damage 20 all here
	 * 
	 * Fireball magic spell: cast fireball jason12345 north
	 * 	check mana 20
	 * 	damage 20 target projectile north
	 * 	damage 20 allnottarget target
	 * 
	 * Beam magic spell: cast beam north
	 * 	check mana 75
	 * 	damage 20 all projectile
	 * 
	 * Slash attack: slash jason12345
	 * 	check balance
	 * 	damage 15 target here
	 * 
	 * 
	 * SO it seems like the general syntax seems to be going like...
	 * 
	 * name of action followed by that actions specifics.
	 * 
	 * For Damage it seems to be...
	 * 
	 * NAME(damage) INTENSITY(20) RULESFORWHOGETSHURT(all or target or self or allies) RULESFORWHERE(projectile(dir) or target or here)
	 * RULESFORWHERE is a little dangerous, target would imply a guaranteed hit, not true for here as target might not be there.
	 * 
	 * Ok, so need to build TWO enums, one for rules for who gets targetted, and another for rules for where.
	 * 
	 * List of actions I'll need:
	 * 	damage done
	 * 	checks done
	 * 	messages done
	 * 	effects done
	 * 	costs done
	 *  type done
	 *  chance
	 *  
	 *  Other POSSIBLE actions: What about things I've made already? Get, Throw, Move, Create, Drop, Look, are those "actions" building blocks?
	 */
	
	// Covers RULES FOR WHERE
	// HERE, PROJECTILE, TARGET
	public enum Where {
				
		HERE() {
			@Override
			public ArrayList<Container> findLoc(Skill s) {
				ArrayList<Container> loc = new ArrayList<Container>();
				loc.add(s.getCurrentPlayer().getContainer());
				return loc;
			}
		},
		
		// Below probably doesn't account for miss-spellings or leaving out the id? it needs work.
		TARGET() {
			@Override
			public ArrayList<Container> findLoc(Skill s) {			
				ArrayList<Container> loc = new ArrayList<Container>();				
				Mobile t = WorldServer.mobList.get(s.getStringInfo(Syntax.TARGET));
				if (t != null) {
					loc.add(t.getContainer());
					return loc;
				}				
				return null;
			}
		},
		
		// This would check personal inventory, rework for Container being anyone's inventory?
		INVENTORY() {
			@Override
			public ArrayList<Container> findLoc(Skill s) {			
				ArrayList<Container> loc = new ArrayList<Container>();				
				loc.add(s.getCurrentPlayer());
				return loc;
			}
		},
		
		// Only works with second word at the moment.
		ONEAWAY() {
			@Override
			public ArrayList<Container> findLoc(Skill s) {
				ArrayList<Container> loc = new ArrayList<Container>();
				String dir = s.getStringInfo(Syntax.DIRECTION);
				if (dir != null) {
					loc.add(s.getCurrentPlayer().getContainer().getContainer(dir));
				}
				return loc;
			}
		},
		
		// Specifically uses the third word in command, seems a bit odd.
		PROJECTILE() {
			@Override
			public ArrayList<Container> findLoc(Skill s) {
				ArrayList<Container> loc = new ArrayList<Container>();
				String dir = s.getStringInfo(Syntax.DIRECTION);
				Container onPath = s.getCurrentPlayer().getContainer();
				loc.add(onPath);
				onPath = onPath.getContainer(dir);
				while (onPath != null) {
					loc.add(onPath);
				}
				return loc;
			}
			
		
		};
		//Constructor
		private Where() {			
		}		
		public abstract ArrayList<Container> findLoc(Skill s);
	}
	
	// Covers RULES FOR WHO
	// SELF, All, TARGET
	public enum Who {
			
		SELF() {
			@Override
			public ArrayList<Mobile> findTarget(Skill s, ArrayList<Container> Containers) {
				ArrayList<Mobile> targ = new ArrayList<Mobile>();
				targ.add(s.getCurrentPlayer());
				return targ;
			}
		},
		
		ALL() {
			@Override
			public ArrayList<Mobile> findTarget(Skill s, ArrayList<Container> Containers) {
				ArrayList<Mobile> targ = new ArrayList<Mobile>();
				for (Container l : Containers) {
					for (Holdable m : l.getInventory()) {
						if (m instanceof Mobile) {
							targ.add((Mobile) m);
						}
					}
				}
				return targ;
			}
		},
		
		TARGET() {
			@Override
			public ArrayList<Mobile> findTarget(Skill s, ArrayList<Container> Containers) {
				ArrayList<Mobile> targ = new ArrayList<Mobile>();
				for (Container l : Containers) {
					Holdable h = l.getHoldableFromString(s.getStringInfo(Syntax.TARGET));
					if (h != null) {
						targ.add((Mobile)h);
						return targ;
					}
				}
				return null;					
			}
		},
		// handles grabbing JUST one target or JUST self poorly.
		OTHERS() {
			@Override
			public ArrayList<Mobile> findTarget(Skill s, ArrayList<Container> Containers) {
				ArrayList<Mobile> targs = new ArrayList<Mobile>();
				ArrayList<Mobile> targets = TARGET.findTarget(s, Containers);
				Mobile target = null;
				if (targets != null) {
					target = targets.get(0);
				}
				Mobile self = (SELF.findTarget(s, Containers)).get(0);
				for (Container l : Containers) {
					for (Holdable h : l.getInventory()) {
						if (h instanceof Mobile && h != target && h != self) {
							targs.add((Mobile)h);
						}
					}
					return targs;
				}
				return null;					
			}
		},
		
		ALLIES() {
			@Override
			public ArrayList<Mobile> findTarget(Skill s, ArrayList<Container> Containers) {
				
				return null;
			}
		},
		
		ENEMIES() {
			@Override
			public ArrayList<Mobile> findTarget(Skill s, ArrayList<Container> Containers) {
				
				return null;
			}
		};
		
		//Constructor
		private Who() {			
		}		
		public abstract ArrayList<Mobile> findTarget(Skill s, ArrayList<Container> Containers);
	}
}
