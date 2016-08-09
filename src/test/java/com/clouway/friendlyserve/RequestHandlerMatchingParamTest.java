package com.clouway.friendlyserve;

import com.clouway.friendlyserve.testing.FakeRequest;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.io.IOException;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;


/**
 * @author Miroslav Genov (miroslav.genov@clouway.com)
 */
public class RequestHandlerMatchingParamTest {

  @Test
  public void requestWithoutParameters() throws IOException {
    Optional<Response> possibleResponse = new RequestHandlerMatchingParam("param1", "value1", dummyHandler()).route(
            new FakeRequest("/test1", Collections.<String, String>emptyMap(), "test".getBytes())
    );
    assertThat(possibleResponse.isPresent(), is(equalTo(false)));
  }


  @Test
  public void paramIsMatching() throws IOException {
    Optional<Response> possibleResponse = new RequestHandlerMatchingParam("param1", "value1", dummyHandler()).route(new FakeRequest("/test1", ImmutableMap.of("param1", "value1"), "test".getBytes()));

    assertThat(possibleResponse.isPresent(), is(equalTo(true)));
  }

  @Test
  public void paramNameIsNotMatching() throws IOException {
    Optional<Response> possibleResponse = new RequestHandlerMatchingParam("==param1==", "value1", dummyHandler()).route(
            new FakeRequest("/test1", ImmutableMap.of("==another-parameter==", "value1"), "test".getBytes())
    );

    assertThat(possibleResponse.isPresent(), is(equalTo(false)));
  }

  @Test
  public void paramValueIsNotMatching() throws IOException {
    Optional<Response> possibleResponse = new RequestHandlerMatchingParam("==param==", "==value==", dummyHandler()).route(
            new FakeRequest("/test1", ImmutableMap.of("==param==", "==another-value=="), "test".getBytes())
    );

    assertThat(possibleResponse.isPresent(), is(equalTo(false)));
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