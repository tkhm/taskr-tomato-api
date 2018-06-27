package com.soybs.taskrtomato.api.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyLoader {
    /**
     * 環境変数(ANNOT_DB_URL, ANNOT_DB_USER, ANNOT_DB_PW)からプロパティを読み込む
     * 見つからない場合は、指定されたファイル名をクラスパスから探し出し、プロパティとして読み込んで返す
     * 
     * @param dbPropFilePath
     *            環境変数が見つからない場合の探索対象のファイル名
     * 
     * @return 探索対象のファイルを読み込んだプロパティ
     */
    public static Properties loadDbProperty(String dbPropFilePath) {
        final String urlProp = "TOMATO_DB_URL";
        final String userProp = "TOMATO_DB_USER";
        final String pwProp = "TOMATO_DB_PW";
        Properties props = new Properties();

        String envUrl = "";
        String envUser = "";
        String envPw = "";
        // 環境変数から値を取得する
        try {
            envUrl = System.getenv(urlProp);
            envUser = System.getenv(userProp);
            envPw = System.getenv(pwProp);
            props.put("url", envUrl);
            props.put("user", envUser);
            props.put("password", envPw);
        } catch (NullPointerException | SecurityException e) {
            // 環境変数から取得できなかった場合はファイルから読み込む
            props = loadPropertyFromFile(dbPropFilePath);
        }

        return props;
    }

    /**
     * 指定されたファイル名をクラスパスから探し出し、プロパティとして読み込んで返す {@code loadProperty("db.properties");}
     * 
     * @param propFileName
     *            探索対象のファイル名
     * @return 探索対象のファイルを読み込んだプロパティ
     */
    public static Properties loadPropertyFromFile(String propFileName) {
        Properties props = new Properties();
        try (InputStream input = PropertyLoader.class.getResourceAsStream(propFileName)) {
            props.load(input);
            input.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return props;
    }
}
