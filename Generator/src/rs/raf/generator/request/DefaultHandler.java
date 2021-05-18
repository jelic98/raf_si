package rs.raf.generator.request;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import rs.raf.generator.response.Response;
import rs.raf.generator.util.Log;

import java.io.*;

public class DefaultHandler implements HttpHandler {

    private final RequestHandler handler;

    public DefaultHandler(RequestHandler handler) {
        this.handler = handler;
    }

    @Override
    public void handle(HttpExchange x) {
        StringBuilder sb = new StringBuilder();

        try(BufferedReader br = new BufferedReader(new InputStreamReader(x.getRequestBody()))) {
            br.lines().forEach(sb::append);
        }catch(Exception e) {
            Log.error(String.format("Error occurred: %s", e.getMessage()));
            return;
        }

        String method = x.getRequestMethod();
        String request = sb.toString();

        Log.print(String.format("%s from %s with %s", method, x.getRemoteAddress(), request));

        try {
            Response response = handler.handle(method, request);

            int code = response.getCode();
            String content = response.getContent();

            Log.print(String.format("%d to %s with %s", code, x.getRemoteAddress(), content));

            x.sendResponseHeaders(code, content.length());

            OutputStream os = x.getResponseBody();
            os.write(content.getBytes());
            os.close();
        }catch (IOException e) {
            Log.error(String.format("Error occurred: %s", e.getMessage()));
        }
    }
}
