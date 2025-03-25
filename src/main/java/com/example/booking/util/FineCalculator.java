package com.example.booking.util;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class FineCalculator {

    public static double calculateFine(LocalDate dueDate, LocalDate returnDate) {
        long daysLate = ChronoUnit.DAYS.between(dueDate, returnDate);

        if (daysLate <= 0) return 0;

        double fine = 0;
        if (daysLate > 14) {
            fine += Math.min(daysLate - 14, 14) * 1.0;  // $1 per day after 2 weeks
        }
        if (daysLate > 28) {
            fine += (daysLate - 28) * 0.5;  // $0.5 per day after a month
        }
        return fine;
    }
}
