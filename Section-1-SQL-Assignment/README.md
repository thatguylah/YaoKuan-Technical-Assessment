# Table of contents
1. How to use? 
2. Q1. Update Query
3. Q2. Optimize Update Query
4. Q3. SQL Queries
5. TODO: Docker/Terraform for deployment purposes


# How to use?
Run the following commands in terminal/CLI. Assume docker is installed on host machine. 

    cd docker/
    docker build -t section-1-image .
    docker run --name section-1-container -p 5432:5432 -d section-1-image
    docker exec -it section-1-container bash

Once inside the docker container:
        
    psql -U myuser mydatabase

Folder structure: 
- README.md (Contains questions, answers, code snippets)
- `docker/`
  - Dockerfile (Stand up Postgres ENV on local docker engine)
  - DDL.sql (Basic Data Definition Language to stand up tables and insert mock values)
- `queries/`
  - relevant .sql queries, copy and paste the relevant snippets and run them in psql shell as needed
  - Optionally you may also choose to spin up a lightweight SQL client like sqlelectron or DBeaver to connect to the Postgres container.
  - I prefer using psql shell directly tho :)
- `terraform/`
  - TODO: simple rds.tf script that spins up the same infra in AWS instead. Along with all the defined DDL.
  - Likely will use RDS in Postgres. 



# Q1. Update Query
*Q: When a customer updates their contact phone number, what query should we run in order to save that update to the database?*

Basic answer:
    
    UPDATE tblSubscriptionInfo
    SET customer_contact_phone = 'new-phone-number'
    WHERE customer_id = specific_customer_id;

Replace 'new-phone-number' with the correct phone number and update only on customer_id

Better answer:
    
    BEGIN;

    UPDATE tblSubscriptionInfo
    SET customer_contact_phone = 'new-phone-number'
    WHERE customer_id = specific_customer_id;

    COMMIT;

Why is this a better answer? For most OLTP systems such as this one, its important to maintain ACID transactions to ensure data integrity.
A few key points on transactions:

Atomicity: Transactions ensure that operations are atomic. This means all operations within the transaction are completed successfully, or none of them are. If an error occurs during any of the operations, a ROLLBACK can be issued to revert all changes made during that transaction.

Isolation: Transactions provide isolation from other operations. This means the operations within a transaction are hidden from other concurrent transactions until the transaction is complete.

Consistency: By ensuring atomicity and isolation, transactions help maintain database consistency.

Deadlocks: Be aware that transactions can lead to deadlocks if not managed correctly. Deadlocks occur when two or more transactions wait indefinitely for one another to release locks. Database systems usually have mechanisms to detect and resolve deadlocks, often by aborting one of the transactions.

Locking: When working with transactions in high concurrency environments, it's essential to understand the different types of database locks and their implications (e.g., row-level lock, table-level lock). Picking the right type of lock can optimize performance while ensuring data consistency.

In summary, for an OLTP system with multiple concurrent transactions, it's a good practice to use transactions to ensure data consistency and integrity, but it's also important to be aware of the implications and potential pitfalls.

OPTIONALLY: 

We also might want to check whether the Phone Number field is valid before actually committing, each transaction.

    DO $$
    BEGIN
    -- Assuming a phone number is valid if it's 10 characters long
        IF LENGTH('111-111-1111') = 10 THEN
            UPDATE tblSubscriptionInfo
            SET customer_contact_phone = '111-111-1111'
            WHERE customer_id = 1;
        END IF;
    END $$;

Side note: Alternative to using an anonymous block like `DO`, we can consider adding `CONSTRAINT` into our phone number column. 

Also, it might be the case that customer_id is not always retrivable, in that case probably customer_name combined with customer_address is a better choice for our WHERE clause.
    
    UPDATE tblSubscriptionInfo
    SET customer_contact_phone = 'new-phone-number'
    WHERE customer_name = 'specific_name' AND customer_address = 'specific_address';

TLDR; 
- Basic ans: use UPDATE, SET where customer_id = some_specific_customer_id
- Better ans: use SQL transactions with BEGIN and COMMIT
- Maintaining ACID for OLTP systems. 
- Additional Considerations: Data Validation, Changing WHERE clause
- Additional reading: 
- 
# Q2. Optimize Update Query
*Q: We've noted that the phone number update feature in the web application is too slow, and have identified that the update query is the primary bottleneck. What could we do to speed up this query?*

