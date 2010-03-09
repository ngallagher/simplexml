package com.rbsfm.plugin.build.rpc;
public interface ResponseListener {
   public void success(int code);
   public void exception(Throwable cause);
}
