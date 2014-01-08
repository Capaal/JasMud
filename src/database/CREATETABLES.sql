itemcmdsCreate table LOCATIONSTATS (
	LOCID integer not null AUTO_INCREMENT,
	LOCNAME varchar(50) not null,
	LOCDES text null,
	LOCTYPE ENUM 'GROUND', 'WATER', 'AIR' DEFAULT 'GROUND',
	LOCINV integer null,
	LOCNORTHX integer null,
	LOCNORTHXDIR varchar (20) null,
	LOCSOUTHX integer null,
	LOCSOUTHXDIR varchar (20) null,
	LOCEASTX integer null,
	LOCEASTXDIR varchar (20) null,
	LOCWESTX integer null,
	LOCWESTXDIR varchar (20) null,
	LOCNEX integer null,
	LOCNEXDIR varchar (20) null,
	LOCSEX integer null,
	LOCSEXDIR varchar (20) null,
	LOCSWX integer null,
	LOCSWXDIR varchar (20) null,
	LOCNWX integer null,
	LOCNWXDIR varchar (20) null,
	LOCUPX integer null,
	LOCUPXDIR varchar (20) null,
	LOCDOWNX integer null,
	LOCDOWNXDIR varchar (20) null,
	LOCOUTX integer null,
	LOCOUTXDIR varchar (20) null,
	LOCINX integer null,
	LOCINXDIR varchar (20) null,
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
)