package rs.raf.service;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import okhttp3.*;
import org.apache.commons.text.StringEscapeUtils;
import org.jetbrains.annotations.NotNull;
import rs.raf.generator.Generator;
import rs.raf.util.Log;
import java.io.IOException;

public class Service {

    private static final String BROKER_URL = "http://127.0.0.1:9000/services";
    private static final String SERVICE_NAME = "generator";
    private static final String SERVICE_HOST = "127.0.0.1";
    private static final String SERVICE_PORT = "9006";

    private final int port;

    public Service(int port) {
        this.port = port;
    }

    public void start() {
        try {
            registerService();
            startService();
        } catch (Exception e) {
            Log.error(String.format("Cannot start service on port %s (%s)", port, e.getMessage()));
            e.printStackTrace();
        }
    }

    private void registerService() throws IOException {
        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("name", SERVICE_NAME)
                .addFormDataPart("host", SERVICE_HOST)
                .addFormDataPart("port", SERVICE_PORT)
                .build();
        Request request = new Request.Builder()
                .url(BROKER_URL)
                .post(body)
                .build();
        new OkHttpClient().newCall(request).execute();
    }

    private void startService() {
        Javalin server = Javalin.create().start(port);
        server.post("/generate", new Handler() {
            @Override
            public void handle(@NotNull Context ctx) {
                ctx.result(new Generator().generate(StringEscapeUtils.unescapeHtml4(ctx.formParam("model"))));
            }
        });
    }
}

