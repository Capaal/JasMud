insert into locationstats (LOCID, LOCNAME, LOCDES, LOCTYPE) values ('1','Beach','This is start.','GROUND');
insert into locationstats (LOCID, LOCNAME, LOCDES, LOCTYPE) values ('2','Road north of beach','This is north of start.','GROUND');
insert into locationstats (LOCID, LOCNAME, LOCDES, LOCTYPE) values ('3','Mud Intro Shack','This will be the mud intro shack.','GROUND');
insert into locationstats (LOCID, LOCNAME, LOCDES, LOCTYPE) values ('4','East of road','This is east of the road.','GROUND');
insert into locationstats (LOCID, LOCNAME, LOCDES, LOCTYPE) values ('5','Tidepool','This is south of start.','WATER');

update locationstats set locsouth='1',locsouthdir='NORTH' where locid='2';
update locationstats set locse='1',locsedir='NORTHWEST' where locid='3';
update locationstats set locwest='2',locwestdir='EAST' where locid='4';
update locationstats set locnorth='1',locnorthdir='SOUTH' where locid='5';
select * from locationstats;


insert into itemstats (ITEMNAME, ITEMPHYS, ITEMBAL, ITEMDESC, ITEMSHORTD, ITEMMAXDUR, ITEMCURDUR, ITEMLOC, ITEMLOCTYPE)
		values ('Dagger', 1.10, 1.5, 'Short and sharp.','a dagger',100, 100, 1, 'LOCATION');

insert into itemtypetable (ITEMID, TYPEID) values (1, 2);
insert into type (TYPE) values ('PIERCE');
insert into itemtypetable (ITEMID, TYPEID) values (1, 1);

insert into SLOTTABLE (ITEMID, SLOTID) values (1, 1);
insert into SLOTTABLE (ITEMID, SLOTID) values (1, 2);
insert into SLOT (SLOT) values ('RIGHTHAND');
insert into SLOT (SLOT) values ('LEFTHAND');

insert into mobstats (MOBID, MOBNAME, MOBPASS, MOBDESC, MOBSHORTD, MOBLOC, MOBTYPE, MOBCURRENTHP, MOBDEAD, MOBXPWORTH, MOBCURRENTXP, MOBCURRENTLEVEL, MOBAGE, LOADONSTARTUP)
	values	(NULL, 'James', 'James', 'He is a farmer named James.','a farmer', 1, 'STDMOB', 50, false, 10, 1, 1, 1, true);
/*insert into mobstats values ('3','defaultGod', 'defaultGod', 'Contains God skills.', 'A god skin.','1', 'STDMOB', false);*/
select * from mobstats;

insert into SKILLBOOK (SKILLBOOKID, SKILLBOOKNAME) values (1, 'Basic Skills');

/* Will need to add skillbook progress for leveling skills */
insert into SKILLBOOKTABLE (MOBID, SKILLBOOKID, MOBPROGRESS) values(3, 1, 1);

insert into SKILL (SKILLID, SKILLDES, SKILLNAME, SKILLFAILMSG) values('1', '1', 'damage', 'FAIL');
insert into SKILL (SKILLID, SKILLDES, SKILLNAME, SKILLFAILMSG) values('2', 'blah', 'slash', 'FAIL');

insert into blocktable (SKILLID, BLOCKID) values('1', '1');
insert into blocktable (SKILLID, BLOCKID) values('1', '2');
insert into blocktable (SKILLID, BLOCKID) values('2', '2');
insert into blocktable (SKILLID, BLOCKID) values('2', '3');
insert into blocktable (SKILLID, BLOCKID) values('2', '4');
/*
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, INTVALUE, TARGETWHO, TARGETWHERE) values('1', 'DAMAGE', '1', '20', 'TARGET', 'HERE');
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, INTVALUE, TARGETWHO, TARGETWHERE) values('2', 'DAMAGE', '1', '5', 'TARGET', 'HERE');
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, INTVALUE, TARGETWHO, TARGETWHERE) values('3', 'DAMAGE', '2', '5', 'TARGET', 'HERE');
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, INTVALUE, TARGETWHO, TARGETWHERE) values('4', 'DAMAGE', '3', '5', 'TARGET', 'HERE');
*/
insert into syntaxtable (SKILLID, SYNTAXID) values ('1', '1');
insert into syntaxtable (SKILLID, SYNTAXID) values ('1', '2');
insert into syntaxtable (SKILLID, SYNTAXID) values ('2', '1');
insert into syntaxtable (SKILLID, SYNTAXID) values ('2', '2');

