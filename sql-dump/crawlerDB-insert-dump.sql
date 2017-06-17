INSERT INTO Search VALUES("123", 15, "google", "", "", "pending", "wentao", "veloz");
UPDATE Search SET search_progress = 'pending' WHERE search_id = 123;

SELECT * FROM Email;
SELECT * FROM Customer;