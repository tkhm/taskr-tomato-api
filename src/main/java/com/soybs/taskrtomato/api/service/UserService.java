package com.soybs.taskrtomato.api.service;

import java.util.Arrays;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.soybs.taskrtomato.api.iobean.User;

public class UserService {
    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    /**
     * AuthorizationHeaderの情報からユーザーを取得する 現時点ではヘッダーから情報を取り出すに留めている
     */
    public User getLoginUser(String authHeader) {
        if (Objects.isNull(authHeader)) {
            logger.info("Auth header is null.");
            return null;
        }

        authHeader = authHeader.substring("Digest ".length());
        String[] strs = authHeader.split(",");
        String username = Arrays.stream(strs).filter(s -> s.startsWith("username")).findFirst().orElse("username=");

        username = username.substring(username.indexOf("=") + 1);
        if (username.indexOf("\"") == 0) {
            username = username.substring(1, username.length() - 1); // peel ""
        }

        User user = null;

        if (Objects.isNull(username)) {
            return null;
        } else {
            user = new User();
            user.id = username;
        }

        return user;
    }

}