insert into syntax (SYNTAXPOS, SYNTAXTYPE) values ('1', 'TARGET');
insert into syntax (SYNTAXPOS, SYNTAXTYPE) values ('0', 'SKILL');



/* GASH */
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, TARGETWHO, TARGETWHERE, INTVALUE) values('5', 'BALANCECOST', 0, 'SELF', 'HERE', 4000);
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, INTVALUE, TARGETWHO, TARGETWHERE, BOOLEANONE, TYPE) values('6', 'DAMAGE', 5, '10', 'TARGET', 'HERE', 'TRUE', 'SHARP');
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, INTVALUE, INTVALUETWO, TARGETWHO, TARGETWHERE) values('7', 'BLEEDEFFECT', '3', 5000, 5, 'TARGET', 'HERE');
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, INTVALUE, TARGETWHO, TARGETWHERE, BOOLEANONE, TYPE) values('8', 'DAMAGE', 2, '-15', 'SELF', 'HERE', 'FALSE', NULL);
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, BLOCKPOINTERONE) values('9', 'CHANCE', '4', '8');
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, TYPE, TARGETWHO, TARGETWHERE) values('10', 'WEAPONEQUIPPEDCHECK', 1, 'SHARP', 'SELF', 'HERE');
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, STRINGONE, TARGETWHO, TARGETWHERE) values('11', 'MESSAGE', '6', 'You make a sharp slash at %s and then %s turns and fights back.', 'SELF', 'HERE');
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, STRINGONE, TARGETWHO, TARGETWHERE) values('12', 'MESSAGE', '7', 'You watch as %s slashes horribly at %s and %s turns to fight back.', 'OTHERS', 'HERE');
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, STRINGONE, TARGETWHO, TARGETWHERE) values('13', 'MESSAGE', '8',  '%s slashes you painfully.', 'TARGET', 'HERE');
insert into blocktable (SKILLID, BLOCKID) values('3', '5');
insert into blocktable (SKILLID, BLOCKID) values('3', '6');
insert into blocktable (SKILLID, BLOCKID) values('3', '7');
insert into blocktable (SKILLID, BLOCKID) values('3', '9');
insert into blocktable (SKILLID, BLOCKID) values('3', '10');
insert into blocktable (SKILLID, BLOCKID) values('3', '11');
insert into blocktable (SKILLID, BLOCKID) values('3', '12');
insert into blocktable (SKILLID, BLOCKID) values('3', '13');	
insert into SKILL (SKILLID, SKILLNAME, SKILLDES, SKILLFAILMSG) values(3, 'gash', 'A horrible gashing slash, that for some reason heals sometimes...?', 'Your clusmy gash misses.');
insert into SKILLTABLE (SKILLBOOKID, SKILLID) values(1, 3);
insert into syntaxtable (SKILLID, SYNTAXID) values ('3', '1');
insert into syntaxtable (SKILLID, SYNTAXID) values ('3', '2');
insert into skilltypetable (SKILLID, TYPEID) values ('3', '1');
insert into type (TYPE) values ('SHARP');
insert into msgstringstable (BLOCKID, MSGSTRINGSID) values ('11', '1');
insert into msgstringstable (BLOCKID, MSGSTRINGSID) values ('11', '2');
insert into msgstringstable (BLOCKID, MSGSTRINGSID) values ('12', '3');
insert into msgstringstable (BLOCKID, MSGSTRINGSID) values ('12', '4');
insert into msgstringstable (BLOCKID, MSGSTRINGSID) values ('12', '5');
insert into msgstringstable (BLOCKID, MSGSTRINGSID) values ('13', '3');
insert into msgstrings (MSGSTRINGSID, MSGSTRINGSPOS, MSGSTRINGSTYPE) values (1, 1, 'TARGET');
insert into msgstrings (MSGSTRINGSID, MSGSTRINGSPOS, MSGSTRINGSTYPE) values ('2', '2', 'TARGET');
insert into msgstrings (MSGSTRINGSID, MSGSTRINGSPOS, MSGSTRINGSTYPE) values ('3', '1', 'SELF');
insert into msgstrings (MSGSTRINGSID, MSGSTRINGSPOS, MSGSTRINGSTYPE) values ('4', '2', 'TARGET');
insert into msgstrings (MSGSTRINGSID, MSGSTRINGSPOS, MSGSTRINGSTYPE) values ('5', '3', 'TARGET');

