package com.salecycle.impressions;

import io.vertx.core.Vertx;

import io.vertx.core.http.HttpServer;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.BodyHandler;

public class Server {

    public static void main(String[] args) {

        Vertx vertx = Vertx.vertx();

        HttpServer server = vertx.createHttpServer();

        Router router = Router.router(vertx);
        Handlers handler = new Handlers(vertx);

        router.route().handler(BodyHandler.create());
        router.get("/health").handler(Handlers::handleHealth);
        router.post("/impression").handler(handler::handleImpression);
        router.post("/echo").handler(Handlers::handleEcho);

         server.requestHandler(router::accept)
            .listen(5678, hndlr -> {
                if (hndlr.succeeded()) {
                    System.out.println("http://localhost:5678/");
                } else {
                    System.err.println("Failed to listen on port 8080");
                }
            });
    }

}
