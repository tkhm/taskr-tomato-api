package com.soybs.taskrtomato.api.dao;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.soybs.taskrtomato.api.dto.Tasks;
import com.soybs.taskrtomato.api.dto.Tomatoes;

public class JpaDao {
    private static final Logger logger = LoggerFactory.getLogger(JpaDao.class);

    private EntityManagerFactory entityManagerFactory;

    public JpaDao() {
        this.init();
    }

    /** 指定したtaskIdに該当するタスクのis_finishedカラムをtrueにする */
    public boolean finishTask(UUID taskId) {
        try {
            EntityManager em = entityManagerFactory.createEntityManager();
            em.getTransaction().begin();
            Tasks task = em.find(Tasks.class, taskId);
            task.isFinished = true;
            em.getTransaction().commit();
            em.close();
        } catch (HibernateException e) {
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }

    /** 該当ユーザーのタスク一覧を応答する */
    public List<Tasks> getTaskList(String userId) {
        List<Tasks> tasksDtoList = new ArrayList<>();

        try {
            EntityManager em = entityManagerFactory.createEntityManager();
            TypedQuery<Tasks> query = em
                    .createQuery("SELECT task FROM Tasks task WHERE task.userId = :userId", Tasks.class)
                    .setParameter("userId", userId);
            tasksDtoList = query.getResultList();
            em.close();
        } catch (HibernateException e) {
            logger.error(e.getMessage());
            return null;
        }
        return tasksDtoList;
    }

    /** タスクを保存する */
    public boolean insertTask(Tasks task) {
        // JPAではinsert時にnow()を入れるのは難しいので処理の直前に時間を取得する
        Timestamp currentTimestamp = new Timestamp(new Date().getTime());
        try {
            EntityManager em = entityManagerFactory.createEntityManager();
            em.getTransaction().begin();
            em.persist(task);
            task.isFinished = false;
            task.createdAt = currentTimestamp;
            task.updatedAt = currentTimestamp;
            em.getTransaction().commit();
            em.close();
        } catch (HibernateException e) {
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }

    /** タスクに紐づくトマトを保存する */
    public boolean insertTomato(Tomatoes tomato) {
        // JPAではinsert時にnow()を入れるのは難しいので処理の直前に時間を取得する
        Timestamp currentTimestamp = new Timestamp(new Date().getTime());
        tomato.finishedAt = currentTimestamp;
        try {
            EntityManager em = entityManagerFactory.createEntityManager();
            em.getTransaction().begin();
            em.persist(tomato);
            em.getTransaction().commit();
            em.close();
        } catch (HibernateException e) {
            logger.error(e.getMessage());
            return false;
        }
        return true;
    }

    /** 1件以上の応答があればtrue, 厳密には1件の応答になるはず */
    public boolean isTaskUserOwn(UUID taskId, String userId) {

        int taskCount = 0;
        try {
            EntityManager em = entityManagerFactory.createEntityManager();
            Query query = em
                    .createQuery("SELECT COUNT(task) FROM Tasks task WHERE task.id = :taskId AND task.userId = :userId")
                    .setParameter("id", taskId).setParameter("userId", userId);
            taskCount = (int) query.getSingleResult();
            em.close();
        } catch (HibernateException e) {
            logger.error(e.getMessage());
            return false;
        }

        return taskCount > 0;
    }

    private void init() {
        entityManagerFactory = Persistence.createEntityManagerFactory("tomato-unit");
    }
}
