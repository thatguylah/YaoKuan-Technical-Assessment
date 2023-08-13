package com.onlinebookstore.util;

import com.onlinebookstore.repository.*;
import com.github.javafaker.*;
import com.onlinebookstore.model.entity.User;
import com.onlinebookstore.model.entity.Book;
import com.onlinebookstore.model.entity.UserRole;
import org.springframework.stereotype.Service;


@Service
public class DBSeederService {

    private final BookRepository bookRepository;

    private final UserRepository userRepository;

    public DBSeederService(BookRepository bookRepository, UserRepository userRepository) {
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

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
                    faker.name().username(),
                    faker.internet().password(),
                    UserRole.USER
            );
            userRepository.save(user);
        }
    }
}

