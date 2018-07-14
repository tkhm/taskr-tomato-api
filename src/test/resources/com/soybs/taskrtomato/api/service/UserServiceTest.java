package com.soybs.taskrtomato.api.service;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import org.junit.Before;
import org.junit.Test;

import com.soybs.taskrtomato.api.dao.JpaDao;
import com.soybs.taskrtomato.api.dto.Users;

public class UserServiceTest {

    EntityManagerFactory entityManagerFactory;

    @Before
    public void setUp() throws Exception {
        entityManagerFactory = Persistence.createEntityManagerFactory("tomato-unit");
    }

    @Test
    public void test() {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();
        Users aUser = entityManager.find(Users.class, "annotator001");
        entityManager.getTransaction().commit();
        entityManager.close();

        assertThat(aUser.id, is("annotator001"));
    }

    @Test
    public void test2() {
        JpaDao jpaDao = new JpaDao();
        boolean actual = jpaDao.finishTask(UUID.fromString("ed8d84a0-7a05-11e8-83a0-116395a7c3da"));
        assertThat(actual, is(true));
    }

}
