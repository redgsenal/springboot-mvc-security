package com.springboot.tutorial.mvc.security.dao;

import com.springboot.tutorial.mvc.security.entity.User;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

public class UserDaoImpl implements UserDao {

    private EntityManager entityManager;

    public UserDaoImpl(EntityManager theEntityManager) {
        this.entityManager = theEntityManager;
    }

    @Override
    public User findByUserName(String userName) {
        // retrieve/read from database using username
        TypedQuery<User> theQuery = entityManager.createQuery("from User where userName =:uName and enabled = true ", User.class);
        theQuery.setParameter("uName", userName);
        try {
            return theQuery.getSingleResult();
        } catch (Exception e) {
        }
        return null;
    }
}
