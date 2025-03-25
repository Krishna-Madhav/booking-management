package com.example.booking.service;

import com.example.booking.entity.Book;
import com.example.booking.entity.Reservation;
import com.example.booking.entity.User;
import com.example.booking.exception.ResourceNotFoundException;
import com.example.booking.repo.BookRepository;
import com.example.booking.repo.ReservationRepository;
import com.example.booking.repo.UserRepository;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final BookRepository bookRepository;
    private final UserRepository userRepository;

    public ReservationService(ReservationRepository reservationRepository, BookRepository bookRepository, UserRepository userRepository) {
        this.reservationRepository = reservationRepository;
        this.bookRepository = bookRepository;
        this.userRepository = userRepository;
    }

    public Reservation reserveBook(String userId, String bookId, LocalDate reservationDate, LocalDate dueDate, String status, String notes) {
        User user = userRepository.findById(Long.valueOf(userId))
                .orElseThrow(() -> new ResourceNotFoundException("User not found with ID: " + userId));

        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with ID: " + bookId));

        if (!book.getAvailability()) {
            throw new IllegalStateException("Book is not available for reservation.");
        }

        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setBook(book);
        reservation.setReservationDate(reservationDate);
        reservation.setDueDate(dueDate);
        reservation.setStatus(status);
        reservation.setNotes(notes);

        book.setAvailability(false);
        bookRepository.save(book);

        return reservationRepository.save(reservation);
    }

    public Reservation updateReservation(String reservationId, LocalDate newDueDate, String newStatus, String newNotes) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with ID: " + reservationId));

        reservation.setDueDate(newDueDate);
        reservation.setStatus(newStatus);
        reservation.setNotes(newNotes);

        return reservationRepository.save(reservation);
    }

    public double calculateFine(String reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with ID: " + reservationId));

        LocalDate dueDate = reservation.getDueDate();
        LocalDate today = LocalDate.now();

        if (!today.isAfter(dueDate)) {
            return 0.0; // No fine if the book is returned on or before due date
        }

        long overdueDays = ChronoUnit.DAYS.between(dueDate, today);
        double fine = 0.0;

        if (overdueDays > 14 && overdueDays <= 30) {
            fine = (overdueDays - 14) * 1.0; // $1 per day after 2 weeks up to a month
        } else if (overdueDays > 30) {
            fine = (16 * 1.0) + ((overdueDays - 30) * 0.5); // $0.5 per day after 1 month
        }

        return fine;
    }

    public void deleteReservation(String reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found with ID: " + reservationId));

        reservation.setStatus("deleted");
        reservationRepository.save(reservation);
    }
}
