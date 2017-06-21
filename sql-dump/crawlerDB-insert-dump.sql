INSERT INTO Search VALUES("123", 3, "microsoft", "", "", "pending", "wentao", "veloz");
INSERT INTO Search VALUES("456", 2, "interior designer", "", "", "pending", "wentao", "veloz");
UPDATE Search SET search_progress = 'pending' WHERE search_id = 123;

SELECT * FROM Email;
SELECT * FROM Customer;

SELECT * FROM Search;


SELECT * FROM Result;

INSERT INTO Result VALUES("789", "https://www.linkedin.com/in/annie-thomas-harrison-a0103354/");