/* GETBALANCE 
insert into SKILL (SKILLID, SKILLNAME, SKILLDES, SKILLFAILMSG) values('4', 'getbalance', 'It gives you back balance instantly.', 'You probably already have balance.');
insert into SKILLTABLE (SKILLBOOKID, SKILLID) values(1, 4);
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, BOOLEANONE, TARGETWHO, TARGETWHERE) values('14', 'BALANCECOST', '1', 'TRUE', 'SELF', 'HERE');
insert into blocktable (SKILLID, BLOCKID) values('4', '14');
*/
/* SAY */
insert into SKILL (SKILLID, SKILLNAME, SKILLDES, SKILLFAILMSG) values('5', 'say', 'Speaking to those nearby.', 'You emit no noise.');
insert into SKILLTABLE (SKILLBOOKID, SKILLID) values(1, 5);
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS) values('15', 'SAY', '1');
insert into blocktable (SKILLID, BLOCKID) values('5', '15');
insert into syntax (SYNTAXPOS, SYNTAXTYPE) values ('2', 'LIST');
insert into syntaxtable (SKILLID, SYNTAXID) values ('5', '3');
insert into syntaxtable (SKILLID, SYNTAXID) values ('5', '1');

/* EXAMINE */
insert into SKILL (SKILLID, SKILLNAME, SKILLDES, SKILLFAILMSG) values('6', 'examine', 'Taking a closer look at objects', 'Examining the air is difficult.');
insert into SKILLTABLE (SKILLBOOKID, SKILLID) values(1, 6);
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, BLOCKPOINTERONE, BLOCKPOINTERTWO) values('16', 'OR', '1', '17', '18');
insert into block (BLOCKID, BLOCKTYPE, TARGETWHERE) values('17', 'EXAMINE','INVENTORY');
insert into block (BLOCKID, BLOCKTYPE, TARGETWHERE) values('18', 'EXAMINE','HERE');
insert into blocktable (SKILLID, BLOCKID) values('6', '16');
insert into syntaxtable (SKILLID, SYNTAXID) values ('6', '2');
insert into syntaxtable (SKILLID, SYNTAXID) values ('6', '1');

/* GET */
insert into SKILL (SKILLID, SKILLNAME, SKILLDES, SKILLFAILMSG) values ('7', 'get', 'Picking up objects.', 'You grab at the air, did you get it?');
insert into SKILLTABLE (SKILLBOOKID, SKILLID) values(1, 7);
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, TARGETWHO, TARGETWHERE) values ('19', 'GET', '1', 'SELF', 'HERE');
insert into blocktable (SKILLID, BLOCKID) values('7', '19');
insert into syntax (SYNTAXPOS, SYNTAXTYPE) values ('1', 'ITEM');
insert into syntaxtable (SKILLID, SYNTAXID) values ('7', '2');
insert into syntaxtable (SKILLID, SYNTAXID) values ('7', '4');

/* DROP */
insert into SKILL (SKILLID, SKILLNAME, SKILLDES, SKILLFAILMSG) values ('10', 'drop', 'Dropping objects', 'You open your hands and let the air drift away...');
insert into SKILLTABLE (SKILLBOOKID, SKILLID) values(1, 10);
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, TARGETWHO, TARGETWHERE, ENDWHERE) values ('26', 'DROP', '1', 'SELF', 'HERE', 'HERE');
insert into blocktable (SKILLID, BLOCKID) values('10', '26');
insert into syntaxtable (SKILLID, SYNTAXID) values ('10', '2');
insert into syntaxtable (SKILLID, SYNTAXID) values ('10', '4');

