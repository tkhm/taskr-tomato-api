package com.soybs.taskrtomato.api.iobean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class Tomato {
    @ApiModelProperty(dataType = "number", example = "1", value = "各ポモドーロのIDとなる連番", required = false)
    public int id;

    @ApiModelProperty(dataType = "string", example = "be8f91b0-ab68-490f-9dba-2db80ee4d201", value = "ポモドーロに紐づくタスクID, uuid形式", required = true)
    public String taskId;

    @ApiModelProperty(dataType = "string", example = "Write 2 paragraphs", value = "ポモドーロ完了時に書く実施内容", required = false)
    public String summary;

    @ApiModelProperty(dataType = "string", example = "2018-05-07 03:23:44.078", value = "ポモドーロの完了日時", required = false)
    public String finishedAt;
}
