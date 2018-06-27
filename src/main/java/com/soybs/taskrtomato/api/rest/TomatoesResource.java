package com.soybs.taskrtomato.api.rest;

import java.util.Objects;

import javax.ws.rs.Consumes;
import javax.ws.rs.HeaderParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.soybs.taskrtomato.api.iobean.Tomato;
import com.soybs.taskrtomato.api.iobean.User;
import com.soybs.taskrtomato.api.service.TaskService;
import com.soybs.taskrtomato.api.service.UserService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@Api(value = "ポモドーロ関連操作")
@Path("/tomatoes")
public class TomatoesResource extends TomatoApplication {
    private UserService userService = new UserService();
    private TaskService taskService = new TaskService();

    /**
     * 結果の送信、作業中のデータを保存にも使用する
     */
    @POST
    @Consumes({ MediaType.APPLICATION_JSON })
    @ApiOperation(value = "ポモドーロの終了の保存", notes = "ポモドーロの終了時に保存する")
    @ApiResponses(value = { @ApiResponse(code = CODE_NO_CONTENT, message = "OK"),
            @ApiResponse(code = CODE_UNAUTHORIZED, message = "認証に失敗しました"),
            @ApiResponse(code = CODE_INTERNAL_SERVER_ERROR, message = "Error") })
    public Response postTomatoes(
            @ApiParam(value = "ダイジェスト認証情報", required = false) @HeaderParam("Authorization") String auth,
            @ApiParam(value = "完了したポモドーロの情報", required = true) Tomato tomato) {

        User user = this.userService.getLoginUser(auth);
        if (Objects.isNull(user)) {
            return RESP_UNAUTHORIZED;
        }

        if (!this.taskService.saveTomato(tomato, user)) {
            return RESP_INTERNAL_SERVER_ERROR;
        }

        return RESP_NO_CONTENT;
    }
}