/* LOOK */
insert into SKILL (SKILLID, SKILLNAME, SKILLDES, SKILLFAILMSG) values ('8', 'look', 'Viewing your surroundings.', 'WHY CAN"T YOU SEE????');
insert into SKILLTABLE (SKILLBOOKID, SKILLID) values(1, 8);
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, TARGETWHERE) values ('20', 'LOOK', '1', 'ONEAWAYPROJECTILE');
insert into blocktable (SKILLID, BLOCKID) values ('8', '20');
insert into syntax (SYNTAXPOS, SYNTAXTYPE) values ('2', 'DIRECTION');
insert into syntaxtable (SKILLID, SYNTAXID) values ('8', '1');
insert into syntaxtable (SKILLID, SYNTAXID) values ('8', '5');

/* MOVE */
insert into SKILL (SKILLID, SKILLNAME, SKILLDES, SKILLFAILMSG) values ('9', 'move', 'One foot in front of the other.', 'You cannot move! Take the time to enjoy your surroundings.');
insert into SKILLTABLE (SKILLBOOKID, SKILLID) values(1, 9);
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, GROUNDTYPE, TARGETWHO, TARGETWHERE, ENDWHERE) values ('21', 'MOVECHECK', '2', 'GROUND', 'SELF', 'HERE', 'ONEAWAYPROJECTILE');
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, STRINGONE, TARGETWHO, TARGETWHERE) values('22', 'MESSAGE', '3', '%s leaves to the %s.', 'ALL', 'HERE');
insert into msgstringstable (BLOCKID, MSGSTRINGSID) values ('22', '3');
insert into msgstringstable (BLOCKID, MSGSTRINGSID) values ('22', '7');
insert into msgstrings (MSGSTRINGSID, MSGSTRINGSPOS, MSGSTRINGSTYPE) values ('7', '2', 'MOVE');
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, STRINGONE, TARGETWHO, TARGETWHERE) values('23', 'MESSAGE', '5', '%s enters from the %s.', 'ALL', 'HERE');
insert into msgstringstable (BLOCKID, MSGSTRINGSID) values ('23', '3');
insert into msgstringstable (BLOCKID, MSGSTRINGSID) values ('23', '9');
insert into msgstrings (MSGSTRINGSID, MSGSTRINGSPOS, MSGSTRINGSTYPE) values ('9', '2', 'OPPMOVE');
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, TARGETWHO, TARGETWHERE, ENDWHERE) values ('24', 'MOVE', '4', 'SELF', 'HERE', 'ONEAWAYPROJECTILE');
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, BOOLEANONE, TARGETWHO, TARGETWHERE) values ('25', 'BALANCECHECK', '1', 'TRUE', 'SELF', 'HERE');
insert into blocktable (SKILLID, BLOCKID) values ('9', '25');
insert into blocktable (SKILLID, BLOCKID) values ('9', '21');
insert into blocktable (SKILLID, BLOCKID) values ('9', '22');
insert into blocktable (SKILLID, BLOCKID) values ('9', '24');
insert into blocktable (SKILLID, BLOCKID) values ('9', '23');
insert into syntax (SYNTAXPOS, SYNTAXTYPE) values ('1', 'DIRECTION');
insert into syntaxtable (SKILLID, SYNTAXID) values ('9', '6');

