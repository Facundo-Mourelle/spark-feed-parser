package httpRequest;

public final class HttpRequesterReddit extends HttpRequester {
    @Override
    protected Mtype getFormat() {
        return Mtype.JSON;
    }
}
