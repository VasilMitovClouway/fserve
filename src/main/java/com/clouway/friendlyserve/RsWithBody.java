package com.clouway.friendlyserve;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.Map;

/**
 * @author Miroslav Genov (miroslav.genov@clouway.com)
 */
public class RsWithBody extends RsWrap {

  public RsWithBody(InputStream in) {
    this(new RsEmpty(), in);
  }

  public RsWithBody(final Response response, final InputStream body) {
    super(new Response() {
      @Override
      public Status status() {
        return new Status(HttpURLConnection.HTTP_OK, "");
      }

      @Override
      public Map<String, String> header() throws IOException {
        return response.header();
      }
      @Override
      public InputStream body() throws IOException {
        return body;
      }
    });
  }
}
