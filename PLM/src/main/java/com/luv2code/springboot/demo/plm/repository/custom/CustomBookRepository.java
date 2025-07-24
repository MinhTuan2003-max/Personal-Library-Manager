package com.luv2code.springboot.demo.plm.repository.custom;

import com.luv2code.springboot.demo.plm.model.entity.Book;

import java.util.List;

public interface CustomBookRepository {
    List<Book> findBooksWithComplexQuery(String userId, String searchTerm, String genre);
}