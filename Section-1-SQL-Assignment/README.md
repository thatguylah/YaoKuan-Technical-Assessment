# Table of contents
1. Assignment Prompt
2. High Level Approach
3. Docker Approach (To stand up infrastructure on local)
4. Terraform Approach (To stand up infra on AWS)
5. How to use?
6. Explanations
7. 

# Assignment

We have a single table below. This table sits behind a web application that allows our existing customers subscribe to use Autodesk software. That application also allows our existing customers to update customer info, like their name or address.
When a customer updates their contact phone number, what query should we run in order to save that update to the database?

create table tblSubscriptionInfo
(
subscription_id int
product_id int
product_name varchar
subscription_start_date datetime
subscription_end_date datetime
customer_id int
customer_contact_phone varchar
customer_name varchar
customer_address varchar
)

We've noted that the phone number update feature in the web application is too slow, and have identified that the update query is the primary bottleneck. What could we do to speed up this query?

Come up with the queries to find:

number of subscribers whose subscriptions will be ending in 2023;
number of subscribers who have subscribed for more than 3 months in 2022;
subscribers who have subscribed for more than two products;
product with the most/2ndmost/3rdmost number of subscribers in 2022;
number of subscribers who have re-subscribed more than once for each product;
subscribers who have re-subscribed a higher version of the product in 2023 - for example Autocad 2022 to Autocad 2023.