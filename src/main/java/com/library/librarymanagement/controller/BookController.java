package com.library.librarymanagement.controller;

import com.library.librarymanagement.entity.Book;
import com.library.librarymanagement.entity.Book.BookStatus;
import com.library.librarymanagement.service.BookService;
import com.library.librarymanagement.service.FileStorageService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/books")

public class BookController {
    
    @Autowired
    private BookService bookService;
    
    @GetMapping
    public ResponseEntity<List<Book>> getAllBooks() {
        return ResponseEntity.ok(bookService.getAllBooks());
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable Integer id) {
        return ResponseEntity.ok(bookService.getBookById(id));
    }
    
    @GetMapping("/filter")
    public ResponseEntity<List<Book>> getBooksByFilters(
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String author,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) String language) {
        return ResponseEntity.ok(bookService.getBooksByFilters(category, author, genre, language));
    }
    
    @PostMapping
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<?> createBook(@RequestBody Book book) {
        try {
            return ResponseEntity.ok(bookService.createBook(book));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<?> updateBook(@PathVariable Integer id, @RequestBody Book book) {
        try {
            return ResponseEntity.ok(bookService.updateBook(id, book));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @PatchMapping("/{id}/status")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<?> updateBookStatus(@PathVariable Integer id, @RequestParam BookStatus status) {
        try {
            return ResponseEntity.ok(bookService.updateBookStatus(id, status));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
    
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<?> deleteBook(@PathVariable Integer id) {
        try {
            bookService.deleteBook(id);
            return ResponseEntity.ok("Book deleted successfully");
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }


    @Autowired
    private FileStorageService fileStorageService;
    
    @PostMapping("/{id}/upload-image")
    @PreAuthorize("hasRole('LIBRARIAN')")
    public ResponseEntity<?> uploadBookImage(@PathVariable Integer id, @RequestParam("image") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("Please select a file to upload");
            }
            
            // Store the file and get the URL
            String imageUrl = fileStorageService.storeFile(file);
            
            // Update the book with the image URL
            Book book = bookService.getBookById(id);
            
            // Delete old image if exists
            if (book.getImageUrl() != null) {
                fileStorageService.deleteFile(book.getImageUrl());
            }
            
            book.setImageUrl(imageUrl);
            Book updatedBook = bookService.updateBook(id, book);
            
            return ResponseEntity.ok(updatedBook);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Failed to upload image: " + e.getMessage());
        }
    }


}