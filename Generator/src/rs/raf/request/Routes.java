package rs.raf.request;

public class Routes {

    public static RequestHandler[] get() {
        return new RequestHandler[]{
                new GenerateHandler()
        };
    }
}