/* WIELD */
insert into SKILL (SKILLID, SKILLNAME, SKILLDES, SKILLFAILMSG) values (11, 'wield', 'The act of readying something in your hands.', 'You cannot wield that.');
insert into SKILLTABLE (SKILLBOOKID, SKILLID) values(1, 11);
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, TARGETWHO, TARGETWHERE, BOOLEANONE) values ('28', 'EQUIPCHANGE', 2, 'SELF', 'HERE', 'TRUE');
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, STRINGONE, TARGETWHO, TARGETWHERE) values('27', 'MESSAGE', 3, '%s wields a %s.', 'OTHERS', 'HERE');
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, STRINGONE, TARGETWHO, TARGETWHERE) values('29', 'MESSAGE', 4, 'You wield a %s.', 'SELF', 'HERE');
insert into blocktable (SKILLID, BLOCKID) values (11, '25');
insert into blocktable (SKILLID, BLOCKID) values (11, '28');
insert into blocktable (SKILLID, BLOCKID) values (11, '27');
insert into msgstringstable (BLOCKID, MSGSTRINGSID) values ('27', '10');
insert into msgstringstable (BLOCKID, MSGSTRINGSID) values ('27', '3');
insert into msgstringstable (BLOCKID, MSGSTRINGSID) values ('29', '3');
insert into msgstrings (MSGSTRINGSPOS, MSGSTRINGSTYPE) values (2, 'ITEM');
insert into syntax (SYNTAXPOS, SYNTAXTYPE) values (3, 'SLOT');
insert into syntaxtable (SKILLID, SYNTAXID) values (11, 4);
insert into syntaxtable (SKILLID, SYNTAXID) values (11, 2);
insert into syntaxtable (SKILLID, SYNTAXID) values (11, 7);

