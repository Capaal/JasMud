set foreign_key_checks=0;

Create table LOCATIONSTATS (
	LOCID integer unsigned not null AUTO_INCREMENT,
	LOCNAME varchar(50) not null,
	LOCDES text null,
	LOCTYPE ENUM('GROUND', 'WATER', 'AIR', 'CONTAINTER') NOT NULL DEFAULT 'GROUND',
	LOCNORTH integer unsigned null,
	LOCNORTHDIR ENUM('NORTH', 'NORTHEAST', 'EAST', 'SOUTHEAST', 'SOUTH', 'SOUTHWEST', 'WEST',
		'NORTHWEST', 'UP', 'DOWN', 'IN', 'OUT') null,
	LOCSOUTH integer unsigned null,
	LOCSOUTHDIR ENUM('NORTH', 'NORTHEAST', 'EAST', 'SOUTHEAST', 'SOUTH', 'SOUTHWEST', 'WEST',
		'NORTHWEST', 'UP', 'DOWN', 'IN', 'OUT') null,
	LOCEAST integer unsigned null,
	LOCEASTDIR ENUM('NORTH', 'NORTHEAST', 'EAST', 'SOUTHEAST', 'SOUTH', 'SOUTHWEST', 'WEST',
		'NORTHWEST', 'UP', 'DOWN', 'IN', 'OUT') null,
	LOCWEST integer unsigned null,
	LOCWESTDIR ENUM('NORTH', 'NORTHEAST', 'EAST', 'SOUTHEAST', 'SOUTH', 'SOUTHWEST', 'WEST',
		'NORTHWEST', 'UP', 'DOWN', 'IN', 'OUT') null,
	LOCNORTHEAST integer unsigned null,
	LOCNORTHEASTDIR ENUM('NORTH', 'NORTHEAST', 'EAST', 'SOUTHEAST', 'SOUTH', 'SOUTHWEST', 'WEST',
		'NORTHWEST', 'UP', 'DOWN', 'IN', 'OUT') null,
	LOCSOUTHEAST integer unsigned null,
	LOCSOUTHEASTDIR ENUM('NORTH', 'NORTHEAST', 'EAST', 'SOUTHEAST', 'SOUTH', 'SOUTHWEST', 'WEST',
		'NORTHWEST', 'UP', 'DOWN', 'IN', 'OUT') null,
	LOCSOUTHWEST integer unsigned null,
	LOCSOUTHWESTDIR ENUM('NORTH', 'NORTHEAST', 'EAST', 'SOUTHEAST', 'SOUTH', 'SOUTHWEST', 'WEST',
		'NORTHWEST', 'UP', 'DOWN', 'IN', 'OUT') null,
	LOCNORTHWEST integer unsigned null,
	LOCNORTHWESTDIR ENUM('NORTH', 'NORTHEAST', 'EAST', 'SOUTHEAST', 'SOUTH', 'SOUTHWEST', 'WEST',
		'NORTHWEST', 'UP', 'DOWN', 'IN', 'OUT') null,
	LOCUP integer unsigned null,
	LOCUPDIR ENUM('NORTH', 'NORTHEAST', 'EAST', 'SOUTHEAST', 'SOUTH', 'SOUTHWEST', 'WEST',
		'NORTHWEST', 'UP', 'DOWN', 'IN', 'OUT') null,
	LOCDOWN integer unsigned null,
	LOCDOWNDIR ENUM('NORTH', 'NORTHEAST', 'EAST', 'SOUTHEAST', 'SOUTH', 'SOUTHWEST', 'WEST',
		'NORTHWEST', 'UP', 'DOWN', 'IN', 'OUT') null,
	LOCOUT integer unsigned null,
	LOCOUTDIR ENUM('NORTH', 'NORTHEAST', 'EAST', 'SOUTHEAST', 'SOUTH', 'SOUTHWEST', 'WEST',
		'NORTHWEST', 'UP', 'DOWN', 'IN', 'OUT') null,
	LOCIN integer unsigned null,
	LOCINDIR ENUM('NORTH', 'NORTHEAST', 'EAST', 'SOUTHEAST', 'SOUTH', 'SOUTHWEST', 'WEST',
		'NORTHWEST', 'UP', 'DOWN', 'IN', 'OUT') null,		
	PRIMARY KEY (LOCID),
	index (LOCNORTH),
	index (LOCSOUTH),
	index (LOCEAST),
	index (LOCWEST),
	index (LOCNORTHEAST),
	index (LOCSOUTHEAST),
	index (LOCSOUTHWEST),
	index (LOCNORTHWEST),
	index (LOCIN),
	index (LOCOUT),
	index (LOCUP),
	index (LOCDOWN),
	FOREIGN KEY (LOCNORTH) REFERENCES LOCATIONSTATS (LOCID),
	FOREIGN KEY (LOCEAST) REFERENCES LOCATIONSTATS (LOCID),
	FOREIGN KEY (LOCSOUTH) REFERENCES LOCATIONSTATS (LOCID),
	FOREIGN KEY (LOCWEST) REFERENCES LOCATIONSTATS (LOCID),
	FOREIGN KEY (LOCNORTHEAST) REFERENCES LOCATIONSTATS (LOCID),
	FOREIGN KEY (LOCSOUTHEAST) REFERENCES LOCATIONSTATS (LOCID),
	FOREIGN KEY (LOCSOUTHWEST) REFERENCES LOCATIONSTATS (LOCID),
	FOREIGN KEY (LOCNORTHWEST) REFERENCES LOCATIONSTATS (LOCID),
	FOREIGN KEY (LOCUP) REFERENCES LOCATIONSTATS (LOCID),
	FOREIGN KEY (LOCDOWN) REFERENCES LOCATIONSTATS (LOCID),
	FOREIGN KEY (LOCIN) REFERENCES LOCATIONSTATS (LOCID),
	FOREIGN KEY (LOCOUT) REFERENCES LOCATIONSTATS (LOCID)
);

