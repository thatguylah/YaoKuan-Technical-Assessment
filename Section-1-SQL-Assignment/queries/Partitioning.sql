-- Create the main table, which will serve as a partitioned table
CREATE TABLE tblSubscriptionInfo (
                                     subscription_id int,
                                     product_id int,
                                     product_name varchar(256),
                                     subscription_start_date timestamp,
                                     subscription_end_date timestamp,
                                     customer_id int,
                                     customer_contact_phone varchar(256),
                                     customer_name varchar(256),
                                     customer_address varchar(256)
) PARTITION BY LIST (product_id);

-- Create partitions for specific product IDs
CREATE TABLE tblSubscriptionInfo_product1 PARTITION OF tblSubscriptionInfo
    FOR VALUES IN (1);

CREATE TABLE tblSubscriptionInfo_product2 PARTITION OF tblSubscriptionInfo
    FOR VALUES IN (2);