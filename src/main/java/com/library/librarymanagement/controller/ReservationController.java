package com.library.librarymanagement.controller;

import com.library.librarymanagement.dto.ReservationRequest;
import com.library.librarymanagement.entity.Reservation;
import com.library.librarymanagement.service.ReservationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/reservations")

public class ReservationController {
    
    @Autowired
    private ReservationService reservationService;
    
    @PostMapping
    @PreAuthorize("hasRole('USER') or hasRole('LIBRARIAN')")
    public ResponseEntity<?> createReservation(@RequestBody ReservationRequest request) {
        try {
            return ResponseEntity.ok(reservationService.createReservation(
                request.getUserId(), 
                request.getBookId(), 
                request.getDays()
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @GetMapping("/user/{userId}")
    @PreAuthorize("hasRole('USER') or hasRole('LIBRARIAN')")
    public ResponseEntity<List<Reservation>> getUserReservations(@PathVariable Integer userId) {
        return ResponseEntity.ok(reservationService.getUserReservations(userId));
    }
    
    @GetMapping
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<List<Reservation>> getAllReservations() {
        return ResponseEntity.ok(reservationService.getAllReservations());
    }
    
    @PatchMapping("/{id}/return")
    @PreAuthorize("hasRole('USER') or hasRole('LIBRARIAN')")
    public ResponseEntity<?> returnBook(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(reservationService.returnBook(id));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}