Create table ITEMSTATS (
	ITEMID integer unsigned not null AUTO_INCREMENT,
	ITEMNAME varchar(50) not null DEFAULT 'Default Name',
	ITEMPHYS decimal(5,2) unsigned not null DEFAULT 1.0,
	ITEMBAL decimal(5,2) unsigned not null DEFAULT 1.0,
	ITEMDESC text not null,
	ITEMSHORTD varchar (50) not null DEFAULT 'a default',
	ITEMMAXDUR integer unsigned not null DEFAULT 1,
	ITEMCURDUR integer unsigned not null DEFAULT 1,
	ITEMLOC integer unsigned not null DEFAULT 1,
	EQUIPSLOT ENUM('HEAD', 'NECK', 'LEFTEAR', 'RIGHTEAR', 'LEFTHAND', 'RIGHTHAND', 'LEFTFINGER', 'RIGHTFINGER', 'CHEST', 'LEGS', 'FEET') DEFAULT null,
	/* The below enum might have pouch and/or pack? */
	ITEMLOCTYPE ENUM('LOCATION', 'INVENTORY', 'CONTAINER', 'EQUIPMENT') not null DEFAULT 'LOCATION',
	PRIMARY KEY (ITEMID, ITEMNAME),
	index (ITEMLOC) 
);

Create table SLOTTABLE (
	/*SLOTTABLEID integer unsigned not null auto_increment,*/
	ITEMID integer unsigned not null,
	SLOTID integer unsigned not null,
	PRIMARY KEY (ITEMID, SLOTID),
	index (ITEMID),
	index (SLOTID),
	FOREIGN KEY (ITEMID) REFERENCES ITEMSTATS (ITEMID),
	FOREIGN KEY (SLOTID) REFERENCES SLOT (SLOTID)
);

Create table SLOT (
	SLOTID integer unsigned not null auto_increment,
	SLOT ENUM('HEAD', 'NECK', 'LEFTEAR', 'RIGHTEAR', 'LEFTHAND', 'RIGHTHAND', 'LEFTFINGER', 'RIGHTFINGER', 'CHEST', 'LEGS', 'FEET'),
	PRIMARY KEY (SLOTID)
);
/*
Create table LOCINV (
	LOCID integer unsigned not null,
	ITEMID integer unsigned not null,
	PRIMARY KEY (LOCID, ITEMID),
	index (ITEMID),
	FOREIGN KEY (ITEMID) REFERENCES ITEMSTATS (ITEMID),
	FOREIGN KEY (LOCID) REFERENCES LOCATIONSTATS (LOCID)
);*/

