package rs.raf.service;

import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;
import io.javalin.http.Context;
import io.javalin.http.Handler;
import okhttp3.*;
import org.apache.commons.text.StringEscapeUtils;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONObject;
import rs.raf.generator.Generator;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Service {

    private static final String BROKER_URL = "http://127.0.0.1:9000/services";
    private static final String STORAGE_URL = "http://127.0.0.1:9006/files";
    private static final String SERVICE_NAME = "generator";
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
        Javalin server = Javalin.create(JavalinConfig::enableCorsForAllOrigins).start(port);
        server.post("/generate", new Handler() {
            @Override
            public void handle(@NotNull Context ctx) {
                JSONObject response = new JSONObject();

                try {
                    String model = StringEscapeUtils.unescapeHtml4(ctx.formParam("model"));

                    JSONArray files = new Generator().generate(model);
                    String hash = UUID.randomUUID().toString();
                    storeFiles(hash, files);

                    response.put("code", 200);
                    response.put("hash", hash);
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

    private void storeFiles(String hash, JSONArray files) throws IOException {
        Map<String, String> form = new HashMap<>();
        form.put("hash", hash);
        form.put("files", files.toString());

        makeRequest(STORAGE_URL, form);
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

