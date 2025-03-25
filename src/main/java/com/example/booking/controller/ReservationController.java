package com.example.booking.controller;

import com.example.booking.entity.Reservation;
import com.example.booking.service.ReservationService;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/api/reservations")
public class ReservationController {
    private final ReservationService reservationService;

    public ReservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @PostMapping("/reserve")
    public Reservation reserveBook(@RequestBody Map<String, String> request) {
        return reservationService.reserveBook(
                request.get("userId"),
                request.get("bookId"),
                LocalDate.parse(request.get("reservationDate")),
                LocalDate.parse(request.get("dueDate")),
                request.get("status"),
                request.get("notes")
        );
    }

    @PutMapping("/update/{id}")
    public Reservation updateReservation(@PathVariable String id, @RequestBody Map<String, String> request) {
        return reservationService.updateReservation(
                id,
                LocalDate.parse(request.get("newDueDate")),
                request.get("newStatus"),
                request.get("newNotes")
        );
    }

    @GetMapping("/fine/{id}")
    public Map<String, Double> calculateFine(@PathVariable String id) {
        return Map.of("fine", reservationService.calculateFine(id));
    }

    @DeleteMapping("/delete/{id}")
    public void deleteReservation(@PathVariable String id) {
        reservationService.deleteReservation(id);
    }
}
