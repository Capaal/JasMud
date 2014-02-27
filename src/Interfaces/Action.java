package interfaces;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import interfaces.Container;
import processes.Location.Direction;
import processes.Skill;
import processes.Skill.Syntax;
import processes.WorldServer;

public abstract class Action {
	
	protected int id;

	public abstract boolean activate(Skill s, String fullCommand, Mobile currentPlayer);
	
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
	 *  
	 *  What WHERE must be able to do:
	 *  In the location the user is
	 *  In a single location in any direction in a straight line.
	 *  All locations in a straight line in any direction.
	 *  A single target, anywhere.
	 *  A bunch of targets anywhere?
	 *  All targets within x squares?
	 *  inside inventories? include equipment.
	 *  
	 *  *************************************************************************************************************************
	 *  
	 *  RECURSIVE SYSTEM NOTES
	 *  
	 *  I need to implement a recrusive system for targetting. This is because if I want a skill that does something like:
	 *  ALL players in ALL location ONEAWAY from the location TWO locations NORTH of the USER.
	 *  To do that I'll either need to allow three targettings to combine, ONEAWAYPROJECTILE north, ONEAWAYPROJECTILE north, ONEAWAY.
	 *  That effectely does the above skill. Alternately I have to write code called something like TWOAWAYALLAROUND
	 *  Which would select all locations around two locations north. 
	 *  
	 *  I think the first option sounds like the only reasonable choice. ANY other ideas?
	 *  
	 *  RECURSIVE blocks. kind of like damage(30, ONEAWAYPROJECTILE north, TARGET, damage(10, ONEAWAY, ALL)).
	 *  This way it does the first damage, and then does the second damage, except the key is that it knows where it currently is?
	 *  This way the first block got it one north, and did damage, and then the second block gets to do stuff from 1 north of the user.
	 *  
	 *  Could the above be done with an AND block?  damage 30 one north, damage 10 all around from here (which is one north)?
	 *  This would effectively run one, and then the other They wouldn't know yet about WHERE they are.
	 *  So I'd have to put in code to put in WHERE they are, most would just take in the USERS location.
	 *  But in the and it would be ACTION ONE (user location), action two (action one's location), but nothing remembers....
	 *  
	 *  ANOTHER IDEA: A TARGET block who's job it is to figure out whether the target can be hit following the rules, and then let
	 *  all the other blocks do whatever they want. But you still have the problem of targetting. But maybe a targetting block
	 *  would be allowed to know it's location, in which case it could recursively do it's targetting.
	 *  
	 *  CONCLUSION AS OF 2/27/2014:
	 *  I Think we should stick with the original plan where I just program a targetting scheme as we need it.
	 *  For example, a huge majority of skills will either target the location the user is in, or in some form of projectile.
	 *  This means I'm only making < 10, doing recursive targetting IS more powerful, but it is more complicated in every other way.
	 *  And if some skill demands something really complicated, I can make that, but 90% of the skills we make won't.
	 *  The axe is sharp enough already.
	 */

	// Covers RULES FOR WHERE
	// HERE, PROJECTILE, TARGET
	public enum Where {
			
		//Targetting the location the user is in.
		HERE() {
			@Override
			public ArrayList<Container> findLoc(Skill s, String fullCommand, Mobile currentPlayer) {
				ArrayList<Container> loc = new ArrayList<Container>();
				loc.add(currentPlayer.getContainer());
				return loc;
			}
		},
		
		// Targetting whatever location that the target is in, inefficient. (Implies NO rules, always hit)
		//TODO Below probably doesn't account for miss-spellings or leaving out the id? it needs work.
		TARGET() {
			@Override
			public ArrayList<Container> findLoc(Skill s, String fullCommand, Mobile currentPlayer) {			
				ArrayList<Container> loc = new ArrayList<Container>();				
				Mobile t = WorldServer.mobList.get(s.getStringInfo(Syntax.TARGET, fullCommand));
				if (t != null) {
					loc.add(t.getContainer());
				}				
				return loc;
			}
		},
		
		// Targetting special containers that is not a location.
		//TODO This would check personal inventory, rework for Container being anyone's inventory?
		INVENTORY() {
			@Override
			public ArrayList<Container> findLoc(Skill s, String fullCommand, Mobile currentPlayer) {			
				ArrayList<Container> loc = new ArrayList<Container>();				
				loc.add(currentPlayer);
				return loc;
			}
		},
		
		// Targetting all locations surrounding the user. Probably don't even need this.
		//TODO Ugly, too much Direction conversion and only is one away from the USER
		ONEAWAY() {
			@Override
			public ArrayList<Container> findLoc(Skill s, String fullCommand, Mobile currentPlayer) {
				ArrayList<Container> loc = new ArrayList<Container>();
				for (Direction d : Direction.values()) {
					loc.add(currentPlayer.getContainer().getContainer(d.toString()));
				}			
				return loc;
			}
		},
		
		// Targetting the location 1 away in 1 direction
		ONEAWAYPROJECTILE() {
			@Override
			public ArrayList<Container> findLoc(Skill s, String fullCommand, Mobile currentPlayer) {
				ArrayList<Container> loc = new ArrayList<Container>();
				String dir = s.getStringInfo(Syntax.DIRECTION, fullCommand);
				if (!dir.equals("")) {
					loc.add(currentPlayer.getContainer().getContainer(dir));
				}
				return loc;
			}
		},
		
		// Targets all locations in a particular direction, NOT user's location.
		PROJECTILE() {
			@Override
			public ArrayList<Container> findLoc(Skill s, String fullCommand, Mobile currentPlayer) {
				ArrayList<Container> loc = new ArrayList<Container>();
				String dir = s.getStringInfo(Syntax.DIRECTION, fullCommand);
				Container onPath = currentPlayer.getContainer().getContainer(dir);
				while (onPath != null) {
					loc.add(onPath);
					onPath = onPath.getContainer(dir);
				}
				return loc;
			}
			
		
		};
		//Constructor
		private Where() {			
		}		
		public abstract ArrayList<Container> findLoc(Skill s, String fullCommand, Mobile currentPlayer);
	}
	//TODO WHY DO THESE RETURN ARRAYS OF MOBILES???? Shouldn't it just return holdables, so that we can target items?
	// Covers RULES FOR WHO
	// SELF, All, TARGET
	public enum Who {
			
		// Targets the USER, even if WHERE is incorrect.
		SELF() {
			@Override
			public ArrayList<Mobile> findTarget(Skill s, String fullCommand, Mobile currentPlayer, ArrayList<Container> Containers) {
				ArrayList<Mobile> targ = new ArrayList<Mobile>();
				targ.add(currentPlayer);
				return targ;
			}
		},
		
		// Targets EVERYONE in the locations given by WHERE.
		ALL() {
			@Override
			public ArrayList<Mobile> findTarget(Skill s, String fullCommand, Mobile currentPlayer, ArrayList<Container> Containers) {
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
		// Targets the Mobile named as TARGET by skill syntax, but only if the target is in a location from WHERE.
		TARGET() {
			@Override
			public ArrayList<Mobile> findTarget(Skill s, String fullCommand, Mobile currentPlayer, ArrayList<Container> Containers) {
				ArrayList<Mobile> targ = new ArrayList<Mobile>();
				for (Container l : Containers) {
					Holdable h = l.getHoldableFromString(s.getStringInfo(Syntax.TARGET, fullCommand));
					if (h != null) {
						targ.add((Mobile)h);
						return targ;
					}
				}
				return targ;					
			}
		},
		
		// Targets the Mobiles named in the list, effectively like multiple targets.
		LIST() {
			@Override
			public ArrayList<Mobile> findTarget(Skill s, String fullCommand, Mobile currentPlayer, ArrayList<Container> Containers) {
				ArrayList<Mobile> targets = new ArrayList<Mobile>();
				ArrayList<String> listInfo = new ArrayList<String>();
				StringTokenizer st = new StringTokenizer(s.getStringInfo(Syntax.TARGET, fullCommand));
				while (st.hasMoreTokens()) {
					listInfo.add(st.nextToken());
				}				
				for (Container l : Containers) {
					for (String listName : listInfo) {
						Holdable h = l.getHoldableFromString(listName);
						if (h != null) {
							targets.add((Mobile)h);
						}
					}
				}
				return targets;					
			}
		},
		
		// Targets everyone found in WHERE locations who is NOT self NOR target.
		// handles grabbing JUST one target or JUST self poorly.
		OTHERS() {
			@Override
			public ArrayList<Mobile> findTarget(Skill s, String fullCommand, Mobile currentPlayer, ArrayList<Container> Containers) {
				ArrayList<Mobile> targs = new ArrayList<Mobile>();
				ArrayList<Mobile> targets = TARGET.findTarget(s, fullCommand, currentPlayer, Containers);
				Mobile self = null;
				ArrayList<Mobile> selfs = SELF.findTarget(s, fullCommand, currentPlayer, Containers);
				Mobile target = null;
				if (!targets.isEmpty() && !selfs.isEmpty()) {
					target = targets.get(0);
					self = selfs.get(0);
				} else {
					return targs;
				}			
				for (Container l : Containers) {
					for (Holdable h : l.getInventory()) {
						if (h instanceof Mobile && h != target && h != self) {
							targs.add((Mobile)h);
						}
					}
					return targs;
				}
				return targs;					
			}
		},
		
		// Not implemented, but would target only those on user's friends list.
		ALLIES() {
			@Override
			public ArrayList<Mobile> findTarget(Skill s, String fullCommand, Mobile currentPlayer, ArrayList<Container> Containers) {
				//TODO
				return new ArrayList<Mobile>();
			}
		},
		// Not implemented, but would target only those on user's enemies list.
		ENEMIES() {
			@Override
			public ArrayList<Mobile> findTarget(Skill s, String fullCommand, Mobile currentPlayer, ArrayList<Container> Containers) {
				//TODO
				return new ArrayList<Mobile>();
			}
		};
		
		//Constructor
		private Who() {			
		}		
		public abstract ArrayList<Mobile> findTarget(Skill s, String fullCommand, Mobile currentPlayer, ArrayList<Container> Containers);
	}
}
