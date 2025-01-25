package com.risha.journalApp.repository;

import com.risha.journalApp.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.util.List;

public class UserRepositoryImpl {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<User> getUSerForSA(){
        Query query = new Query();
        query.addCriteria(Criteria.where("email")
                .regex("[A-Za-z0-9\\._%+\\-]+@[A-Za-z0-9\\.\\-]+\\.[A-Za-z]{2,6}$"));    //one way
        query.addCriteria(Criteria.where("sentimentAnalysis").exists(true));

//        Criteria criteria = new Criteria();
//        query.addCriteria(criteria.orOperator(               //another way
//                Criteria.where("email").exists(true),
//                Criteria.where("semanticAnalysis").exists(true))
//        );

        List<User> users =  mongoTemplate.find(query, User.class);
        return users;

    }
}
