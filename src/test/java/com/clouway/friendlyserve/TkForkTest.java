package com.clouway.friendlyserve;

import com.clouway.friendlyserve.testing.FakeRequest;
import com.google.common.base.Optional;
import com.google.common.collect.ImmutableMap;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.startsWith;
import static org.junit.Assert.fail;


/**
 * @author Miroslav Genov (miroslav.genov@clouway.com)
 */
public class TkForkTest {

  @Test
  public void oneRoute() throws IOException {
    TkFork fork = new TkFork(new Fork() {
      @Override
      public Optional<Response> route(Request request) throws IOException {
        return Optional.<Response>of(new RsText("text message"));
      }
    });

    Response response = fork.ack(new FakeRequest("/test", ImmutableMap.<String, String>of(), "test".getBytes()));
    assertThat(new RsPrint(response).printBody(), startsWith("text message"));
  }

  @Test
  public void manyRoutes() throws IOException {

    TkFork fork = new TkFork(
            new Fork() {
              @Override
              public Optional<Response> route(Request request) throws IOException {
                return Optional.absent();
              }
            },
            new Fork() {
              @Override
              public Optional<Response> route(Request request) throws IOException {
                return Optional.<Response>of(new RsText("another message"));
              }
            }
    );

    Response response = fork.ack(new FakeRequest("/another", ImmutableMap.<String, String>of(), "test".getBytes()));
    assertThat(new RsPrint(response).printBody(), startsWith("another message"));
  }

  @Test
  public void noRoute() throws IOException {

    TkFork fork = new TkFork(new Fork() {
      @Override
      public Optional<Response> route(Request request) throws IOException {
        return Optional.absent();
      }
    });

    try {
      fork.ack(new FakeRequest("/test", ImmutableMap.<String, String>of(), "test".getBytes()));
      fail("exception wasn't throw when route was not found?");
    } catch (HttpException e) {
      assertThat(e.code(), is(404));
    }

  }

}