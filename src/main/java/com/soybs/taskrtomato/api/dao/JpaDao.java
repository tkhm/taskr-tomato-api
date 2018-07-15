package com.soybs.taskrtomato.api.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
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
        String insertTaskSql = "INSERT INTO tasks (id,category,title,description,is_finished,created_at,updated_at,user_id)"
                + " VALUES(?,?,?,?,false,now(),now(),?)";
        int updateCount = 0;

        try (Connection conn = getConnection();
                PreparedStatement insertTaskStmt = conn.prepareStatement(insertTaskSql)) {
            insertTaskStmt.setObject(1, task.id);
            insertTaskStmt.setString(2, task.category);
            insertTaskStmt.setString(3, task.title);
            insertTaskStmt.setString(4, task.description);
            insertTaskStmt.setString(5, task.userId);

            updateCount = insertTaskStmt.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getSQLState());
            logger.error(e.getMessage());
            return false;
        }

        return updateCount == 1;
    }

    /** タスクに紐づくトマトを保存する */
    public boolean insertTomato(Tomatoes tomatoesDto) {
        String insertTaskSql = "INSERT INTO tomatoes (summary,finished_at,task_id) VALUES(?,now(),?)";
        int updateCount = 0;

        try (Connection conn = getConnection();
                PreparedStatement insertTaskStmt = conn.prepareStatement(insertTaskSql)) {
            insertTaskStmt.setString(1, tomatoesDto.summary);
            insertTaskStmt.setObject(2, tomatoesDto.taskId);

            updateCount = insertTaskStmt.executeUpdate();
        } catch (SQLException e) {
            logger.error(e.getSQLState());
            logger.error(e.getMessage());
            return false;
        }

        return updateCount == 1;
    }

    /** 1件以上の応答があればtrue, 厳密には1件の応答になるはず */
    public boolean isTaskUserOwn(UUID taskId, String userId) {
        String countTaskSql = "SELECT COUNT(*) FROM tasks WHERE id = ? AND user_id = ?";
        int taskCount = 0;

        try (Connection conn = getConnection(); PreparedStatement countTaskStmt = conn.prepareStatement(countTaskSql)) {
            countTaskStmt.setObject(1, taskId);
            countTaskStmt.setString(2, userId);

            try (ResultSet rs = countTaskStmt.executeQuery();) {
                if (rs.next()) {
                    taskCount = rs.getInt(1);
                } else {
                    logger.info("Specified task_id and user_id is not in tasks : " + taskId + "," + userId);
                    return false;
                }
            }
        } catch (SQLException e) {
            logger.error(e.getSQLState());
            logger.error(e.getMessage());
            return false;
        }

        return taskCount > 0;
    }

    /**
     * PostgreSQLの設定値を取得してConnectionをreturnするメソッド
     *
     * @return Connection PostgreSQLへのConnection
     */
    private Connection getConnection() {
        return null;
    }

    private void init() {
        entityManagerFactory = Persistence.createEntityManagerFactory("tomato-unit");
    }
}
