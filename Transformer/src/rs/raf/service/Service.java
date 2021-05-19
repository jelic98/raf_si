package rs.raf.service;

import io.javalin.Javalin;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import okhttp3.*;
import org.apache.commons.text.StringEscapeUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONObject;
import rs.raf.transformer.Transformer;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Service {

    private static final String BROKER_URL = "http://127.0.0.1:9000/services";
    private static final String SERVICE_NAME = "transformer";
    private static final String SERVICE_HOST = "127.0.0.1";

    private final int port;

    public Service(int port) {
        this.port = port;
    }

    public void start() {
        try {
            registerService();
            startService();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void registerService() throws IOException {
        Map<String, String> form = new HashMap<>();
        form.put("name", SERVICE_NAME);
        form.put("host", SERVICE_HOST);
        form.put("port", String.valueOf(port));

        makeRequest(BROKER_URL, form);
    }

    private void startService() {
        Javalin server = Javalin.create().start(port);
        server.post("/transform", new Handler() {
            @Override
            public void handle(@NotNull Context ctx) {
                JSONObject response = new JSONObject();

                try {
                    String model = StringEscapeUtils.unescapeHtml4(ctx.formParam("model"));

                    JSONObject id = new Transformer().transform(model);

                    response.put("code", 200);
                    response.put("id", id);
                }catch(Exception e) {
                    response.put("code", 500);
                    response.put("message", e.getMessage());

                    e.printStackTrace();
                }

                ctx.status(response.getInt("code"));
                ctx.result(response.toString());
            }
        });
    }

    private void makeRequest(String url, Map<String, String> form) throws IOException {
        MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);

        for (Map.Entry<String, String> e : form.entrySet()) {
            body.addFormDataPart(e.getKey(), e.getValue());
        }

        Request request = new Request.Builder()
                .url(url)
                .post(body.build())
                .build();

        Response response = new OkHttpClient().newCall(request).execute();
        System.out.println(response);
    }
}

