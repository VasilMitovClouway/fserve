package com.clouway.friendlyserve.testing;

import com.clouway.friendlyserve.Request;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Lists;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;

public class FakeRequest implements Request {

  private final Map<String, String> params;
  private final Map<String, String> headers;
  private final byte[] body;
  private final String path;

  public FakeRequest(String path, Map<String, String> params, Map<String, String> headers, byte[] content) {
    this.path = path;
    this.params = ImmutableMap.copyOf(params);
    this.body = Arrays.copyOf(content, content.length);
    this.headers = ImmutableMap.copyOf(headers);
  }

  public FakeRequest(Map<String, String> params, Map<String, String> headers) {
    this("/", params, headers, new byte[]{});
  }

  public FakeRequest(String path, Map<String, String> params, byte[] content) {
    this(path, params, new LinkedHashMap<String, String>(), content);
  }


  @Override
  public String path() {
    return path;
  }

  @Override
  public String param(String name) {
    return params.get(name);
  }

  @Override
  public Iterable<String> names() {
    return params.keySet();
  }

  @Override
  public Iterable<String> cookie(String name) {
    return Lists.newLinkedList();
  }

  @Override
  public String header(String name) {
    return headers.get(name);
  }

  @Override
  public InputStream body() {
    return new ByteArrayInputStream(body);
  }
}