/*Basic Ans*/
UPDATE tblSubscriptionInfo
SET customer_contact_phone = '111-111-1111'
WHERE customer_id = 1;

/*Better Ans*/
BEGIN;

UPDATE tblSubscriptionInfo
SET customer_contact_phone = '111-111-1111'
WHERE customer_id = 1;

COMMIT;

/* Data Validation */
DO $$
    BEGIN
        -- Assuming a phone number is valid if it's 10 characters long
        IF LENGTH('111-111-1111') = 10 THEN
            UPDATE tblSubscriptionInfo
            SET customer_contact_phone = '111-111-1111'
            WHERE customer_id = 1;
        END IF;
    END $$;


/* Composite Fields */
UPDATE tblSubscriptionInfo
SET customer_contact_phone = '111-111-1111'
WHERE customer_name = 'John Doe' AND customer_address = '123 Street';