package com.luv2code.springboot.demo.plm.repository.custom;

import com.luv2code.springboot.demo.plm.model.entity.Book;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomBookRepositoryImpl implements CustomBookRepository {

    @Autowired
    private MongoTemplate mongoTemplate;

    @Override
    public List<Book> findBooksWithComplexQuery(String userId, String searchTerm, String genre) {
        Query query = new Query();
        query.addCriteria(Criteria.where("userId").is(userId));

        if (searchTerm != null && !searchTerm.isEmpty()) {
            Criteria searchCriteria = new Criteria().orOperator(
                    Criteria.where("title").regex(searchTerm, "i"),
                    Criteria.where("author").regex(searchTerm, "i")
            );
            query.addCriteria(searchCriteria);
        }

        if (genre != null && !genre.isEmpty()) {
            query.addCriteria(Criteria.where("genre").is(genre));
        }

        return mongoTemplate.find(query, Book.class);
    }
}