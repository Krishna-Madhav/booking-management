package com.example.booking.repo;

import com.example.booking.entity.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, String> {
    List<Book> findByTitleContainingIgnoreCaseAndAvailabilityTrue(String title);
    List<Book> findByAuthorContainingIgnoreCaseAndAvailabilityTrue(String author);
    List<Book> findByPublicationYearAndAvailabilityTrue(Integer publicationYear);
    Book findByIsbnAndAvailabilityTrue(String isbn);
}
