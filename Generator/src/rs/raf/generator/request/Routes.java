package rs.raf.generator.request;

public class Routes {

    public static RequestHandler[] get() {
        return new RequestHandler[]{
                new GenerateHandler()
        };
    }
}
