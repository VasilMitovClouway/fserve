package com.clouway.friendlyserve;

import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;


/**
 * @author Miroslav Genov (miroslav.genov@clouway.com)
 */
public class FkParamsTest {

  @Test
  public void requestWithoutParameters() throws IOException {
    Optional<Response> opt = new FkParams("param1", "value1", dummyHandler()).route(
            new ByteRequest("/test1", Collections.<String, String>emptyMap(), "test".getBytes())
    );
    assertThat(opt.isPresent(), is(equalTo(false)));
  }


  @Test
  public void paramIsMatching() throws IOException {
    Optional<Response> opt = new FkParams("param1", "value1", dummyHandler()).route(new ByteRequest("/test1", ImmutableMap.of("param1", "value1"), "test".getBytes()));

    assertThat(opt.isPresent(), is(equalTo(true)));
  }

  @Test
  public void paramNameIsNotMatching() throws IOException {
    Optional<Response> opt = new FkParams("==param1==", "value1", dummyHandler()).route(
            new ByteRequest("/test1", ImmutableMap.of("==another-parameter==", "value1"), "test".getBytes())
    );

    assertThat(opt.isPresent(), is(equalTo(false)));
  }

  @Test
  public void paramValueIsNotMatching() throws IOException {
    Optional<Response> opt = new FkParams("==param==", "==value==", dummyHandler()).route(
            new ByteRequest("/test1", ImmutableMap.of("==param==", "==another-value=="), "test".getBytes())
    );

    assertThat(opt.isPresent(), is(equalTo(false)));
  }

  private Take dummyHandler() {
    return new Take() {
      @Override
      public Response ack(Request request) throws IOException {
        return new RsText("test");
      }
    };
  }

}