Create table MOBSTATS (
	MOBID integer unsigned not null AUTO_INCREMENT,
	MOBNAME varchar(50) not null,
	MOBPASS varchar(40) not null,
	MOBDESC text null,
	MOBSHORTD varchar (50) null,
	MOBLOC integer unsigned not null default 1,
	MOBTYPE ENUM('STDMOB') DEFAULT 'STDMOB',
	
/*	MOBMAXHP integer unsigned not null default 100, Probably dependant on level */
	MOBCURRENTHP integer unsigned not null default 100,
	MOBDEAD boolean default 0,
	MOBXPWORTH integer unsigned not null default 1,
	MOBCURRENTXP integer unsigned not null default 1,
	MOBCURRENTLEVEL integer unsigned not null default 1,
	MOBAGE integer unsigned not null default 0,
	/* Messages and Bugs need their own tables, and probably current effects as well? */

	LOADONSTARTUP boolean not null DEFAULT 1,
	PRIMARY KEY (MOBID),
	index (MOBLOC),
	FOREIGN KEY (MOBLOC) REFERENCES LOCATIONSTATS (LOCID)
);
/*
Create table MOBINV (
	MOBID integer unsigned not null,
	ITEMID integer unsigned not null,
	PRIMARY KEY (MOBID, ITEMID),
	index (ITEMID),
	FOREIGN KEY (ITEMID) REFERENCES ITEMSTATS (ITEMID),
	FOREIGN KEY (MOBID) REFERENCES MOBSTATS (MOBID)
);*/

Create table SKILLBOOKTABLE (
	/*SKILLBOOKTABLEID integer unsigned not null auto_increment,*/
	MOBID integer unsigned not null,
	SKILLBOOKID integer unsigned not null,
	MOBPROGRESS integer unsigned not null,
	PRIMARY KEY (MOBID, SKILLBOOKID),
	index (MOBID),
	index (SKILLBOOKID),
	FOREIGN KEY (MOBID) REFERENCES MOBSTATS (MOBID),
	FOREIGN KEY (SKILLBOOKID) REFERENCES SKILLBOOK (SKILLBOOKID)
);

Create table SKILLBOOK (
	SKILLBOOKID integer unsigned not null auto_increment,
	SKILLBOOKNAME varchar(50) not null,
	PRIMARY KEY (SKILLBOOKID)
);

Create table SKILLTABLE (
	/*SKILLTABLEID integer unsigned not null AUTO_INCREMENT,*/
	SKILLBOOKID integer unsigned not null,
	SKILLID integer unsigned not null,
	PRIMARY KEY (SKILLBOOKID, SKILLID),
	index (SKILLBOOKID),
	index (SKILLID),
	FOREIGN KEY (SKILLBOOKID) REFERENCES SKILLBOOK (SKILLBOOKID),
	FOREIGN KEY (SKILLID) REFERENCES SKILL (SKILLID)
);

Create table SKILL (
	SKILLID integer unsigned not null auto_increment,
	SKILLNAME varchar(50) not null,
	SKILLDES text,	
	SKILLFAILMSG text,
	PRIMARY KEY (SKILLID)
);

Create table SKILLTYPETABLE (
	/*SKILLTYPETABLEID integer unsigned not null auto_increment,*/
	SKILLID integer unsigned not null,
	TYPEID integer unsigned not null,
	PRIMARY KEY (SKILLID, TYPEID),
	index (SKILLID),
	index (TYPEID),
	FOREIGN KEY (SKILLID) REFERENCES SKILL (SKILLID),
	FOREIGN KEY (TYPEID) REFERENCES TYPE (TYPEID) 
);

Create table ITEMTYPETABLE (
	/*ITEMTYPETABLEID integer unsigned not null auto_increment,*/
	ITEMID integer unsigned not null,
	TYPEID integer unsigned not null,
	PRIMARY KEY (ITEMID, TYPEID),
	index (ITEMID),
	index (TYPEID),
	FOREIGN KEY (ITEMID) REFERENCES ITEM (ITEMID),
	FOREIGN KEY (TYPEID) REFERENCES TYPE (TYPEID) 
);


Create table TYPE (
	TYPEID integer unsigned not null auto_increment,
	TYPE ENUM('SHARP', 'FIRE', 'PIERCE'),
	PRIMARY KEY(TYPEID)
);

