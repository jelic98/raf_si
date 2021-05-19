package rs.raf.response;

public class MethodNotAllowedResponse extends Response {

    public MethodNotAllowedResponse() {
        super(405, "Method Not Allowed");
    }
}
