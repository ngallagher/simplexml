@Root(name="message",
      strict=false)
public class Message {

   private Collection<Entry> list;
         
   @Attribute(name="version")        
   private int version;        

   @ElementList(name="list",
                type=Entry.class,
                required=true)
   public void setList(Collection<Entry> entry) {
      this.entry = entry;           
   }        

   @ElementList(name="list",
                type=Entry.class,
                required=true)
   public Collection<Entry> getList() {
      return entry;           
   }
}

@Root(name="entry")
public class Entry {

   @Attribute(name="name")        
   public String name;    

   public String text;   

   @Text
   public String getText() {
      return text;           
   }

   @Text
   public void setText(String text){
      this.text = text;           
   }
   
   public String getName() {
      return name;           
   }
}

<message>
   <list>
      <entry name="a">Example text one</entry>
      <entry name="b">Example text two</entry>
      </entry>
   </list>
</message>
