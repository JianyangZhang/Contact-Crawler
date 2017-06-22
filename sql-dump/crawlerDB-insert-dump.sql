INSERT INTO Search VALUES("123", 3, "microsoft", "", "", "pending", "wentao", "veloz");
INSERT INTO Search VALUES("456", 2, "interior designer", "", "", "pending", "wentao", "veloz");
UPDATE Search SET search_progress = 'pending' WHERE search_id = 123;

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
Insert into Company VALUES('baller','baller.com');
insert into Employment VALUES('testurl1','baller');
insert into Employment VALUES('testurl2','baller');
insert into Employment VALUES('testurl3','baller');
insert into Email VALUES('kobe@baller.com','testurl1','baller','0');
insert into Email VALUES('james@baller.com','testurl2','baller','0');
insert into Email VALUES('tim@baller.com','testurl2','baller','2');
insert into Result Values('20170621234247153','testurl1');
Insert into Result Values('20170621234247153','testurl2');
Insert into Result Values('20170621234247153','testurl3');
