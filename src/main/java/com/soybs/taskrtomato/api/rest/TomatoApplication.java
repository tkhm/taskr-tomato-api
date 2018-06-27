package com.soybs.taskrtomato.api.rest;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.Status;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@ApplicationPath("/v1")
public class TomatoApplication extends Application {
    /** Web APIの受付口では共通のロガーで取り回す */
    protected static final Logger logger = LoggerFactory.getLogger(TomatoApplication.class);

    // 従来的にresponseを返したいならここで既定したものを使うとよい
    static final Response RESP_OK = Response.ok().build();
    static final Response RESP_ACCEPTED = Response.status(Status.ACCEPTED).build();
    static final Response RESP_CREATED = Response.status(Status.CREATED).build();
    static final Response RESP_NO_CONTENT = Response.status(Status.NO_CONTENT).build();

    static final Response RESP_BAD_REQUEST = Response.status(Status.BAD_REQUEST).build();
    static final Response RESP_UNAUTHORIZED = Response.status(Status.UNAUTHORIZED).build();
    static final Response RESP_NOT_FOUND = Response.status(Status.NOT_FOUND).build();

    static final Response RESP_INTERNAL_SERVER_ERROR = Response.status(Status.INTERNAL_SERVER_ERROR).build();
    static final Response RESP_NOT_IMPLEMENTED = Response.status(Status.NOT_IMPLEMENTED).build();

    /** 正常系の応答で使う */
    static final int CODE_OK = 200;
    /** Putなどの更新系応答時などに使う **/
    static final int CODE_CREATED = 201;
    /** Deleteなどの削除系応答時などに使う **/
    static final int CODE_NO_CONTENT = 204;

    /** パラメーターエラーなどに使う */
    static final int CODE_BAD_REQUEST = 400;
    /** 必要な認証に失敗した場合などに使う */
    static final int CODE_UNAUTHORIZED = 401;
    /** リエクスト先が見つからないもしくは存在を知らせたくないときなどに使う */
    static final int CODE_NOT_FOUND = 404;
    /** 既に存在する対象を追加しようとする際のエラーなどに使う */
    static final int CODE_CONFLICT = 409;

    /** サーバーサイド由来のエラーに使う */
    static final int CODE_INTERNAL_SERVER_ERROR = 500;
    /** 未実装時に使う */
    static final int CODE_NOT_IMPLEMENTED = 501;
    /** メンテ時など応答不能時に使う */
    static final int CODE_SERVICE_UNAVAILABLE = 503;
}
