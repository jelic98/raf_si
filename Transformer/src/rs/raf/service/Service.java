package rs.raf.service;

import io.javalin.Javalin;
import io.javalin.core.JavalinConfig;
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
    private static final String CORE_URL = "http://127.0.0.1:9000/core/models";
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

        makeRequest(BROKER_URL, form, null);
    }

    private void startService() {
        Javalin server = Javalin.create(JavalinConfig::enableCorsForAllOrigins).start(port);
        server.post("/transform", new Handler() {
            @Override
            public void handle(@NotNull Context ctx) {
                JSONObject response = new JSONObject();

                try {
                    String model = StringEscapeUtils.unescapeHtml4(ctx.formParam("model"));

                    JSONObject transformed = new Transformer().transform(model);

                    JSONObject id = transformed.getJSONObject("_id");

                    Map<String, String> form = new HashMap<>();
                    form.put("type", "functional");
                    form.put("name", id.getString("name"));
                    form.put("project", id.getString("project"));
                    form.put("details", transformed.toString());

                    Map<String, String> headers = new HashMap<>();
                    headers.put("Authorization", ctx.header("Authorization"));

                    makeRequest(CORE_URL, form, headers);

                    response.put("code", 200);
                    response.put("_id", id);
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

    private void makeRequest(String url, Map<String, String> form, Map<String, String> headers) throws IOException {
        MultipartBody.Builder body = new MultipartBody.Builder().setType(MultipartBody.FORM);

        for (Map.Entry<String, String> e : form.entrySet()) {
            body.addFormDataPart(e.getKey(), e.getValue());
        }

        Request.Builder request = new Request.Builder().url(url).post(body.build());

        if(headers != null) {
            for(Map.Entry<String, String> e : headers.entrySet()) {
                request.header(e.getKey(), e.getValue());
            }
        }

        Response response = new OkHttpClient().newCall(request.build()).execute();
        System.out.println(response);
    }
}

