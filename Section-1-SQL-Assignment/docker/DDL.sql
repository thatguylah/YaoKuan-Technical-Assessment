CREATE TABLE tblSubscriptionInfo
(
    subscription_id int,
        product_id int,
        product_name varchar(256),
        subscription_start_date timestamp,
        subscription_end_date timestamp,
        customer_id int,
        customer_contact_phone varchar(256),
        customer_name varchar(256),
        customer_address varchar(256)
);

/*Changed datetime to timestamp and varchar to varchar(256)*/

INSERT INTO tblSubscriptionInfo VALUES
                                    (1, 101, 'Autocad 2022', '2020-01-01', '2020-12-31', 1, '555-1234', 'John Doe', '123 Street'),
                                    (2, 101, 'Autocad 2022', '2021-01-01', '2021-12-31', 2, '555-5678', 'Jane Smith', '456 Lane'),
                                    (3, 101, 'Autocad 2022', '2021-01-01', '2021-12-31', 1, '555-1234', 'John Doe', '123 Street'),
                                    (4, 101, 'Autocad 2022', '2022-01-01', '2022-12-31', 1, '555-1234', 'John Doe', '123 Street'),
                                    (5, 103, 'Revit 2022', '2022-02-01', '2022-11-30', 2, '555-5678', 'Jane Smith', '456 Lane'),
                                    (6, 102, 'Autocad 2023', '2023-01-01', '2023-12-31', 1, '555-1234', 'John Doe', '123 Street'),
                                    (7, 104, 'Revit 2023', '2023-01-01', '2023-12-31', 2, '555-5678', 'Jane Smith', '456 Lane');

/* Insert some mock values into the DB during the initialization phase */