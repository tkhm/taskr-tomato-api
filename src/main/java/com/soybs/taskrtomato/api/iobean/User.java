package com.soybs.taskrtomato.api.iobean;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@ApiModel
public class User {
    @ApiModelProperty(dataType = "string", example = "kanamechan", value = "ユーザーID", required = true)
    public String id;

}
