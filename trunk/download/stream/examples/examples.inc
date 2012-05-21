<p>
Blah blah!!
</p>
<table border='0'><tr><td><img src='/images/home_bullet.gif' border='0'></td><td><a name='optional'></a><b>Using paths to create nested elements</b></td></tr><tr><td><!-- padding --></td><td>
<p>
Here is an example of how to use the @Path annotation to create nested XML elements.
</p>
<pre class="sh_java">

   @Root
   public class Example {
      
      @Path("a/b[1]")
      @Element
      private String x;
      
      @Path("a/b[2]")
      @Element
      private String y;
   }
   
</pre>

<pre class="sh_xml">
   
   &lt;example&gt;
     &lt;a&gt;
       &lt;b&gt;
         &lt;x&gt;foo&lt;/x&gt;
       &lt;/b&gt;
       &lt;b&gt;
         &lt;y&gt;bar&lt;/y&gt;
       &lt;/b&gt;
     &lt;/a&gt;
   &lt;/example&gt;

</pre>
This example can be downloaded from <a href='example1/example1.zip'>here</a>.
</td></tr></table><table border='0'><tr><td><img src='/images/home_bullet.gif' border='0'></td><td><a name='optional'></a><b>Using paths to create nested elements</b></td></tr><tr><td><!-- padding --></td><td>
<p>
Here is an example of how to use the @Path annotation to create nested XML elements.
</p>
<pre class="sh_java">

   @Root
   public class Example {
      
      @ElementUnion({
         @Element(name="text", type=String.class),
         @Element(name="int", type=Integer.class),
         @Element(name="double", type=Double.class)
      })
      private Object value;
   }
   
</pre>

<pre class="sh_xml">
   
   &lt;example&gt;
     &lt;int&gt;12&lt;/int&gt;
   &lt;/example&gt;

</pre>
This example can be downloaded from <a href='example2/example2.zip'>here</a>.
</td></tr></table><table border='0'><tr><td><img src='/images/home_bullet.gif' border='0'></td><td><a name='optional'></a><b>Using paths to create nested elements</b></td></tr><tr><td><!-- padding --></td><td>
<p>
Here is an example of how to use the @Path annotation to create nested XML elements.
</p>
<pre class="sh_java">

   @Root
   public class Point {
      
      @Element
      private final int x;
      
      @Element
      private final int y;
      
      public Point(@Element(name="x") int x, @Element(name="y") int y) {
         this.x = x;
         this.y = y;
      }
   }
   
</pre>

<pre class="sh_xml">
   
   &lt;point&gt;
     &lt;x&gt;10&lt;/x&gt;
     &lt;y&gt;4&lt;/y&gt;
   &lt;/point&gt;

</pre>
This example can be downloaded from <a href='example3/example3.zip'>here</a>.
</td></tr></table><table border='0'><tr><td><img src='/images/home_bullet.gif' border='0'></td><td><a name='optional'></a><b>Using paths to create nested elements</b></td></tr><tr><td><!-- padding --></td><td>
<p>
Here is an example of how to use the @Path annotation to create nested XML elements.
</p>
<pre class="sh_java">

   @Root
   public class Point {
      
      @Path("a/b[1]")
      @Element
      private final int x;
      
      @Path("a/b[1]")
      @Element
      private final int y;
      
      public Point(@Element(name="x") int x, @Element(name="y") int y) {
         this.x = x;
         this.y = y;
      }
   }
   
</pre>

<pre class="sh_xml">
   
   &lt;point&gt;
     &lt;a&gt;
       &lt;b&gt;
         &lt;x&gt;2&lt;/x&gt;
         &lt;y&gt;7&lt;/y&gt;
       &lt;/b&gt;
     &lt;/a&gt;
   &lt;/point&gt;

</pre>
This example can be downloaded from <a href='example4/example4.zip'>here</a>.
</td></tr></table><table border='0'><tr><td><img src='/images/home_bullet.gif' border='0'></td><td><a name='optional'></a><b>Using paths to create nested elements</b></td></tr><tr><td><!-- padding --></td><td>
<p>
Here is an example of how to use the @Path annotation to create nested XML elements.
</p>
<pre class="sh_java">

   @Root
   public class Example {
      
      @Namespace(reference="http://www.blah.com/ns/a")
      @Element
      private String a;
      
      @Namespace(reference="http://www.blah.com/ns/b")
      @Element
      private String b;
   }
   
</pre>

<pre class="sh_xml">
   
   &lt;example&gt;
     &lt;a xmlns="http://www.blah.com/ns/a"&gt;foo&lt;/a&gt;
     &lt;b xmlns="http://www.blah.com/ns/b"&gt;bar&lt;/b&gt;
   &lt;/example&gt;

</pre>
This example can be downloaded from <a href='example5/example5.zip'>here</a>.
</td></tr></table><table border='0'><tr><td><img src='/images/home_bullet.gif' border='0'></td><td><a name='optional'></a><b>Using paths to create nested elements</b></td></tr><tr><td><!-- padding --></td><td>
<p>
Here is an example of how to use the @Path annotation to create nested XML elements.
</p>
<pre class="sh_java">

   @Root
   public class Example {
      
      @Namespace(prefix="ns1", reference="http://www.blah.com/ns/a")
      @Element
      private String a;
      
      @Namespace(prefix="ns2", reference="http://www.blah.com/ns/b")
      @Element
      private String b;
   }
   
</pre>

<pre class="sh_xml">
   
   &lt;example&gt;
     &lt;ns1:a xmlns:ns1="http://www.blah.com/ns/a"&gt;foo&lt;/ns1:a&gt;
     &lt;ns2:b xmlns:ns2="http://www.blah.com/ns/b"&gt;bar&lt;/ns2:b&gt;
   &lt;/example&gt;

</pre>
This example can be downloaded from <a href='example6/example6.zip'>here</a>.
</td></tr></table><table border='0'><tr><td><img src='/images/home_bullet.gif' border='0'></td><td><a name='optional'></a><b>Using paths to create nested elements</b></td></tr><tr><td><!-- padding --></td><td>
<p>
Here is an example of how to use the @Path annotation to create nested XML elements.
</p>
<pre class="sh_java">

   @Root
   @Namespace(prefix="ns1", reference="http://www.blah.com/ns/a")
   public class Example {
      
      @Namespace(reference="http://www.blah.com/ns/a")
      @Path("a/b")
      @Element
      private String x;
   }
   
</pre>

<pre class="sh_xml">
   
   &lt;example xmlns:ns1="http://www.blah.com/ns/a"&gt;
     &lt;a&gt;
       &lt;b&gt;
         &lt;ns1:x&gt;blah&lt;/ns1:x&gt;
       &lt;/b&gt;
     &lt;/a&gt;
   &lt;/example&gt;

</pre>
This example can be downloaded from <a href='example7/example7.zip'>here</a>.
</td></tr></table>
