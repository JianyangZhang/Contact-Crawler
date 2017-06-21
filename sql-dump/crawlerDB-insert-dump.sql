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
