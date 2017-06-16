INSERT INTO Search VALUES("123", 5, "uber", "", "", "pending", "wentao", "tbc");
UPDATE Search SET search_progress = 'pending' WHERE search_id = 123;

SELECT * FROM Email;