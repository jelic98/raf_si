package rs.raf.generator.request;

import rs.raf.generator.response.MethodNotAllowedResponse;
import rs.raf.generator.response.Response;

public class GenerateHandler implements RequestHandler {

    @Override
    public Response handle(String method, String request) {
        if (method.equals("POST")) {
            return new Response(200, "Hello world");
        }else {
            return new MethodNotAllowedResponse();
        }
    }

    @Override
    public String getPath() {
        return "/generate";
    }
}
