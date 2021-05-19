package rs.raf.response;

import java.util.Objects;

public class Response {

    private final int code;
    private final String content;

    public Response(int code, String content) {
        this.code = code;
        this.content = content;
    }

    public int getCode() {
        return code;
    }

    public String getContent() {
        return content;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Response) {
            Response r = (Response) obj;
            return getCode() == r.getCode() && getContent().equals(r.getContent());
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCode(), getContent());
    }

    @Override
    public String toString() {
        return getCode() + " " + getContent();
    }
}
