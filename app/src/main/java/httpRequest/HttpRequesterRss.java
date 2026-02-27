package httpRequest;

public final class HttpRequesterRss extends HttpRequester {
    @Override
    protected Mtype getFormat() {
        return Mtype.XML;
    }

}
