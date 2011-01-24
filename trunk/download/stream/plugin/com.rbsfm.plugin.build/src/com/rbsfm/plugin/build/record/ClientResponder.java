package com.rbsfm.plugin.build.record;
import org.apache.commons.httpclient.HttpMethod;
public interface ClientResponder {
  public void respond(HttpMethod method);
}
