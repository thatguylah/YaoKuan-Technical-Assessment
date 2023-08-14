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



@Service
public class SearchService {
    private static final Logger logger = LoggerFactory.getLogger(SearchService.class);
    private final BookRepository bookRepository;

    /**
     * Constructor to initialize dependencies for SearchService.
     *
     * @param bookRepository the repository to manage books.
     */
    @Autowired
    public SearchService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    /**
     * Searches for books based on the given search criteria provided within the {@link SearchBookDTO}.
     * The method performs a fuzzy search on the title, author, and ISBN fields of the books.
     * If any of these fields in the DTO is null or empty, that field is disregarded in the search criteria.
     *
     * @param searchBookDto The search criteria encapsulated in a DTO.
     * @return A list of books matching the provided search criteria.
     */

    public List<Book> combinedSearch(SearchBookDTO searchBookDto) {
        String title = (searchBookDto.getTitle() == null || searchBookDto.getTitle().isEmpty()) ? null : searchBookDto.getTitle();
        String author = (searchBookDto.getAuthor() == null || searchBookDto.getAuthor().isEmpty()) ? null : searchBookDto.getAuthor();
        String isbn = (searchBookDto.getIsbn() == null || searchBookDto.getIsbn().isEmpty()) ? null : searchBookDto.getIsbn();

        return bookRepository.findByTitleContainingAndAuthorContainingAndIsbnContaining(title, author, isbn);
    }

    /**
     * Filters the list of books based on the provided filter criteria within the {@link FilterBookDTO}.
     * It allows filtering based on a price range and whether the book is in stock.
     * If the "avail" field in the DTO is true, it only returns books that are in stock.
     *
     * @param filterCriteria The filter criteria encapsulated in a DTO.
     * @return A list of books matching the provided filter criteria.
     */

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
