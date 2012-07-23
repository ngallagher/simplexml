package com.yieldbroker.common.swing.ui.table;

import com.yieldbroker.common.swing.ui.WidgetException;

public class TableException extends WidgetException {

   public TableException(Throwable cause) {
      super(cause);
   }

   public TableException(String message, Object... arguments) {
      super(message, arguments);
   }
}
