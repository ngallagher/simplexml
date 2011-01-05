package com.rbsfm.plugin.build.rpc;
public interface ResponseListener {
   public void success(String message);
   public void exception(Throwable cause);
}
