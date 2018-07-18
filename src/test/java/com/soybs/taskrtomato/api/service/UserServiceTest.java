package com.soybs.taskrtomato.api.service;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.List;
import java.util.Properties;
import java.util.UUID;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.junit.BeforeClass;
import org.junit.Test;
import org.postgresql.ds.PGSimpleDataSource;

import com.soybs.taskrtomato.api.dao.JpaDao;
import com.soybs.taskrtomato.api.dto.Tasks;
import com.soybs.taskrtomato.api.testhelper.PropertyLoader;

public class UserServiceTest {

    static JpaDao jpaDao;

    @BeforeClass
    public static void setUpContext() {
        try {
            System.setProperty(Context.INITIAL_CONTEXT_FACTORY, "org.apache.naming.java.javaURLContextFactory");
            System.setProperty(Context.URL_PKG_PREFIXES, "org.apache.naming");

            InitialContext ic = new InitialContext();

            ic.createSubcontext("java:");
            ic.createSubcontext("java:comp");
            ic.createSubcontext("java:comp/env");
            ic.createSubcontext("java:comp/env/jdbc");

            Properties dbProps = PropertyLoader.loadDbProperty("/db.properties");
            String dbUser = dbProps.getProperty("user");
            String dbPass = dbProps.getProperty("password");
            String dbHost = dbProps.getProperty("dbhost");
            String dbName = dbProps.getProperty("dbname");
            int dbPort = Integer.parseInt(dbProps.getProperty("dbport"));

            PGSimpleDataSource ds = new PGSimpleDataSource();

            ds.setUser(dbUser);
            ds.setPassword(dbPass);
            ds.setServerName(dbHost);
            ds.setDatabaseName(dbName);
            ds.setPortNumber(dbPort);

            // persistance.xmlから参照できるようにする
            ic.bind("java:comp/env/jdbc/TomatoDatabase", ds);
        } catch (NamingException ex) {
            ex.printStackTrace();
        }
        jpaDao = new JpaDao();
    }

    @Test
    public void annotator001のタスク一覧が取得できる() {
        List<Tasks> tasksList = jpaDao.getTaskList("annotator001");
        assertThat(tasksList.size(), is(2));
    }

    @Test
    public void 特定タスクのステータスを完了に変更できる() {
        boolean actual = jpaDao.finishTask(UUID.fromString("ed8d84a0-7a05-11e8-83a0-116395a7c3da"));
        assertThat(actual, is(true));
    }

}
