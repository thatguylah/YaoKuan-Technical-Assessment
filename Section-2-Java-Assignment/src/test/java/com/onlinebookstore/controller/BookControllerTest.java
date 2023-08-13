package com.onlinebookstore.controller;

import com.onlinebookstore.model.dto.FilterBookDTO;
import com.onlinebookstore.model.dto.SearchBookDTO;
import com.onlinebookstore.model.entity.Book;
import com.onlinebookstore.service.BookService;
import com.onlinebookstore.service.SearchService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class BookControllerTest {

    @Mock
    BookService bookService;

    @Mock
    SearchService searchService;

    @InjectMocks
    BookController bookController;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testGetAllBooks() {
        Book book = new Book();
        book.setTitle("Sample Title");

        List<Book> books = List.of(book);

        when(bookService.getAllBooks()).thenReturn(books);

        List<Book> response = bookController.getAllBooks();

        assertEquals(1, response.size());
        assertEquals(books, response);
    }

    @Test
    void testGetBookById() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Sample Title");

        when(bookService.getBook(1L)).thenReturn(Optional.of(book));

        ResponseEntity<Book> response = bookController.getBook(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(book, response.getBody());
    }

    @Test
    void testRemoveBook() {
        doNothing().when(bookService).removeBook(1L);

        ResponseEntity<Void> response = bookController.removeBook(1L);

        assertEquals(204, response.getStatusCodeValue()); // No Content status code
    }

    @Test
    void testGetBookQuantity() {
        when(bookService.getBookQuantity(1L)).thenReturn(10);

        ResponseEntity<Integer> response = bookController.getBookQuantity(1L);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(10, response.getBody());
    }

    @Test
    void testUpdateBookQuantity() {
        Book book = new Book();
        book.setId(1L);
        book.setTitle("Sample Title");
        book.setQuantity(15);

        when(bookService.updateBookQuantity(1L, 15)).thenReturn(book);

        ResponseEntity<Book> response = bookController.updateBookQuantity(1L, 15);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(book, response.getBody());
    }

    @Test
    void testAddBookOnlyTitle() {
        Book book = new Book();
        book.setTitle("Sample Title");

        when(bookService.addBook(any(Book.class))).thenReturn(book);

        ResponseEntity<Book> response = bookController.addBook(book);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals(book, response.getBody());
    }

    @Test
    void testAddBookPass() {
        Book book = new Book();
        book.setTitle("Sample Title");
        book.setAuthor("Hello");
        book.setIsbn("test");
        book.setPrice(0.0);
        book.setQuantity(1);

        when(bookService.addBook(any(Book.class))).thenReturn(book);

        ResponseEntity<Book> response = bookController.addBook(book);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(book, response.getBody());
    }

    @Test
    void testFilterBooks() {
        FilterBookDTO filterCriteria = new FilterBookDTO(10,100,true);

        Book book = new Book();
        book.setTitle("Sample Title");
        book.setPrice(50.0);

        List<Book> books = Arrays.asList(book);

        when(searchService.combinedFilter(filterCriteria)).thenReturn(books);

        ResponseEntity<List<Book>> response = bookController.filterBooks(filterCriteria);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(books, response.getBody());
    }

    @Test
    void testSearchBooks() {
        SearchBookDTO searchCriteria = new SearchBookDTO("helloworld","harry","testing");

        Book book = new Book();
        book.setTitle("Hello Worldhelloworld hello world");

        List<Book> books = List.of(book);

        when(searchService.combinedSearch(searchCriteria)).thenReturn(books);  // Fixed this line

        ResponseEntity<List<Book>> response = bookController.searchBooks(searchCriteria);

        assertEquals(200, response.getStatusCodeValue());
        assertEquals(books, response.getBody());
    }

}
