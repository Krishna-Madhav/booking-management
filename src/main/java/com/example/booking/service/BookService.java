package com.example.booking.service;

import com.example.booking.entity.Book;

import com.example.booking.repo.BookRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    private final BookRepository bookRepository;

    public BookService(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    public List<Book> searchBooks(String title, String author, Integer publicationYear, String isbn) {
        if (isbn != null) return List.of(bookRepository.findByIsbnAndAvailabilityTrue(isbn));
        if (title != null) return bookRepository.findByTitleContainingIgnoreCaseAndAvailabilityTrue(title);
        if (author != null) return bookRepository.findByAuthorContainingIgnoreCaseAndAvailabilityTrue(author);
        if (publicationYear != null) return bookRepository.findByPublicationYearAndAvailabilityTrue(publicationYear);
        return bookRepository.findAll();
    }
}
