insert into locationstats (LOCID, LOCNAME, LOCDES, LOCTYPE) values ('1','Beach','This is start.','GROUND');

insert into SKILLBOOK (SKILLBOOKID, SKILLBOOKNAME) values (1, 'Basic Skills');

insert into SKILL (SKILLID, SKILLNAME, SKILLDES, SKILLFAILMSG) values (12, 'godcreate', 'Designing almost anything', 'You do not have the permissions of a god.');
insert into SKILLTABLE (SKILLBOOKID, SKILLID) values(1, 12);
insert into block (BLOCKID, BLOCKTYPE, BLOCKPOS) values (30, 'GODCREATE', 1);
insert into blocktable (SKILLID, BLOCKID) values (12, 30);
insert into syntaxtable (SKILLID, SYNTAXID) values (12, 2);