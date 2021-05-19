package rs.raf.request;

import rs.raf.generator.Generator;
import rs.raf.response.MethodNotAllowedResponse;
import rs.raf.response.Response;

public class GenerateHandler implements RequestHandler {

    private Generator generator;

    public GenerateHandler() {
        generator = new Generator();
    }

    @Override
    public Response handle(String method, String request) {
        if (method.equals("POST")) {
            return new Response(200, generator.generate(request));
        }else {
            return new MethodNotAllowedResponse();
        }
    }

    @Override
    public String getPath() {
        return "/generate";
    }
}
