/* 1. Number of Subscribers whose subscriptions will be ending in 2023; Returns Count = 2 */
SELECT COUNT(DISTINCT customer_id)
FROM tblSubscriptionInfo
WHERE EXTRACT(YEAR FROM subscription_end_date) = 2023;

/* 2. Number of Subscribers who subscribed more than 3 months in 2022; Returns Count=2 */
SELECT COUNT(DISTINCT customer_id)
FROM tblSubscriptionInfo
WHERE EXTRACT(YEAR FROM subscription_start_date) = 2022 AND
            subscription_end_date - subscription_start_date > INTERVAL '3 MONTH';

/* 3. Subscribers who subscribed more than 2 products; Returns customer_name = Jane Smith. Customers with
   only 2 products or fewer are filtered out. */
SELECT customer_name
FROM tblSubscriptionInfo
GROUP BY customer_name
HAVING COUNT(DISTINCT product_id) > 2;

/* 4. Product with the most/2ndmost/3rdmost number of subscribers in 2022; Returns product_id 101 and 103*/
SELECT product_id, COUNT(DISTINCT customer_id) as subscriber_count
FROM tblSubscriptionInfo
WHERE EXTRACT(YEAR FROM subscription_start_date) = 2022
GROUP BY product_id
ORDER BY subscriber_count DESC
LIMIT 3;

/* 5. Number of Subscribers who re-subscribed more than once for each product; Returns product_id 101 and total_resubscribers_multiple_times =1
   Only John Doe resubscribed to a product more than once. Product_id == 101 */

SELECT product_id, COUNT(DISTINCT customer_id) AS total_resubscribers_multiple_times
FROM tblSubscriptionInfo
GROUP BY product_id, customer_id
HAVING COUNT(*) > 2; /*Re-subscribed more than once means you must at least have 3 rows per product_id per unique subscriber.*/

/* 6. Subscribers who re-sub a higher ver of the product in 2023; */
SELECT DISTINCT customer_id, customer_name
FROM tblSubscriptionInfo t1
WHERE EXTRACT(YEAR FROM t1.subscription_start_date) = 2023
  AND EXISTS (
    SELECT 1
    FROM tblSubscriptionInfo t2
    WHERE t2.customer_id = t1.customer_id
      AND t2.product_name < t1.product_name
)
