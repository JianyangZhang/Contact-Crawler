INSERT INTO Search VALUES("123", 2, "microsoft", "", "", "pending", "wentao", "veloz", 0);
INSERT INTO Search VALUES("456", 2, "interior designer", "", "", "pending", "wentao", "veloz", 0);
UPDATE Search SET search_progress = 'pending' WHERE search_id = 123;
UPDATE Search SET has_deleted = 1 WHERE search_id = 123;

SELECT * FROM Email;
SELECT * FROM Customer;

SELECT * FROM Search;


SELECT * FROM Result;

INSERT INTO Result VALUES("789", "https://www.linkedin.com/in/annie-thomas-harrison-a0103354/");





-- after create db, excute these queries;
Insert into Linkedin_Account VALUES('adam','adam@thevelozgroup.com','5056Veloz');
Insert into Internal_Company VALUES('CT','Custom Tobacco','adam');
Insert into Internal_Company VALUES('BHC','Beverly Hills Chairs','adam');
Insert into User VALUES('ryan','ryan','123');
Insert into User VALUES('caitlin','caitlin','123');



-- create some test data into db
Insert into Templete VALUES('1',"templete1","content1");
Insert into Templete VALUES('2',"templete2","content2");
Insert into Compaign VALUES('0','CT','1');
Insert into Customer VALUES('testurl1','Kobe','ceo','los angeles','ceo','2017-06-18','2017-06-19','CT','0');
Insert into Customer VALUES('testurl2','James','ceo','cleveland','ceo','2017-06-18','2017-06-19','CT','0');
Insert into Customer VALUES('testurl3','Tim','ceo','texas','ceo','2017-06-18','2017-06-19','CT','0');
Insert into Customer VALUES('testurl4','Tony','ceo','texas','ceo','2017-06-18','2017-06-19','CT','0');

Insert into Company VALUES('baller','baller.com');
insert into Employment VALUES('testurl1','baller');
insert into Employment VALUES('testurl2','baller');
insert into Employment VALUES('testurl3','baller');
insert into Employment VALUES('testurl4','baller');

insert into Email VALUES('kobe@baller.com','testurl1','baller','0');
insert into Email VALUES('kobe24@baller.com','testurl1','baller','0');
insert into Email VALUES('kobe8@baller.com','testurl1','baller','0');



insert into Email VALUES('james@baller.com','testurl2','baller','0');
insert into Email VALUES('james23@baller.com','testurl2','baller','0');
insert into Email VALUES('james6@baller.com','testurl2','baller','0');


insert into Result Values('20170623234150352','testurl1');
Insert into Result Values('20170623234150352','testurl2');
Insert into Result Values('20170623234150352','testurl3');
Insert into Result Values('20170623234150352','testurl4');


update Search set search_progress="completed" where search_id="20170623234150352";
update Search set search_progress="processing" where search_id="201706232342197";
update Search set has_deleted='1' where search_id="20170623234304762";
update Search set has_deleted= 0 where search_id="20170623234240107";