There are actually many things we can do speed up our update query. We will focus on mainly 3. 
## 1. Indexing
- Assuming we are updating WHERE customer_id = some_customer_id, that means `customer_id` is a prime column for us to build our index on.
- The update works in two parts. First, the DB finds the row to update on, before the actual write operation occurs.
- During the lookup phase of the update, the DB can use indexes to quickly locate rows that needs updating, avoiding a costly full table scan.
- However, that is under the assumption that the indexed column is used, if another column were to be used, then the index is avoided. Which would be bad.
- The trade-off here is that while the indexes speed up the lookup phase, additional overhead is introduced in the write phase as the DB also needs to update the index.
- Also worth mentioning that there are different kinds of indexes: B-tree, Hash, GiST, etc. But i think thats too in depth and should be out of scope for this section. 

Example SQL:

    CREATE INDEX idx_customer_id ON tblSubscriptionInfo (customer_id);

## 2. Partitioning
- Partitioning divides a table into smaller, more manageable pieces but still being treated as a single table.
- Instead of searching the entire DB for rows to update, the DB only has to search in the relevant partition.
- For example, we could be partitioning the table based on `subscription_end_date`, thus if many users are due for renewal and need to update their contact numbers, only the partition for that specific month or year is accessed.
- The key here is to choose the most relevant partition key that will see the greatest performance gain. 
- Another partition key we could consider is by `product_id`, so if certain products see a higher volume of updates than others, partioning by product can isolate the heavy-update partitions from the lighter ones.

Example SQL:

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

## 3. Normalization
   - This would likely involve separating user information out into a `user` table which could contain fields like customer_contact_phone, customer_name, customer_address and customer_id
   - This way updates to customer information would have no impact on the `subscriptions` table which would likely see a performance gain. 
   - Separating user-related information is a core principle of normalization, where data has a single source of truth which reduces redundancy. 
   - Decoupling user-related updates with `subscriptions` would remove locking on the `subscriptions` table which means read and writes can see performance improvement. 
   - Considerations: Normalization would usually mean more JOINs on disparate tables which could lead to other bottlenecks.

## Other Techniques
- Sharding (Distributing the DB across a cluster): Perhaps this is too heavy-handed as now we have to maintain multiple clusters and possibly lose our ACID transactions
- Vertical Scaling (Increasing RAM and Faster disks): Not always feasible esp in non-cloud environments
- Caching (Using redis or memcached): More commonly used for read operations so might not be applicable to our current use case. 

TLDR;
- Indexing: Index on `customer_id` should be the correct column
- Partitioning
- Normalization: Separating user info out from subscriptions table. 
- 3 techniques which can help optimize our update query. 
- Other techniques include increasing physical capacity in one way or another. 
- Not preferred if there are lower hanging fruits, but of course everything is a trade-off :)

# Q3. SQL Queries
- The assumption here is that `number of subscribers` will be number of unique subscribers, meaning if John Doe subscribed to two products, it counts as only one subscriber. 

## 1. Number of Subscribers whose subscriptions will be ending in 2023;
    SELECT COUNT(DISTINCT customer_id) 
    FROM tblSubscriptionInfo
    WHERE EXTRACT(YEAR FROM subscription_end_date) = 2023;
## 2. Number of Subscribers who subscribed more than 3 months in 2022;
    SELECT COUNT(DISTINCT customer_id) 
    FROM tblSubscriptionInfo
    WHERE EXTRACT(YEAR FROM subscription_start_date) = 2022 AND
    subscription_end_date - subscription_start_date > INTERVAL '3 MONTH';
## 3. Subscribers who subscribed more than 2 products;
    SELECT customer_name
    FROM tblSubscriptionInfo
    GROUP BY customer_name
    HAVING COUNT(DISTINCT product_id) > 2;
## 4. Product with the most/2ndmost/3rdmost number of subscribers in 2022;
    SELECT product_id, COUNT(DISTINCT customer_id) as subscriber_count
    FROM tblSubscriptionInfo
    WHERE EXTRACT(YEAR FROM subscription_start_date) = 2022
    GROUP BY product_id
    ORDER BY subscriber_count DESC
    LIMIT 3;
## 5. Number of Subscribers who re-subscribed more than once for each product;
    SELECT product_id, COUNT(DISTINCT customer_id) AS total_resubscribers_multiple_times
    FROM tblSubscriptionInfo
    GROUP BY product_id, customer_id
    HAVING COUNT(*) > 2;
## 6. Subscribers who re-sub a higher ver of the product in 2023;
    SELECT DISTINCT customer_id, customer_name
    FROM tblSubscriptionInfo t1
    WHERE EXTRACT(YEAR FROM t1.subscription_start_date) = 2023
    AND EXISTS (
    SELECT 1
    FROM tblSubscriptionInfo t2
    WHERE t2.customer_id = t1.customer_id
    AND t2.product_name < t1.product_name
    )