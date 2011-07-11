package org.simpleframework.xml;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class TestActivity extends Activity {
   public void onCreate(Bundle bundle) {
      TestRunner runner = new TestRunner("org.simpleframework.xml.core.TestCaseSuite"); 
      TextView view = new TextView(this);
      try {
         String result = runner.test();
         view.setText(result);
         setContentView(view);
      } catch(Exception e) {
         throw new RuntimeException(e);
      }
   }
}