Create table SYNTAXTABLE (
/*	SYNTAXTABLEID integer unsigned not null auto_increment,*/
	SKILLID integer unsigned not null,
	SYNTAXID integer unsigned not null,
	PRIMARY KEY (SKILLID, SYNTAXID),
	index (SKILLID),
	index (SYNTAXID),
	FOREIGN KEY (SKILLID) REFERENCES SKILL (SKILLID),
	FOREIGN KEY (SYNTAXID) REFERENCES SYNTAX (SYNTAXID)
);

Create table SYNTAX (
	SYNTAXID integer unsigned not null AUTO_INCREMENT,
	SYNTAXPOS integer unsigned not null,
	SYNTAXTYPE ENUM('SKILL' , 'TARGET', 'ITEM', 'LIST', 'DIRECTION', 'SLOT') not null,
	PRIMARY KEY(SYNTAXID)
);

Create table BLOCKTABLE (
	/*BLOCKTABLEID integer unsigned not null AUTO_INCREMENT,*/
	SKILLID integer unsigned not null,
	BLOCKID integer unsigned not null,
	PRIMARY KEY (SKILLID, BLOCKID),
	index (SKILLID),
	index (BLOCKID),
	FOREIGN KEY (SKILLID) REFERENCES SKILL (SKILLID),
	FOREIGN KEY (BLOCKID) REFERENCES BLOCK (BLOCKID)
);

Create table BLOCK (
	BLOCKID integer unsigned AUTO_INCREMENT,
	BLOCKTYPE ENUM('DAMAGE', 'BALANCECOST', 'CHANCE', 'WEAPONEQUIPPEDCHECK', 'BLEEDEFFECT', 'MESSAGE', 'SAY', 'EXAMINE', 'OR', 'GET', 'LOOK',
		'MOVE', 'MOVECHECK', 'BALANCECHECK', 'DROP', 'GODCREATE', 'EQUIPCHANGE') not null,
	BLOCKPOS integer unsigned not null default '0',
	INTVALUE integer,
	INTVALUETWO integer,
	BOOLEANONE ENUM('TRUE', 'FALSE'),
	BLOCKPOINTERONE integer unsigned,
	BLOCKPOINTERTWO integer unsigned,
	ITEMPOINTERONE integer unsigned,
	TYPE ENUM('SHARP', 'PIERCE') null,
	GROUNDTYPE ENUM('GROUND'),
	STRINGONE tinytext,
	TARGETWHO ENUM('SELF', 'ALL', 'TARGET', 'OTHERS', 'ALLIES', 'ENEMIES', 'LIST') not null,
	TARGETWHERE ENUM('HERE', 'TARGET', 'INVENTORY', 'ONEAWAY', 'PROJECTILE', 'ONEAWAYPROJECTILE') not null,
	ENDWHERE ENUM('HERE', 'TARGET', 'INVENTORY', 'ONEAWAY', 'PROJECTILE', 'ONEAWAYPROJECTILE') not null,
	PRIMARY KEY (BLOCKID),
	index (BLOCKPOINTERONE),
	index (BLOCKPOINTERTWO),
	index (ITEMPOINTERONE),
	FOREIGN KEY (BLOCKPOINTERONE) REFERENCES BLOCK (BLOCKID),
	FOREIGN KEY (BLOCKPOINTERTWO) REFERENCES BLOCK (BLOCKID),
	FOREIGN KEY (ITEMPOINTERONE) REFERENCES ITEMSTATS (ITEMID)
);

Create table MSGSTRINGSTABLE (
	/*MSGSTRINGSTABLEID integer unsigned auto_increment,*/
	BLOCKID integer unsigned not null,
	MSGSTRINGSID integer unsigned not null,
	PRIMARY KEY(BLOCKID, MSGSTRINGSID),
	index (BLOCKID),
	index (MSGSTRINGSID),
	FOREIGN KEY (BLOCKID) REFERENCES BLOCK (BLOCKID),
	FOREIGN KEY (MSGSTRINGSID) REFERENCES MSGSTRINGS (MSGSTRINGSID)
);

Create table MSGSTRINGS (
	MSGSTRINGSID integer unsigned auto_increment,
	MSGSTRINGSPOS integer unsigned,
	MSGSTRINGSTYPE ENUM('TARGET', 'SELF', 'MOVE', 'OPPMOVE', 'ITEM'),
	PRIMARY KEY(MSGSTRINGSID)
);

Create table SEQUENCETABLE (
	SEQUENCEID integer unsigned auto_Increment,
	PRIMARY KEY(SEQUENCEID)
);
