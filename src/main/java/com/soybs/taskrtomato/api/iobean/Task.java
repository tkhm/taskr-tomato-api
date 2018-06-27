package com.soybs.taskrtomato.api.iobean;

import java.util.ArrayList;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class Task {
    @ApiModelProperty(dataType = "string", example = "be8f91b0-ab68-490f-9dba-2db80ee4d201", value = "タスクID, uuid形式", required = true)
    public String id;

    @ApiModelProperty(dataType = "string", example = "edge", value = "カテゴリー", required = true)
    public String category;

    @ApiModelProperty(dataType = "string", example = "Writing a letter", value = "タスクタイトル", required = true)
    public String title;

    @ApiModelProperty(dataType = "string", example = "25 or more words required", value = "タスクの内容詳細", required = true)
    public String description;

    @ApiModelProperty(dataType = "boolean", example = "true", value = "タスクが完了済みならtrue", required = false)
    public boolean isFinished;

    @ApiModelProperty(value = "完了させたポモドーロのリスト", required = false)
    public List<Tomato> tomatoes;

    @ApiModelProperty(dataType = "string", example = "2018-05-07 03:23:44.078", value = "タスクの作成日時", required = false)
    public String createdAt;

    @ApiModelProperty(dataType = "string", example = "2018-05-07 03:23:44.078", value = "タスクの更新日時", required = false)
    public String updatedAt;

    /** 配列関係は事前に初期化を済ませる */
    public Task() {
        this.tomatoes = new ArrayList<>();
    }
}