insert into SKILL (SKILLID, SKILLNAME, SKILLDES, SKILLFAILMSG) values (12, 'godcreate', 'Designing almost anything', 'You do not have the permissions of a god.');
insert into SKILLTABLE (SKILLBOOKID, SKILLID) values(1, 12);
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS) values (30, 'GODCREATE', 1);
insert into blocktable (SKILLID, BLOCKID) values (12, 30);
insert into syntaxtable (SKILLID, SYNTAXID) values (12, 2);
/*


	LOTS of new blocks we need to make
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, BOOLEANONE, TARGETWHO, TARGETWHERE) values('5', 'BALANCECOST', '1', 'FALSE', 'SELF', 'HERE');
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, INTVALUE, TARGETWHO, TARGETWHERE) values('6', 'DAMAGE', '2', '10', 'TARGET', 'HERE');
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, INTVALUE, TARGETWHO, TARGETWHERE) values('7', 'BLEEDEFFECT', '3', '10', 'TARGET', 'HERE');
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, INTVALUE, TARGETWHO, TARGETWHERE) values('8', 'DAMAGE', '0', '-15', 'SELF', 'HERE');
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, BLOCKPOINTERONE) values('9', 'CHANCE', '8');
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, WEAPONTYPE, TARGETWHO, TARGETWHERE) values('10', 'WEAPONEQUIPPEDCHECK', 'SHARP', 'TARGET', 'HERE');
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, STRINGONE, TARGETWHO, TARGETWHERE) values('11', 'MESSAGE', 'You make a sharp slash at %s and then %s turns and fights back.', 'TARGET', 'HERE');
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, STRINGONE, TARGETWHO, TARGETWHERE) values('12', 'MESSAGE', 'You watch as %s slashes horribly at %s and %s turns to fight back.', 'OTHERS', 'HERE');
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, STRINGONE, TARGETWHO, TARGETWHERE) values('13', 'MESSAGE', '%s slashes you painfully.', 'TARGET', 'HERE');
	blocktable needs one for most of the blocks above, except for the damage from chance
insert into blocktable (SKILLID, BLOCKID) values('3', '5');
insert into blocktable (SKILLID, BLOCKID) values('3', '6');
insert into blocktable (SKILLID, BLOCKID) values('3', '7');
insert into blocktable (SKILLID, BLOCKID) values('3', '9');
insert into blocktable (SKILLID, BLOCKID) values('3', '10');
insert into blocktable (SKILLID, BLOCKID) values('3', '11');
insert into blocktable (SKILLID, BLOCKID) values('3', '12');
insert into blocktable (SKILLID, BLOCKID) values('3', '13');
	To set up the skill
insert into SKILL (SKILLID, SKILLNAME, SKILLDES, SKILLFAILMSG, SKILLTYPE) values('3', 'gash', 'A horrible gashing slash, that for some reason heals sometimes...?', 'Your clusmy gash misses.', 'SHARP');
	To set up skill types:
insert into SKILLTABLE (MOBID, SKILLID) values('3', '3');
insert into skilltypetable (SKILLID, SKILLTYPEID) values ('3', '1');
insert into skilltype (SKILLTYPE) values ('SHARP');
	To set up syntax
	So, syntax for the skill is the same as others 
insert into syntaxtable (SKILLID, SYNTAXID) values ('2', '1');
insert into syntaxtable (SKILLID, SYNTAXID) values ('2', '2');
	No new syntax needed, as we've already made those 2.
	And then giving our mob this new skill via skilltable
insert into SKILLTABLE (MOBID, SKILLID) values('3', '3');
	I had to go through CREATETABLES to update enums with new possibilites, and add things for blocks with new information types.
	After this, integrate with code:
	


	skillBuild.setup(this, "slash");		
		skillBuild.addAction(new BalanceCost(false, Who.SELF, Where.HERE));		
		skillBuild.addAction(new Damage(10, Who.TARGET, Where.HERE));		
		skillBuild.addAction(new BleedEffect(10, Who.TARGET, Where.HERE));		
		skillBuild.addAction(new Chance(10, new Damage(-15, Who.SELF, Where.HERE)));		
		skillBuild.addType(Type.SHARP);
		skillBuild.addAction(new WeaponEquippedCheck(Type.SHARP, Who.SELF, Where.HERE));		
		skillBuild.addAction(new Message("You make a sharp slash at %s and then %s turns and fights back.", Who.TARGET, Where.HERE, msgStrings.TARGET, msgStrings.TARGET));
		skillBuild.addAction(new Message("You watch as %s slashes horribly at %s and %s turns to fight back.", Who.OTHERS, Where.HERE, msgStrings.SELF, msgStrings.TARGET, msgStrings.TARGET)); 
		skillBuild.addAction(new Message("%s slashes you painfully.", Who.TARGET, Where.HERE, msgStrings.SELF));	
		skillBuild.setFailMsg("You fail to slash.");
		skillBuild.setSyntax(Syntax.SKILL, Syntax.TARGET);


	To Build 'getbalance":
insert into SKILL (SKILLID, SKILLNAME, SKILLDES, SKILLFAILMSG) values('4', 'getbalance', 'It gives you back balance instantly.', 'You probably already have balance.');
insert into SKILLTABLE (MOBID, SKILLID) values('3', '4');
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, BOOLEANONE, TARGETWHO, TARGETWHERE) values('14', 'BALANCECOST', '1', 'TRUE', 'SELF', 'HERE');
insert into blocktable (SKILLID, BLOCKID) values('4', '14');

		skillBuild.setup(this, "getbalance");
		skillBuild.addAction(new BalanceCost(true, Who.SELF, Where.HERE));
		skillBuild.complete(skillBook);


		
		skillBuild.setup(this, "bleeddefence");
		skillBuild.addAction(new DefenceEffect(500, Type.BLEED, Who.SELF, Where.HERE));
		skillBuild.complete(skillBook);
		
		skillBuild.setup(this, "sharpdefence");
		skillBuild.addAction(new DefenceEffect(500, Type.SHARP, Who.SELF, Where.HERE));
		skillBuild.complete(skillBook);
		
To build "say":
insert into SKILL (SKILLID, SKILLNAME, SKILLDES, SKILLFAILMSG) values('5', 'say', 'Speaking to those nearby.', 'You emit no noise.');
insert into SKILLTABLE (MOBID, SKILLID) values('3', '5');
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS) values('15', 'SAY', '1');
insert into blocktable (SKILLID, BLOCKID) values('5', '15');
insert into syntax (SYNTAXPOS, SYNTAXTYPE) values ('2', 'LIST');
insert into syntaxtable (SKILLID, SYNTAXID) values ('5', '3');
insert into syntaxtable (SKILLID, SYNTAXID) values ('5', '1');

		skillBuild.setup(this, "say");
		skillBuild.addAction(new Say());
		skillBuild.setSyntax(Syntax.SKILL, Syntax.LIST);
		skillBuild.complete(skillBook);

To build "examine": THIS USED "OR"
insert into SKILL (SKILLID, SKILLNAME, SKILLDES, SKILLFAILMSG) values('6', 'examine', 'Taking a closer look at objects', 'Examining the air is difficult.');
insert into SKILLTABLE (MOBID, SKILLID) values('3', '6');
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS) values('16', 'EXAMINE', '1');
insert into blocktable (SKILLID, BLOCKID) values('6', '16');
insert into syntaxtable (SKILLID, SYNTAXID) values ('6', '2');
insert into syntaxtable (SKILLID, SYNTAXID) values ('6', '1');
		
		skillBuild.setup(this, "examine");
		skillBuild.addAction(new Or(new Examine(Where.INVENTORY), new Examine(Where.HERE)));
		skillBuild.setSyntax(Syntax.SKILL, Syntax.ITEM);
		skillBuild.complete(skillBook);
		
To build "get": Well, kind of need items to be working...
insert into SKILL (SKILLID, SKILLNAME, SKILLDES, SKILLFAILMSG) values ('7', 'get', 'Picking up objects.', 'You grab at the air, did you get it?');
insert into SKILLTABLE (MOBID, SKILLID) values('3', '7');
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, TARGETWHO, TARGETWHERE) values ('19', 'GET', '1', 'SELF', 'HERE');
insert into blocktable (SKILLID, BLOCKID) values('7', '19');
insert into syntax (SYNTAXPOS, SYNTAXTYPE) values ('2', 'ITEM');
insert into syntaxtable (SKILLID, SYNTAXID) values ('7', '1');
insert into syntaxtable (SKILLID, SYNTAXID) values ('7', '4');


		skillBuild.setup(this, "get");
		skillBuild.addAction(new Get(Who.SELF, Where.HERE));
		skillBuild.setSyntax(Syntax.SKILL, Syntax.ITEM);
		skillBuild.complete(skillBook);
		
To build "drop": Well, kind of need items to be working...
insert into SKILL (SKILLID, SKILLNAME, SKILLDES, SKILLFAILMSG) values ('10', 'drop', 'Dropping objects', 'You open your hands and let the air drift away...');
insert into SKILLTABLE (MOBID, SKILLID) values('3', '10');
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, TARGETWHO, TARGETWHERE) values ('26', 'DROP', '1', 'SELF', 'HERE');
insert into blocktable (SKILLID, BLOCKID) values('10', '26');
insert into syntaxtable (SKILLID, SYNTAXID) values ('10', '1');
insert into syntaxtable (SKILLID, SYNTAXID) values ('10', '4');

		skillBuild.setup(this, "drop");
		skillBuild.addAction(new Drop(Who.SELF, Where.HERE, Where.HERE));
		skillBuild.setSyntax(Syntax.SKILL, Syntax.ITEM);
		skillBuild.complete(skillBook);
		
To build "look":
insert into SKILL (SKILLID, SKILLNAME, SKILLDES, SKILLFAILMSG) values ('8', 'look', 'Viewing your surroundings.', 'WHY CAN"T YOU SEE????');
insert into SKILLTABLE (MOBID, SKILLID) values ('3', '8');
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, TARGETWHERE) values ('20', 'LOOK', '1', 'ONEAWAY');
insert into blocktable (SKILLID, BLOCKID) values ('8', '20');
insert into syntax (SYNTAXPOS, SYNTAXTYPE) values ('2', 'DIRECTION');
insert into syntaxtable (SKILLID, SYNTAXID) values ('8', '1');
insert into syntaxtable (SKILLID, SYNTAXID) values ('8', '5');

		skillBuild.setup(this, "look");
		skillBuild.addAction(new Look(Where.ONEAWAY));
		skillBuild.setSyntax(Syntax.SKILL, Syntax.DIRECTION);
		skillBuild.complete(skillBook);
		
To build "move":
insert into SKILL (SKILLID, SKILLNAME, SKILLDES, SKILLFAILMSG) values ('9', 'move', 'One foot in front of the other.', 'You can't move! Take the time to enjoy your surroundings.');
insert into SKILLTABLE (MOBID, SKILLID) values ('3', '9');
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, GROUNDTYPEID, TARGETWHO, TARGETWHERE, ENDWHERE) values ('21', 'MOVECHECK', '2', 'GROUND', 'SELF', 'HERE', 'ONEAWAY');
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, STRINGONE, TARGETWHO, TARGETWHERE) values('22', 'MESSAGE', '3', '%s leaves to the %s.', 'ALL', 'HERE');
insert into msgstringstable (BLOCKID, MSGSTRINGSID) values ('22', '3');
insert into msgstringstable (BLOCKID, MSGSTRINGSID) values ('22', '7');
insert into msgstrings (MSGSTRINGSID, MSGSTRINGSPOS, MSGSTRINGSTYPE) values ('7', '2', 'MOVE');
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, STRINGONE, TARGETWHO, TARGETWHERE) values('23', 'MESSAGE', '5', '%s enters from the %s.', 'ALL', 'HERE');
insert into msgstringstable (BLOCKID, MSGSTRINGSID) values ('23', '3');
insert into msgstringstable (BLOCKID, MSGSTRINGSID) values ('23', '9');
insert into msgstrings (MSGSTRINGSID, MSGSTRINGSPOS, MSGSTRINGSTYPE) values ('9', '2', 'OPPMOVE');
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, TARGETWHO, TARGETWHERE, ENDWHERE) values ('24', 'MOVE', '4', 'SELF', 'HERE', 'ONEAWAY');
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, BOOLEANONE, TARGETWHO, TARGETWHERE) values ('25', 'BALANCECHECK', '1', 'TRUE', 'SELF', 'HERE');
insert into blocktable (SKILLID, BLOCKID) values ('9', '25');
insert into blocktable (SKILLID, BLOCKID) values ('9', '21');
insert into blocktable (SKILLID, BLOCKID) values ('9', '22');
insert into blocktable (SKILLID, BLOCKID) values ('9', '24');
insert into blocktable (SKILLID, BLOCKID) values ('9', '23');
insert into syntax (SYNTAXPOS, SYNTAXTYPE) values ('1', 'DIRECTION');
insert into syntaxtable (SKILLID, SYNTAXID) values ('9', '6');


		skillBuild.setup(this, "move");
		skillBuild.addAction(new BalanceCheck(true, Who.SELF, Where.HERE));
		skillBuild.addAction(new MoveCheck(GroundType.GROUND, Who.SELF, Where.HERE, Where.ONEAWAY));
		skillBuild.addAction(new Message("%s leaves to the %s.", Who.ALL, Where.HERE, msgStrings.SELF, msgStrings.MOVE));
		skillBuild.addAction(new Move(Who.SELF, Where.HERE, Where.ONEAWAY));
		skillBuild.addAction(new Message("%s enters from the %s.", Who.ALL, Where.HERE, msgStrings.SELF, msgStrings.OPPMOVE));
		skillBuild.setFailMsg("You cannot walk that way.");
		skillBuild.setSyntax(Syntax.DIRECTION);
		skillBuild.complete(skillBook);
		
		skillBuild.setup(this, "swim");
		skillBuild.addAction(new BalanceCheck(true, Who.SELF, Where.HERE));
		skillBuild.addAction(new MoveCheck(GroundType.WATER, Who.SELF, Where.HERE, Where.ONEAWAY));
		skillBuild.addAction(new Message("%s swims to the %s.", Who.ALL, Where.HERE, msgStrings.SELF, msgStrings.MOVE));
		skillBuild.addAction(new Move(Who.SELF, Where.HERE, Where.ONEAWAY));
		skillBuild.addAction(new Message("%s swims in from the %s.", Who.ALL, Where.HERE, msgStrings.SELF, msgStrings.OPPMOVE));
		skillBuild.setFailMsg("You cannot swim that way.");
		skillBuild.setSyntax(Syntax.DIRECTION);
		skillBuild.complete(skillBook);
*/
