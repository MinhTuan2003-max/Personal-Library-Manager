package com.luv2code.springboot.demo.plm.repository;

import com.luv2code.springboot.demo.plm.model.entity.Book;
import com.luv2code.springboot.demo.plm.model.enums.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface BookRepository extends MongoRepository<Book, String> {

    Page<Book> findByUserId(String userId, Pageable pageable);

    List<Book> findByUserId(String userId);

    Optional<Book> findByIdAndUserId(String id, String userId);

    @Query("{'userId': ?0, $or: [{'title': {$regex: ?1, $options: 'i'}}, {'author': {$regex: ?1, $options: 'i'}}]}")
    Page<Book> findByUserIdAndTitleOrAuthorContainingIgnoreCase(String userId, String keyword, Pageable pageable);

    Page<Book> findByUserIdAndGenre(String userId, Genre genre, Pageable pageable);

    long countByUserId(String userId);
}
