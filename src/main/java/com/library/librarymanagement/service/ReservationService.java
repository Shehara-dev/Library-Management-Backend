package com.library.librarymanagement.service;

import com.library.librarymanagement.entity.Book;
import com.library.librarymanagement.entity.Book.BookStatus;
import com.library.librarymanagement.entity.Reservation;
import com.library.librarymanagement.entity.Reservation.ReservationStatus;
import com.library.librarymanagement.entity.User;
import com.library.librarymanagement.repository.BookRepository;
import com.library.librarymanagement.repository.ReservationRepository;
import com.library.librarymanagement.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private EmailService emailService;
    
    @Autowired
    private ReservationRepository reservationRepository;
    
    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private UserRepository userRepository;
    
    public Reservation createReservation(Integer userId, Integer bookId, Integer days) {
        User user = userRepository.findById(userId)
            .orElseThrow(() -> new RuntimeException("User not found"));
        
        if (user.getIsBlacklisted()) {
            throw new RuntimeException("User is blacklisted");
        }
        
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new RuntimeException("Book not found"));
        
        if (book.getStatus() != BookStatus.AVAILABLE) {
            throw new RuntimeException("Book is not available");
        }
        
        Reservation reservation = new Reservation();
        reservation.setUser(user);
        reservation.setBook(book);
        reservation.setReservationDate(LocalDate.now());
        reservation.setDueDate(LocalDate.now().plusDays(days));
        reservation.setStatus(ReservationStatus.ACTIVE);
        
        book.setStatus(BookStatus.RESERVED);
        bookRepository.save(book);
        
        Reservation savedReservation = reservationRepository.save(reservation);

        // Send reservation confirmation email
        emailService.sendReservationConfirmation(
            user.getEmail(), 
            book.getTitle(), 
            savedReservation.getDueDate().toString()
            );

    return savedReservation;
    }
    
    public List<Reservation> getUserReservations(Integer userId) {
        return reservationRepository.findByUserId(userId);
    }
    
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }
    
    public Reservation returnBook(Integer reservationId) {
        Reservation reservation = reservationRepository.findById(reservationId)
            .orElseThrow(() -> new RuntimeException("Reservation not found"));
        
        reservation.setStatus(ReservationStatus.RETURNED);
        
        Book book = reservation.getBook();
        book.setStatus(BookStatus.AVAILABLE);
        bookRepository.save(book);
        
        Reservation savedReservation = reservationRepository.save(reservation);

        // Send return confirmation email
        emailService.sendReturnConfirmation(
        reservation.getUser().getEmail(), 
        reservation.getBook().getTitle()
        );

        return savedReservation;
        }
}