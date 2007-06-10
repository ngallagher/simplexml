

package simple.xml;

public @interface External {

   public String name() default "";        

   public Class<T extends Substitute> type();

   public boolean data() default false;

   public boolean required() default true;
}
