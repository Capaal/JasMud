insert into locationstats (LOCID, LOCNAME, LOCDES, LOCTYPE) values ('1','Beach','This is start.','land');
insert into locationstats (LOCID, LOCNAME, LOCDES, LOCTYPE) values ('2','Road north of beach','This is north of start.','land');
insert into locationstats (LOCID, LOCNAME, LOCDES, LOCTYPE) values ('3','Mud Intro Shack','This will be the mud intro shack.','land');
insert into locationstats (LOCID, LOCNAME, LOCDES, LOCTYPE) values ('4','East of road','This is east of the road.','land');
insert into locationstats (LOCID, LOCNAME, LOCDES, LOCTYPE) values ('5','Tidepool','This is south of start.','water');

update locationstats set locsouthx='1',locsouthxdir='north' where locid='2';
update locationstats set locsex='1',locsexdir='northwest' where locid='3';
update locationstats set locwestx='2',locwestxdir='east' where locid='4';
update locationstats set locnorthx='1',locnorthxdir='south' where locid='5';
select * from locationstats;

insert into itemstats values ('1','Dagger','1.10','Short and sharp.','a dagger','1','0.80','100','1','location'),
('2','Sword','1.20','Long and sharp.','a sword','1','0.90','150','2','location'),
('3','Vase','0.20',"It's a vase.",'a vase',null,'1.0','10','1','location'),
('4','Ring','0.20',"A small gold ring.",'a ring',null,'1.0','10','1','location'),
('5','Ring','0.20',"It goes on your head.",'a helmet',null,'1.0','10','1','location');
select * from itemstats;

insert into mobstats values ('1','James',"He is a farmer named James.",'a farmer','2');
insert into mobstats values ('2', 'Horse',"It's a horse.",'a horse','2');
select * from mobstats;

