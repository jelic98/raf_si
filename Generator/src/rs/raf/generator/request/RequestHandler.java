package rs.raf.generator.request;

import rs.raf.generator.response.Response;

public interface RequestHandler {

    Response handle(String method, String request);
    String getPath();
}
