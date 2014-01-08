Create table LOCATIONSTATS (
	LOCID integer not null AUTO_INCREMENT,
	LOCNAME varchar(50) not null,
	LOCDES text null,
	LOCTYPE ENUM('GROUND', 'WATER', 'AIR', 'CONTAINTER') NOT NULL DEFAULT 'GROUND',
	LOCINV integer null,
	LOCNORTH integer null,
	LOCNORTHDIR ENUM('NORTH', 'NORTHEAST', 'EAST', 'SOUTHEAST', 'SOUTH', 'SOUTHWEST', 'WEST',
		'NORTHWEST', 'UP', 'DOWN', 'IN', 'OUT') null,
	LOCSOUTH integer null,
	LOCSOUTHDIR ENUM('NORTH', 'NORTHEAST', 'EAST', 'SOUTHEAST', 'SOUTH', 'SOUTHWEST', 'WEST',
		'NORTHWEST', 'UP', 'DOWN', 'IN', 'OUT') null,
	LOCEAST integer null,
	LOCEASTDIR ENUM('NORTH', 'NORTHEAST', 'EAST', 'SOUTHEAST', 'SOUTH', 'SOUTHWEST', 'WEST',
		'NORTHWEST', 'UP', 'DOWN', 'IN', 'OUT') null,
	LOCWEST integer null,
	LOCWESTDIR ENUM('NORTH', 'NORTHEAST', 'EAST', 'SOUTHEAST', 'SOUTH', 'SOUTHWEST', 'WEST',
		'NORTHWEST', 'UP', 'DOWN', 'IN', 'OUT') null,
	LOCNE integer null,
	LOCNEDIR ENUM('NORTH', 'NORTHEAST', 'EAST', 'SOUTHEAST', 'SOUTH', 'SOUTHWEST', 'WEST',
		'NORTHWEST', 'UP', 'DOWN', 'IN', 'OUT') null,
	LOCSE integer null,
	LOCSEDIR ENUM('NORTH', 'NORTHEAST', 'EAST', 'SOUTHEAST', 'SOUTH', 'SOUTHWEST', 'WEST',
		'NORTHWEST', 'UP', 'DOWN', 'IN', 'OUT') null,
	LOCSW integer null,
	LOCSWDIR ENUM('NORTH', 'NORTHEAST', 'EAST', 'SOUTHEAST', 'SOUTH', 'SOUTHWEST', 'WEST',
		'NORTHWEST', 'UP', 'DOWN', 'IN', 'OUT') null,
	LOCNW integer null,
	LOCNWDIR ENUM('NORTH', 'NORTHEAST', 'EAST', 'SOUTHEAST', 'SOUTH', 'SOUTHWEST', 'WEST',
		'NORTHWEST', 'UP', 'DOWN', 'IN', 'OUT') null,
	LOCUP integer null,
	LOCUPDIR ENUM('NORTH', 'NORTHEAST', 'EAST', 'SOUTHEAST', 'SOUTH', 'SOUTHWEST', 'WEST',
		'NORTHWEST', 'UP', 'DOWN', 'IN', 'OUT') null,
	LOCDOWN integer null,
	LOCDOWNDIR ENUM('NORTH', 'NORTHEAST', 'EAST', 'SOUTHEAST', 'SOUTH', 'SOUTHWEST', 'WEST',
		'NORTHWEST', 'UP', 'DOWN', 'IN', 'OUT') null,
	LOCOUT integer null,
	LOCOUTDIR ENUM('NORTH', 'NORTHEAST', 'EAST', 'SOUTHEAST', 'SOUTH', 'SOUTHWEST', 'WEST',
		'NORTHWEST', 'UP', 'DOWN', 'IN', 'OUT') null,
	LOCIN int null,
	LOCINDIR ENUM('NORTH', 'NORTHEAST', 'EAST', 'SOUTHEAST', 'SOUTH', 'SOUTHWEST', 'WEST',
		'NORTHWEST', 'UP', 'DOWN', 'IN', 'OUT') null,
	
	PRIMARY KEY (LOCID)
);

Create table LOCINV (
	LOCID integer not null AUTO_INCREMENT,
	ITEM1 integer null,
	ITEM2 integer null,
	ITEM3 integer null,
	ITEM4 integer null,
	ITEM5 integer null,
	ITEM6 integer null,
	ITEM7 integer null,
	ITEM8 integer null,
	ITEM9 integer null,
	ITEM10 integer null,
	ITEM11 integer null,
	ITEM12 integer null,
	ITEM13 integer null,
	ITEM14 integer null,
	ITEM15 integer null,
	ITEM16 integer null,
	PRIMARY KEY (LOCID)
);

Create table ITEMSTATS (
	ITEMID integer not null AUTO_INCREMENT,
	ITEMNAME varchar(50) not null,
	ITEMPHYS decimal(5,2) null,
	ITEMDESC text null,
	ITEMSHORTD varchar (50) null,
	ITEMCMDS integer null,
	ITEMBAL decimal(5,2) null,
	ITEMMAXCON integer null,
	ITEMLOC integer null,
	ITEMLOCTYPE varchar(20) null,
	PRIMARY KEY (ITEMID, ITEMNAME)
);

Create table ITEMCMDS (
	ITEMCMDS integer not null AUTO_INCREMENT,
	CMD1 integer null,
	CMD2 integer null,
	CMD3 integer null,
	CMD4 integer null,
	CMD5 integer null,
	CMD6 integer null,
	CMD7 integer null,
	CMD8 integer null,
	CMD9 integer null,
	CMD10 integer null,
	CMD11 integer null,
	CMD12 integer null,
	CMD13 integer null,
	PRIMARY KEY (ITEMCMDS)
);

Create table MOBSTATS (
	MOBID integer not null AUTO_INCREMENT,
	MOBNAME varchar(50) not null,
	MOBDESC text null,
	MOBSHORTD varchar (50) null,
	MOBLOC integer null,
	PRIMARY KEY (MOBID)
);

Create table SKILLTABLE (
	SKILLTABLEID integer unsigned not null AUTO_INCREMENT,
	MOBID integer unsigned not null,
	SKILLID integer unsigned not null,
	PRIMARY KEY (SKILLTABLEID)
);

Create table SKILL (
	SKILLID integer unsigned not null,
	SKILLDES text,
	SKILLNAME tinytext,
	SKILLFAILMSG text,
	PRIMARY KEY (SKILLID)
);

Create table BLOCKTABLE (
	BLOCKTABLEID integer unsigned not null AUTO_INCREMENT,
	SKILLID integer unsigned not null,
	BLOCKID integer unsigned not null,
	BLOCKTYPE ENUM('DAMAGE'),
	PRIMARY KEY (BLOCKTABLEID)
);

Create table DAMAGE (
	DAMAGEID integer unsigned not null,
	DAMAGEPOS integer unsigned not null,
	DAMAGEINTENSITY integer not null,
	DAMAGEWHO ENUM('SELF', 'ALL', 'TARGET', 'OTHERS', 'ALLIES', 'ENEMIES') not null,
	DAMAGEWHERE ENUM('HERE', 'TARGET', 'INVENTORY', 'ONEAWAY', 'PROJECTILE') not null,
	PRIMARY KEY (DAMAGEID)
)