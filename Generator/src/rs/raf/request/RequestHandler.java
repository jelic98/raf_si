package rs.raf.request;

import rs.raf.response.Response;

public interface RequestHandler {

    Response handle(String method, String request);
    String getPath();
}
