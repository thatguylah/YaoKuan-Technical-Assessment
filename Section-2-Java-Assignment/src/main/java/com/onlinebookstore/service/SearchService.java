package com.onlinebookstore.service;

import com.onlinebookstore.model.entity.Book;
import com.onlinebookstore.model.dto.SearchBookDTO;
import com.onlinebookstore.model.dto.FilterBookDTO;
import com.onlinebookstore.repository.BookRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import java.util.Collections;


@Service
public class SearchService {
    private static final Logger logger = LoggerFactory.getLogger(SearchService.class);
    private final BookRepository bookRepository;

    @Autowired
    public SearchService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> searchByTitle(String title){
        return bookRepository.findByTitleContaining(title);
    }

    public List<Book> searchByAuthor(String author){
        return bookRepository.findByAuthorContaining(author);
    }

    public List<Book> searchByIsbn(String isbn){
        return bookRepository.findByIsbnContaining(isbn);
    }

    public List<Book> filterByPriceRange(Double minPrice, Double maxPrice){
        return bookRepository.findByPriceBetween(minPrice, maxPrice);
    }

    public List<Book> filterByAvailability(){
        return bookRepository.findByQuantityInStockGreaterThan(0);
    }

    public List<Book> combinedSearch(SearchBookDTO searchBookDto) {
        String title = searchBookDto.getTitle();
        String author = searchBookDto.getAuthor();
        String isbn = searchBookDto.getIsbn();

        // If null or empty, use a wildcard-like string to match all
        title = (title == null || title.isEmpty()) ? "" : title;
        author = (author == null || author.isEmpty()) ? "" : author;
        isbn = (isbn == null || isbn.isEmpty()) ? "" : isbn;

        List<Book> titleResults = searchByTitle(title);
        List<Book> authorResults = searchByAuthor(author);
        List<Book> isbnResults = searchByIsbn(isbn);

        // Stream.of will put these lists in a stream
        // The reduce function will take the intersection of these lists
        return Stream.of(titleResults, authorResults, isbnResults)
                .reduce((list1, list2) -> {
                    // retainAll modifies the list in place and returns a boolean
                    list1.retainAll(list2);
                    return list1; // returning the modified list
                }).orElse(Collections.emptyList()); // default to empty list if none are present
    }

    public List<Book> combinedFilter(FilterBookDTO filterCriteria) {
        Double minPrice = Optional.of(filterCriteria.getMinPrice()).orElse(Double.NEGATIVE_INFINITY);
        Double maxPrice = Optional.of(filterCriteria.getMaxPrice()).orElse(Double.POSITIVE_INFINITY);
        boolean avail = filterCriteria.getAvail();

        if(avail) {
            // Considering there's a 'quantity' field in the 'Book' model
            return bookRepository.findByPriceBetweenAndQuantityInStockGreaterThan(minPrice, maxPrice, 0);
        } else {
            return bookRepository.findByPriceBetween(minPrice, maxPrice);
        }
    }
}
