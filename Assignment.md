# SQL

**Assignment**

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

1. number of subscribers whose subscriptions will be ending in 2023;
2. number of subscribers who have subscribed for more than 3 months in 2022;
3. subscribers who have subscribed for more than two products;
4. product with the most/2ndmost/3rdmost number of subscribers in 2022;
5. number of subscribers who have re-subscribed more than once for each product;
6. subscribers who have re-subscribed a higher version of the product in 2023 - for example Autocad 2022 to Autocad 2023.


# JAVA

You are tasked with implementing a simple online bookstore inventory system. The inventory consists of books, and you need to design data structures and implement algorithms to manage the inventory. Additionally, you need to interact with a SQL database to store and retrieve data.

**Requirement**:

- Implement a microservice Bookstore that contains the following functionality:
- Add a book to the inventory.
- Remove a book from the inventory.
- Update the quantity in stock for a given book.
- Retrieve the quantity in stock for a given book.
- List all books in the inventory.
- Implement at least one popular design pattern in your solution. You can choose from patterns like Singleton, Factory, Observer, Strategy, or any other appropriate pattern that fits the context of the problem.

**Additional Requirements (Bonus Points):**

- Search and Filter Functionality: Implement a search functionality that allows users to search for books by their title, author, or ISBN. Additionally, add filtering options to search for books based on price range, availability, or any other relevant criteria.
- Authentication and Authorization: Implement a simple authentication mechanism for users, including registration and login. Ensure that only authenticated users can perform certain actions, such as adding books to the inventory or making purchases.
- Error Handling and Logging: Implement robust error handling and logging mechanisms to capture and log any exceptions or errors that occur during the execution of the system.
- Performance Optimization: Optimize the database queries and operations for improved performance and efficiency, especially when dealing with large datasets.

**Instructions:**

- Use Java to implement the classes and methods described above. You may use any popular java framework such as Spring Boot, Micronaut, hibernate etc...
- Use any relational database of your choice (e.g., MySQL, PostgreSQL) for storing the book information. Include instructions on how to set up the database schema and connection details in your submission.
- You can use any appropriate data structures and algorithms to implement the functionalities.
- Make sure to handle any potential exceptions or errors that may occur during database interactions or book inventory management.

**Submission Instructions:**

Create a GitHub repository to store your code and related files.
Commit and push all your code files (Java classes), SQL scripts, and the README file into the repository.
Ensure that your repository is public, or provide access to the repository for the evaluation team (if it's a private repository).
Include any additional instructions or notes on how to set up and run your solution in the README file.
Your submission will be evaluated based on the correctness, efficiency, and readability of your code. Additionally, ensure that your code follows best practices, uses appropriate data structures and algorithms, and demonstrates proficiency in SQL database interactions.