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

insert into itemstats values ('1','Dagger','1.10','Short and sharp.','a dagger','1','0.80','100','1','location'),
('2','Sword','1.20','Long and sharp.','a sword','1','0.90','150','2','location'),
('3','Vase','0.20',"It's a vase.",'a vase',null,'1.0','10','1','location'),
('4','Ring','0.20',"A small gold ring.",'a ring',null,'1.0','10','1','location'),
('5','Ring','0.20',"It goes on your head.",'a helmet',null,'1.0','10','1','location');
select * from itemstats;

insert into mobstats values ('1','James', '', "He is a farmer named James.",'a farmer','1', 'STDMOB', true);
insert into mobstats values ('2', 'Horse', '', "It's a horse.",'a horse','1', 'STDMOB', true);
insert into mobstats values ('3','asdf', 'asdf', "James.",'a farmer','1', 'STDMOB', false);
select * from mobstats;


insert into SKILLTABLE (MOBID, SKILLID) values('3', '2');
insert into SKILLTABLE (MOBID, SKILLID) values('1', '2');
insert into SKILLTABLE (MOBID, SKILLID) values('3', '1');

insert into SKILL (SKILLID, SKILLDES, SKILLNAME, SKILLFAILMSG) values('1', '1', 'damage', 'FAIL');
insert into SKILL (SKILLID, SKILLDES, SKILLNAME, SKILLFAILMSG) values('2', 'blah', 'slash', 'FAIL');

insert into blocktable (SKILLID, BLOCKID) values('1', '1');
insert into blocktable (SKILLID, BLOCKID) values('1', '2');
insert into blocktable (SKILLID, BLOCKID) values('2', '2');
insert into blocktable (SKILLID, BLOCKID) values('2', '3');
insert into blocktable (SKILLID, BLOCKID) values('2', '4');

insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, INTVALUE, TARGETWHO, TARGETWHERE) values('1', 'DAMAGE', '1', '20', 'TARGET', 'HERE');
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, INTVALUE, TARGETWHO, TARGETWHERE) values('2', 'DAMAGE', '1', '5', 'TARGET', 'HERE');
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, INTVALUE, TARGETWHO, TARGETWHERE) values('3', 'DAMAGE', '2', '5', 'TARGET', 'HERE');
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, INTVALUE, TARGETWHO, TARGETWHERE) values('4', 'DAMAGE', '3', '5', 'TARGET', 'HERE');

insert into syntaxtable (SKILLID, SYNTAXID) values ('1', '1');
insert into syntaxtable (SKILLID, SYNTAXID) values ('1', '2');
insert into syntaxtable (SKILLID, SYNTAXID) values ('2', '1');
insert into syntaxtable (SKILLID, SYNTAXID) values ('2', '2');

insert into syntax (SYNTAXPOS, SYNTAXTYPE) values ('1', 'TARGET');
insert into syntax (SYNTAXPOS, SYNTAXTYPE) values ('0', 'SKILL');




insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, BOOLEANONE, TARGETWHO, TARGETWHERE) values('5', 'BALANCECOST', '1', 'FALSE', 'SELF', 'HERE');
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, INTVALUE, TARGETWHO, TARGETWHERE) values('6', 'DAMAGE', '2', '10', 'TARGET', 'HERE');
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, INTVALUE, TARGETWHO, TARGETWHERE) values('7', 'BLEEDEFFECT', '3', '10', 'TARGET', 'HERE');
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, INTVALUE, TARGETWHO, TARGETWHERE) values('8', 'DAMAGE', '0', '-15', 'SELF', 'HERE');
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, BLOCKPOINTER) values('9', 'CHANCE', '4', '8');
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, SKILLTYPEID, TARGETWHO, TARGETWHERE) values('10', 'WEAPONEQUIPPEDCHECK', '5', '1', 'TARGET', 'HERE');
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, STRINGONE, TARGETWHO, TARGETWHERE) values('11', 'MESSAGE', '6', 'You make a sharp slash at %s and then %s turns and fights back.', 'TARGET', 'HERE');
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
insert into SKILL (SKILLID, SKILLNAME, SKILLDES, SKILLFAILMSG) values('3', 'gash', 'A horrible gashing slash, that for some reason heals sometimes...?', 'Your clusmy gash misses.');
insert into SKILLTABLE (MOBID, SKILLID) values('3', '3');
insert into syntaxtable (SKILLID, SYNTAXID) values ('3', '1');
insert into syntaxtable (SKILLID, SYNTAXID) values ('3', '2');
insert into skilltypetable (SKILLID, SKILLTYPEID) values ('3', '1');
insert into skilltype (SKILLTYPE) values ('SHARP');
insert into msgstringstable (BLOCKID, MSGSTRINGSID) values ('11', '1');
insert into msgstringstable (BLOCKID, MSGSTRINGSID) values ('11', '2');
insert into msgstringstable (BLOCKID, MSGSTRINGSID) values ('12', '3');
insert into msgstringstable (BLOCKID, MSGSTRINGSID) values ('12', '4');
insert into msgstringstable (BLOCKID, MSGSTRINGSID) values ('12', '5');
insert into msgstringstable (BLOCKID, MSGSTRINGSID) values ('13', '3');
insert into msgstrings (MSGSTRINGSID, MSGSTRINGSPOS, MSGSTRINGSTYPE) values ('1', '1', 'TARGET');
insert into msgstrings (MSGSTRINGSID, MSGSTRINGSPOS, MSGSTRINGSTYPE) values ('2', '2', 'TARGET');
insert into msgstrings (MSGSTRINGSID, MSGSTRINGSPOS, MSGSTRINGSTYPE) values ('3', '1', 'SELF');
insert into msgstrings (MSGSTRINGSID, MSGSTRINGSPOS, MSGSTRINGSTYPE) values ('4', '2', 'TARGET');
insert into msgstrings (MSGSTRINGSID, MSGSTRINGSPOS, MSGSTRINGSTYPE) values ('5', '3', 'TARGET');

	



/*

	LOTS of new blocks we need to make
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, BOOLEANONE, TARGETWHO, TARGETWHERE) values('5', 'BALANCECOST', '1', 'FALSE', 'SELF', 'HERE');
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, INTVALUE, TARGETWHO, TARGETWHERE) values('6', 'DAMAGE', '2', '10', 'TARGET', 'HERE');
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, INTVALUE, TARGETWHO, TARGETWHERE) values('7', 'BLEEDEFFECT', '3', '10', 'TARGET', 'HERE');
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, INTVALUE, TARGETWHO, TARGETWHERE) values('8', 'DAMAGE', '0', '-15', 'SELF', 'HERE');
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS, BLOCKPOINTER) values('9', 'CHANCE', '8');
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
*/
