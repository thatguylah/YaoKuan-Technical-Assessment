package com.onlinebookstore.util;

import com.onlinebookstore.repository.*;
import com.github.javafaker.*;
import com.onlinebookstore.model.entity.User;
import com.onlinebookstore.model.entity.Book;
import com.onlinebookstore.model.entity.UserRole;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;


@Service
public class DBSeederService {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    public void seedDatabase() {
        Faker faker = new Faker();

        // Seed Books
        for (int i = 0; i < 100; i++) {
            Book book = new Book(
                    faker.book().title(),
                    faker.book().author(),
                    faker.code().isbn13(),
                    faker.number().randomDouble(2, 5, 50),
                    faker.number().randomDigitNotZero()
            );
            bookRepository.save(book);
        }

        // Seed Users (For example: 10 users)
        for (int i = 0; i < 10; i++) {
            User user = new User(
                    faker.internet().avatar(),
                    faker.internet().password(),
                    UserRole.USER
            );
            userRepository.save(user);
        }
    }
}

