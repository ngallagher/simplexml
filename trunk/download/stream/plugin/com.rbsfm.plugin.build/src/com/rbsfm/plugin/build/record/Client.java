package com.rbsfm.plugin.build.record;
import org.simpleframework.http.Request;
public interface Client {
  public void handle(ClientResponder responder, Request request) throws Exception;
}
