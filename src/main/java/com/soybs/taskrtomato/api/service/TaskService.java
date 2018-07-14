package com.soybs.taskrtomato.api.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.soybs.taskrtomato.api.dao.PostgresDao;
import com.soybs.taskrtomato.api.dto.Tasks;
import com.soybs.taskrtomato.api.dto.Tomatoes;
import com.soybs.taskrtomato.api.iobean.Task;
import com.soybs.taskrtomato.api.iobean.Tomato;
import com.soybs.taskrtomato.api.iobean.User;

public class TaskService {
    private static final Logger logger = LoggerFactory.getLogger(TaskService.class);

    private PostgresDao psqlDao;

    public TaskService() {
        this.psqlDao = new PostgresDao();
    }

    /** 指定ユーザーでタスクを新規作成する */
    public boolean createTask(Task task, User user) {
        if (Objects.isNull(task) || Objects.isNull(user)) {
            return false;
        }

        Tasks tasksDto = convertTaskToTasksDto(task, user.id);

        boolean isSucceeded = this.psqlDao.insertTask(tasksDto);
        return isSucceeded;
    }

    /** タスクの完了処理をする */
    public boolean finishTask(String taskId, User user) {
        if (Objects.isNull(taskId) || Objects.isNull(user)) {
            return false;
        }

        if (!isEligible(convertStringToUuid(taskId), user.id)) {
            return false;
        }

        boolean isSucceeded = this.psqlDao.finishTask(convertStringToUuid(taskId));

        return isSucceeded;
    }

    /** ユーザーに紐づくすべてのタスクを取得する トマト付きになる予定 */
    public List<Task> getTaskList(User user) {
        if (Objects.isNull(user)) {
            return null;
        }

        List<Tasks> tasksDtoList;
        tasksDtoList = this.psqlDao.getTaskList(user.id);

        List<Task> taskList = new ArrayList<>();
        if (!Objects.isNull(tasksDtoList)) {
            tasksDtoList.forEach(each -> {
                Task task = convertTasksDtoToTask(each);
                taskList.add(task);
            });
        }
        return taskList;
    }

    /** タスクに紐づくトマトを保存する */
    public boolean saveTomato(Tomato tomato, User user) {
        if (Objects.isNull(tomato) || Objects.isNull(user)) {
            return false;
        }

        if (!isEligible(convertStringToUuid(tomato.taskId), user.id)) {
            return false;
        }

        Tomatoes tomatoesDto = convertTomatoToTomatoesDto(tomato);

        boolean isSucceded = this.psqlDao.insertTomato(tomatoesDto);
        return isSucceded;
    }

    /** UUID変換, 形式誤りなどによる変換ミス時はnullを返す */
    private UUID convertStringToUuid(String stringUuid) {
        UUID convertedUuid;
        try {
            convertedUuid = UUID.fromString(stringUuid);
        } catch (IllegalArgumentException e) {
            logger.error(e.getMessage());
            return null;
        }
        return convertedUuid;
    }

    private Task convertTasksDtoToTask(Tasks tasksDto) {
        Task task = new Task();
        task.id = tasksDto.id.toString();
        task.category = tasksDto.category;
        task.title = tasksDto.title;
        task.description = tasksDto.description;
        task.isFinished = tasksDto.isFinished;
        task.createdAt = String.valueOf(tasksDto.createdAt);
        task.updatedAt = String.valueOf(tasksDto.updatedAt);

        tasksDto.tomatoes.forEach(each -> {
            Tomato tomato = convertTomatoesDtoToTomato(each);
            task.tomatoes.add(tomato);
        });
        return task;
    }

    private Tasks convertTaskToTasksDto(Task task, String userId) {
        Tasks tasksDto = new Tasks();

        tasksDto.id = convertStringToUuid(task.id);
        tasksDto.category = task.category;
        tasksDto.title = task.title;
        tasksDto.description = task.description;
        tasksDto.isFinished = task.isFinished;
        tasksDto.userId = userId;

        return tasksDto;
    }

    private Tomato convertTomatoesDtoToTomato(Tomatoes tomatoesDto) {
        Tomato tomato = new Tomato();

        tomato.id = tomatoesDto.id;
        tomato.summary = tomatoesDto.summary;
        tomato.taskId = tomatoesDto.taskId.toString();
        tomato.finishedAt = String.valueOf(tomatoesDto.finishedAt);

        return tomato;
    }

    private Tomatoes convertTomatoToTomatoesDto(Tomato tomato) {
        Tomatoes tomatoesDto = new Tomatoes();

        tomatoesDto.summary = tomato.summary;
        tomatoesDto.taskId = convertStringToUuid(tomato.taskId);

        return tomatoesDto;
    }

    /** 更新系タスクにおいてそもそも更新の権限を持っているかの確認 */
    private boolean isEligible(UUID taskId, String userId) {
        if (Objects.isNull(taskId) || Objects.isNull(userId)) {
            return false;
        }
        return this.psqlDao.isTaskUserOwn(taskId, userId);
    }

}