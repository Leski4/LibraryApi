package com.leski.service;

import com.leski.model.Book;
import com.leski.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BookService {
    private final BookRepository bookRepository;
    @Autowired
    public BookService(BookRepository bookRepository){
        this.bookRepository = bookRepository;
    }
    public List<Book> getAllBooks(){
        return bookRepository.findAll();
    }
    public Book getBookById(int id){
        return bookRepository.findById(id).orElse(null);
    }
    public void addBook(Book book){
        bookRepository.save(book);
    }
    public void deleteById(int id){
        bookRepository.deleteById(id);
    }
    public Book getBookByISBN(String isbn){
        return bookRepository.findByIsbn(isbn);
    }
}