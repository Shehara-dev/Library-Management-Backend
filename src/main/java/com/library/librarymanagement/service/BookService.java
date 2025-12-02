package com.library.librarymanagement.service;

import com.library.librarymanagement.entity.Book;
import com.library.librarymanagement.entity.Book.BookStatus;
import com.library.librarymanagement.repository.BookRepository;
import com.library.librarymanagement.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class BookService {
    
    @Autowired
    private BookRepository bookRepository;
    
    @Autowired
    private CategoryRepository categoryRepository;
    
    public Book createBook(Book book) {
        if (book.getCategory() != null && book.getCategory().getId() != null) {
            book.setCategory(categoryRepository.findById(book.getCategory().getId())
                .orElseThrow(() -> new RuntimeException("Category not found")));
        }
        book.setStatus(BookStatus.AVAILABLE);
        return bookRepository.save(book);
    }
    
    public List<Book> getAllBooks() {
        return bookRepository.findAll();
    }
    
    public Book getBookById(Integer id) {
        return bookRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Book not found"));
    }
    
    public List<Book> getBooksByFilters(String category, String author, String genre, String language) {
        return bookRepository.findByFilters(category, author, genre, language);
    }
    
    public Book updateBook(Integer id, Book book) {
        Book existingBook = bookRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Book not found"));
        
        existingBook.setTitle(book.getTitle());
        existingBook.setAuthor(book.getAuthor());
        existingBook.setGenre(book.getGenre());
        existingBook.setLanguage(book.getLanguage());
        existingBook.setIsbn(book.getIsbn());
        existingBook.setImageUrl(book.getImageUrl());
        
        if (book.getCategory() != null && book.getCategory().getId() != null) {
            existingBook.setCategory(categoryRepository.findById(book.getCategory().getId())
                .orElseThrow(() -> new RuntimeException("Category not found")));
        }
        
        return bookRepository.save(existingBook);
    }
    
    public Book updateBookStatus(Integer id, BookStatus status) {
        Book book = bookRepository.findById(id)
            .orElseThrow(() -> new RuntimeException("Book not found"));
        book.setStatus(status);
        return bookRepository.save(book);
    }
    
    public void deleteBook(Integer id) {
        bookRepository.deleteById(id);
    }
}