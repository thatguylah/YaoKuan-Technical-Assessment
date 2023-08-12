package com.onlinebookstore.model.entity;

import jakarta.persistence.*;

@Entity
@Table(name="books")
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")  // Ensure you have these annotations
    private String title;

    @Column(name = "author")
    private String author;

    @Column(name = "isbn")
    private String isbn;

    @Column(name = "price")
    private Double price;

    @Column(name = "quantity")
    private Integer quantityInStock;

}
