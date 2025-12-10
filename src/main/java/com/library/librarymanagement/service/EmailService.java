package com.library.librarymanagement.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    
    @Autowired
    private JavaMailSender mailSender;
    
    @Value("${spring.mail.username}")
    private String fromEmail;
    
    public void sendWelcomeEmail(String toEmail, String name, String role) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Welcome to Library Management System");
            message.setText(
                "Dear " + name + ",\n\n" +
                "Welcome to our Library Management System!\n\n" +
                "Your account has been created successfully as a " + role + ".\n\n" +
                "You can now log in and start using the system.\n\n" +
                "Best regards,\n" +
                "Library Management Team"
            );
            
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
    

    public void sendReservationConfirmation(String toEmail, String bookTitle, String dueDate) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Book Reservation Confirmation");
            message.setText(
                "Dear Member,\n\n" +
                "Your book reservation has been confirmed!\n\n" +
                "Book: " + bookTitle + "\n" +
                "Due Date: " + dueDate + "\n\n" +
                "Please return the book by the due date to avoid late fees.\n\n" +
                "Best regards,\n" +
                "Library Management Team"
            );
            
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
    
    public void sendReturnConfirmation(String toEmail, String bookTitle) {
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(fromEmail);
            message.setTo(toEmail);
            message.setSubject("Book Return Confirmation");
            message.setText(
                "Dear Member,\n\n" +
                "Thank you for returning the book: " + bookTitle + "\n\n" +
                "The book has been successfully returned to the library.\n\n" +
                "Best regards,\n" +
                "Library Management Team"
            );
            
            mailSender.send(message);
        } catch (Exception e) {
            System.err.println("Failed to send email: " + e.getMessage());
        }
    }
}