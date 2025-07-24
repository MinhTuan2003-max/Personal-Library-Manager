package com.luv2code.springboot.demo.plm.service;

import com.luv2code.springboot.demo.plm.model.dto.request.BookCreateRequest;
import com.luv2code.springboot.demo.plm.model.dto.request.BookUpdateRequest;
import com.luv2code.springboot.demo.plm.model.dto.response.BookResponse;
import com.luv2code.springboot.demo.plm.model.enums.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    Page<BookResponse> getAllBooks(String userId, Pageable pageable);
    Page<BookResponse> searchBooks(String userId, String keyword, Pageable pageable);
    Page<BookResponse> getBooksByGenre(String userId, Genre genre, Pageable pageable);
    BookResponse getBookById(String id, String userId);
    BookResponse createBook(BookCreateRequest request, String userId);
    BookResponse updateBook(String id, BookUpdateRequest request, String userId);
    boolean deleteBook(String id, String userId);
    long getTotalBooksCount(String userId);
}
