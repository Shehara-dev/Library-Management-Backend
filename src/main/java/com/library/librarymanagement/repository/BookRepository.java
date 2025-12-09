package com.library.librarymanagement.repository;

import com.library.librarymanagement.entity.Book;
import com.library.librarymanagement.entity.Book.BookStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository
public interface BookRepository extends JpaRepository<Book, Integer> {
    
    List<Book> findByStatus(BookStatus status);
    
    @Query("SELECT b FROM Book b WHERE " +
           "(:category IS NULL OR b.category.name = :category) AND " +
           "(:author IS NULL OR b.author LIKE %:author%) AND " +
           "(:genre IS NULL OR b.genre = :genre) AND " +
           "(:language IS NULL OR b.language = :language)")
    List<Book> findByFilters(
        @Param("category") String category,
        @Param("author") String author,
        @Param("genre") String genre,
        @Param("language") String language
    );
}