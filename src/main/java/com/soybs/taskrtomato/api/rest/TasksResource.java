package com.soybs.taskrtomato.api.rest;

import java.util.List;
import java.util.Objects;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.soybs.taskrtomato.api.iobean.Task;
import com.soybs.taskrtomato.api.iobean.User;
import com.soybs.taskrtomato.api.service.TaskService;
import com.soybs.taskrtomato.api.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import io.swagger.jaxrs.PATCH;

@Api(value = "タスク関連操作")
@Path("/tasks")
public class TasksResource extends TomatoApplication {
    private UserService userService = new UserService();
    private TaskService taskService = new TaskService();

    /**
     * タスク一覧を取得
     */
    @GET
    @Produces({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "タスク一覧取得", notes = "全タスク一覧を取得する")
    @ApiResponses(value = { @ApiResponse(code = CODE_OK, response = Task[].class, message = "OK"),
            @ApiResponse(code = CODE_UNAUTHORIZED, message = "認証に失敗しました"),
            @ApiResponse(code = CODE_NOT_FOUND, message = "見つかりませんでした") })
    public Response getTasks(
            @ApiParam(value = "ダイジェスト認証情報", required = false) @HeaderParam("Authorization") String auth) {

        User user = this.userService.getLoginUser(auth);
        if (Objects.isNull(user)) {
            return RESP_UNAUTHORIZED;
        }

        List<Task> taskList = taskService.getTaskList(user);
        if (Objects.isNull(taskList) || taskList.size() == 0) {
            return RESP_NOT_FOUND;
        }

        return Response.status(200).entity(taskList).build();
    }

    /**
     * 結果の送信、作業中のデータを保存にも使用する
     */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "タスク新規作成", notes = "タスクを新規作成する")
    @ApiResponses(value = { @ApiResponse(code = CODE_NO_CONTENT, message = "OK"),
            @ApiResponse(code = CODE_UNAUTHORIZED, message = "認証に失敗しました"),
            @ApiResponse(code = CODE_INTERNAL_SERVER_ERROR, message = "Error") })
    public Response postTasks(
            @ApiParam(value = "ダイジェスト認証情報", required = false) @HeaderParam("Authorization") String auth,
            @ApiParam(value = "保存対象のタスクオブジェクト", required = true) Task task) {

        User user = this.userService.getLoginUser(auth);
        if (Objects.isNull(user)) {
            return RESP_UNAUTHORIZED;
        }

        if (!this.taskService.createTask(task, user)) {
            return RESP_INTERNAL_SERVER_ERROR;
        }

        return RESP_NO_CONTENT;
    }

    /** タスク完了時に投稿する */
    @Path("/{taskId}")
    @PATCH
    @ApiOperation(value = "タスク完了マーク", notes = "タスクの完了状態を更新してタスク完了とする")
    @ApiResponses(value = { @ApiResponse(code = CODE_NO_CONTENT, message = "OK"),
            @ApiResponse(code = CODE_UNAUTHORIZED, message = "認証に失敗しました"),
            @ApiResponse(code = CODE_INTERNAL_SERVER_ERROR, message = "Error") })
    public Response patchTaskIdTasks(
            @ApiParam(value = "ダイジェスト認証情報", required = false) @HeaderParam("Authorization") String auth,
            @ApiParam(value = "更新対象のタスクのID", required = true) @PathParam("taskId") String taskId) {

        User user = this.userService.getLoginUser(auth);
        if (Objects.isNull(user)) {
            return RESP_UNAUTHORIZED;
        }

        if (!this.taskService.finishTask(taskId, user)) {
            return RESP_INTERNAL_SERVER_ERROR;
        }

        return RESP_NO_CONTENT;
    }
}
