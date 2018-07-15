package com.soybs.taskrtomato.api.service;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import com.soybs.taskrtomato.api.dao.JpaDao;
import com.soybs.taskrtomato.api.dto.Tasks;

public class UserServiceTest {

    JpaDao jpaDao;

    @Before
    public void setUp() {
        jpaDao = new JpaDao();
    }

    @Test
    public void test() {
        List<Tasks> tasksList = jpaDao.getTaskList("annotator001");
        assertThat(tasksList.size(), is(2));
    }

    @Test
    public void test2() {
        boolean actual = jpaDao.finishTask(UUID.fromString("ed8d84a0-7a05-11e8-83a0-116395a7c3da"));
        assertThat(actual, is(true));
    }

}
