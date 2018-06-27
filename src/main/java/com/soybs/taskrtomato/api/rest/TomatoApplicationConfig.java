package com.soybs.taskrtomato.api.rest;

import java.util.Set;

import javax.ws.rs.ApplicationPath;
import javax.ws.rs.core.Application;

import io.swagger.jaxrs.config.BeanConfig;
import io.swagger.models.Info;

@ApplicationPath("api")
public class TomatoApplicationConfig extends Application {
    /** Tomato Config */
    public TomatoApplicationConfig() {
        Info apiInfo = new Info();
        apiInfo.setTitle("+kr TOMATO");
        apiInfo.setDescription("TOMATO is life!");
        apiInfo.setVersion("0.0.1");

        BeanConfig beanConfig = new BeanConfig();
        beanConfig.setInfo(apiInfo);

        beanConfig.setSchemes(new String[] { "http", "https" });
        beanConfig.setBasePath("/taskr-tomato-api/v1"); // context＋applicationのルートパスを指定
        beanConfig.setPrettyPrint(true);

        // resourcesを追加することでSwaggerに登場させる
        beanConfig.setResourcePackage("com.soybs.taskrtomato.api.rest");
        // trueをセットしたタイミングでset済みのリソースをスキャンしてSwaggerのAPIとしてInitializeする
        beanConfig.setScan(true);
    }

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);

        // enable Swagger
        resources.add(io.swagger.jaxrs.listing.ApiListingResource.class);
        resources.add(io.swagger.jaxrs.listing.SwaggerSerializers.class);

        return resources;
    }

    /**
     * getClasses()とaddRestResourceClasses()については必要性について要確認
     * 必要と言及している例は複数見かけるがある場合/ない場合の影響範囲が特定できていない
     **/
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(TasksResource.class);
    }
}
