package com.salecycle.impressions;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServerResponse;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.RoutingContext;
import io.vertx.redis.RedisClient;
import io.vertx.redis.RedisOptions;

/**
 * Created by steve.jenkins on 09/05/2016.
 */
class Handlers {

    private RedisClient redisClient;

    Handlers(Vertx vertx) {
        RedisOptions redisOptions = new RedisOptions()
                .setHost("127.0.0.1");

        this.redisClient = RedisClient.create(vertx, redisOptions);


    }

    static void handleHealth(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();

        response.putHeader("content-type", "text/plain").end("Heathy as!");
    }

    void handleImpression(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        redisClient.lpush("sc_impressions", routingContext.getBodyAsString().trim(), result -> {
            if (result.succeeded()) {
                response
                        .putHeader("content-type", "text/plain")
                        .setStatusCode(204)
                        .end("");
                return;
            }

            System.out.println(result.cause());

            response.setStatusCode(500)
                    .end(response.getStatusMessage());
        });


    }

    static void handleEcho(RoutingContext routingContext) {
        HttpServerResponse response = routingContext.response();
        JsonObject impression = routingContext.getBodyAsJson();

        response.putHeader("content-type", "application/json").end(impression.encodePrettily());
    }
}
