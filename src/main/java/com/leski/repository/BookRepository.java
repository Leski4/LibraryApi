package com.leski.repository;

import com.leski.model.Book;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookRepository extends JpaRepository<Book,String> {
    boolean existsByIsbn(String isbn);
}