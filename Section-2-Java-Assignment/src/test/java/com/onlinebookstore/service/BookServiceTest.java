package com.onlinebookstore.service;

import com.onlinebookstore.model.entity.Book;
import com.onlinebookstore.repository.BookRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import com.onlinebookstore.model.dto.FilterBookDTO;
import com.onlinebookstore.model.dto.SearchBookDTO;

import java.util.Optional;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class BookServiceTest {

    @Mock
    BookRepository bookRepository;

    @InjectMocks
    BookService bookService;

    @InjectMocks
    SearchService searchService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testAddBook() {
        Book book = new Book();
        book.setTitle("Sample Title");
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        Book returnedBook = bookService.addBook(book);

        verify(bookRepository).save(book);
        assertEquals(book, returnedBook);
    }

    @Test
    void testGetBook() {
        Long id = 1L;
        Book book = new Book();
        book.setId(id);
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));

        Optional<Book> returnedBook = bookService.getBook(id);

        verify(bookRepository).findById(id);
        assertTrue(returnedBook.isPresent());
        assertEquals(book, returnedBook.get());
    }

    @Test
    void testGetAllBooks() {
        Book book1 = new Book();
        book1.setTitle("Title1");
        Book book2 = new Book();
        book2.setTitle("Title2");
        List<Book> books = Arrays.asList(book1, book2);

        when(bookRepository.findAll()).thenReturn(books);

        List<Book> returnedBooks = bookService.getAllBooks();

        verify(bookRepository).findAll();
        assertEquals(2, returnedBooks.size());
        assertTrue(returnedBooks.containsAll(books));
    }

    @Test
    void testRemoveBook() {
        Long id = 1L;
        doNothing().when(bookRepository).deleteById(id);

        bookService.removeBook(id);

        verify(bookRepository).deleteById(id);
    }

    @Test
    void testUpdateBookQuantity() {
        Long id = 1L;
        int quantity = 5;
        Book book = new Book();
        book.setId(id);
        book.setQuantity(quantity);
        when(bookRepository.save(any(Book.class))).thenReturn(book);
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));

        Book updatedBook = bookService.updateBookQuantity(id, 10);

        verify(bookRepository).findById(id);
        verify(bookRepository).save(book);
        assertEquals(10, updatedBook.getQuantity());
    }

    @Test
    void testGetBookQuantity() {
        Long id = 1L;
        int quantity = 5;
        Book book = new Book();
        book.setId(id);
        book.setQuantity(quantity);
        when(bookRepository.findById(id)).thenReturn(Optional.of(book));

        int returnedQuantity = bookService.getBookQuantity(id);

        verify(bookRepository).findById(id);
        assertEquals(quantity, returnedQuantity);
    }

    @Test
    void testFilterBooks() {
        FilterBookDTO filterCriteria = new FilterBookDTO(10.0,50.0,true);
        // Let's say filterCriteria has attributes "minPrice", "maxPrice", and "minQuantityInStock"
        int HARD_CODED_0  =0; //Available means as long as qtyInStock > 0

        Book book1 = new Book();
        book1.setTitle("Affordable Book 1");
        book1.setPrice(15.0);
        book1.setQuantity(10);

        Book book2 = new Book();
        book2.setTitle("Affordable Book 2");
        book2.setPrice(30.0);
        book2.setQuantity(20);

        List<Book> books = Arrays.asList(book1, book2);

        when(bookRepository.findByPriceBetweenAndQuantityInStockGreaterThan(10.0, 50.0,HARD_CODED_0)).thenReturn(books);

        List<Book> returnedBooks = searchService.combinedFilter(filterCriteria);

        verify(bookRepository).findByPriceBetweenAndQuantityInStockGreaterThan(10.0, 50.0,HARD_CODED_0);
        assertEquals(2, returnedBooks.size());
        assertTrue(returnedBooks.containsAll(books));
    }

    @Test
    void testSearchBooks() {
        SearchBookDTO searchCriteria = new SearchBookDTO("Hello World","John Doe","1234567890");
        // Let's say searchCriteria has attributes "title", "author", and "isbn"


        Book book = new Book();
        book.setTitle("Hello world by John Doe");
        book.setAuthor("John Doe");
        book.setIsbn("1234567890");

        List<Book> books = List.of(book);

        when(bookRepository.findByTitleContainingAndAuthorContainingAndIsbnContaining("Hello World", "John Doe", "1234567890")).thenReturn(books);

        List<Book> returnedBooks = searchService.combinedSearch(searchCriteria);

        verify(bookRepository).findByTitleContainingAndAuthorContainingAndIsbnContaining("Hello World", "John Doe", "1234567890");
        assertEquals(1, returnedBooks.size());
        assertTrue(returnedBooks.contains(book));
    }

}
