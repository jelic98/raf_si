package rs.raf;

import com.sun.net.httpserver.HttpServer;
import okhttp3.*;
import rs.raf.request.DefaultHandler;
import rs.raf.request.RequestHandler;
import rs.raf.request.Routes;
import rs.raf.util.Log;
import java.io.IOException;
import java.net.InetSocketAddress;

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
        Response response = new OkHttpClient().newCall(request).execute();
        Log.print(response.body().string());
    }

    private void startService() throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        for (RequestHandler handler : Routes.get()) {
            server.createContext(handler.getPath(), new DefaultHandler(handler));
        }

        server.setExecutor(null);
        server.start();
    }
}

