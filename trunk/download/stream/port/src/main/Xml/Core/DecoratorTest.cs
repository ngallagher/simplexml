#region Using directives
using SimpleFramework.Xml.Strategy;
using SimpleFramework.Xml.Stream;
using SimpleFramework.Xml;
using System.Collections.Generic;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   /// <summary>
   /// This test is provided to demonstrate how simple it is to intercept
   /// the serialization and deserialization process and manupulate the XML
   /// according to requirements. It also shows how the serialized XML can
   /// be written in a language neutral manner.
   /// </summary>
   public class DecoratorTest : ValidationTestCase {
       /// <summary>
       /// This is used to intercept the read and write operations and
       /// change the contents of the XML elements.
       /// </summary>
       public static interface Interceptor {
           public void Read(Class field, NodeMap<InputNode> node);
           public void Write(Class field,NodeMap<OutputNode> node);
       }
       /// <summary>
       /// This acts as a strategy and intercepts all XML elements that
       /// are serialized and deserialized so that the XML can be manipulated
       /// by the provided interceptor implementation.
       /// </summary>
       public static class Decorator : Strategy{
           private readonly Interceptor interceptor;
           private readonly Strategy strategy;
           public Decorator(Interceptor interceptor, Strategy strategy){
               this.interceptor = interceptor;
               this.strategy = strategy;
           }
           /// <summary>
           /// Here we intercept the call to get the element value from the
           /// strategy so that we can change the attributes in the XML element
           /// to match what was change on writing the element.
           /// </summary>
           /// <param name="node">
           /// this is the XML element to be modified
           /// </param>
           public Value Read(Type field, NodeMap<InputNode> node, Dictionary map) {
               interceptor.Read(field.getType(), node);
               return strategy.Read(field, node, map);
           }
           /// <summary>
           /// Here we change the XML element after it has been annotated by
           /// the strategy. In this way we can ensure we write what we want
           /// to the resulting XML document.
           /// </summary>
           /// <param name="node">
           /// this is the XML element that will be written
           /// </param>
           public bool Write(Type field, Object value, NodeMap<OutputNode> node, Dictionary map) {
               bool result = strategy.Write(field, value, node, map);
               interceptor.Write(field.getType(), node);
               return result;
           }
       }
       /// <summary>
       /// The manipulator object is used to manipulate the attributes
       /// added to the XML elements by the strategy in such a way that
       /// they do not contain Java class names but rather neutral ones.
       /// </summary>
       public static class Manipulator : Interceptor {
           private readonly Map<String, String> read;
           private readonly Map<String, String> write;
           private readonly String label;
           private readonly String replace;
           private Manipulator(String label, String replace) {
               this.read = new ConcurrentHashMap<String, String>();
               this.write = new ConcurrentHashMap<String, String>();
               this.label = label;
               this.replace = replace;
           }
           /// <summary>
           /// Here we are inserting an alias for a type. Each time the
           /// specified type is written the provided name is used and
           /// each time the name is found on reading it is substituted
           /// for the type so that it can be interpreted correctly.
           /// </summary>
           /// <param name="type">
           /// this is the class to be given an alias
           /// </param>
           /// <param name="name">
           /// this is the name to use
           /// </param>
           public void Resolve(Class type, String value) {
               String name = type.Name;
               read.put(value, name);
               write.put(name, value);
           }
           public void Read(Class field, NodeMap<InputNode> node) {
               InputNode value = node.remove(replace);
               if(value != null) {
                   String name = value.getValue();
                   String type = read.get(name);
                   if(type == null) {
                       throw new PersistenceException("Could not match name %s", name);
                   }
                   node.put(label, type);
               }
           }
           public void Write(Class field, NodeMap<OutputNode> node) {
               OutputNode value = node.remove(label);
               if(value != null) {
                   String type = value.getValue();
                   String name = write.get(type);
                   if(name == null) {
                       throw new PersistenceException("Could not match class %s", type);
                   }
                   node.put(replace, name);
               }
           }
       }
       [Root]
       public static class FriendList {
           private readonly @ElementList List<Friend> list;
           public FriendList(@ElementList(name="list") List<Friend> list) {
               this.list = list;
           }
           public List<Friend> Friends {
              get {
                  return list;
              }
           }
           //public List<Friend> GetFriends() {
           //    return list;
           //}
       [Root]
       public static class Friend {
           private readonly @Element Member member;
           private readonly @ElementList List<Message> messages;
           private readonly @Attribute Status status;
           public Friend(@Element(name="member") Member member, @ElementList(name="messages") List<Message> messages, @Attribute(name="status") Status status) {
               this.messages = messages;
               this.member = member;
               this.status = status;
           }
           public Member Member {
              get {
                  return member;
              }
           }
           //public Member GetMember() {
           //    return member;
           //}
               return status;
           }
           public List<Message> Messages {
              get {
                  return messages;
              }
           }
           //public List<Message> GetMessages() {
           //    return messages;
           //}
       [Root]
       public static class Member {
           private readonly @Element Address address;
           private readonly @Attribute String name;
           private readonly @Attribute int age;
           public Member(@Attribute(name="name") String name, @Attribute(name="age") int age, @Element(name="address") Address address) {
               this.address = address;
               this.name = name;
               this.age = age;
           }
           public bool IsPrivileged() {
               return false;
           }
           public Address Address {
              get {
                  return address;
              }
           }
           //public Address GetAddress() {
           //    return address;
           //}
               return name;
           }
           public int Age {
              get {
                  return age;
              }
           }
           //public int GetAge() {
           //    return age;
           //}
       [Root]
       public static class GoldMember : Member{
           public GoldMember(@Attribute(name="name") String name, @Attribute(name="age") int age, @Element(name="address") Address address) {
               super(name, age, address);
           }
           [Override]
           public bool IsPrivileged() {
               return true;
           }
       }
       [Root]
       public static class Person {
           private readonly @Element Address address;
           private readonly @Attribute String name;
           private readonly @Attribute int age;
           public Person(@Attribute(name="name") String name, @Attribute(name="age") int age, @Element(name="address") Address address) {
               this.address = address;
               this.name = name;
               this.age = age;
           }
           public Address Address {
              get {
                  return address;
              }
           }
           //public Address GetAddress() {
           //    return address;
           //}
               return name;
           }
           public int Age {
              get {
                  return age;
              }
           }
           //public int GetAge() {
           //    return age;
           //}
       [Root]
       public static class Address {
           private readonly @Element String street;
           private readonly @Element String city;
           private readonly @Element String country;
           public Address(@Element(name="street") String street, @Element(name="city") String city, @Element(name="country") String country) {
               this.street = street;
               this.city = city;
               this.country = country;
           }
           public String Street {
              get {
                  return street;
              }
           }
           //public String GetStreet() {
           //    return street;
           //}
               return city;
           }
           public String Country {
              get {
                  return country;
              }
           }
           //public String GetCountry() {
           //    return country;
           //}
       [Root]
       public static class Message {
           private String title;
           private String text;
           public Message() {
               super();
           }
           public Message(String title, String text){
               this.title = title;
               this.text = text;
           }
           [Attribute]
           public String Title {
              get {
                  return title;
              }
              set {
                  this.title = value;
              }
           }
           //public void SetTitle(String title) {
           //    this.title = title;
           //}
           //public String GetTitle() {
           //    return title;
           //}
           public String Text {
              get {
                  return text;
              }
              set {
                  this.text = value;
              }
           }
           //public void SetText(String text) {
           //    this.text = text;
           //}
           //public String GetText() {
           //    return text;
           //}
       public static enum Status {
           ACTIVE,
           INACTIVE,
           DELETED
       }
       /// <summary>
       /// This test will use an interceptor to replace Java class names
       /// with user specified tokens so that the object can be serialized
       /// and deserialized without referencing specific classes.
       /// </summary>
       public void TestDecorator() {
           Strategy strategy = new TreeStrategy("class", "length");
           Manipulator manipulator = new Manipulator("class", "type");
           Decorator decorator = new Decorator(manipulator, strategy);
           Serializer serializer = new Persister(decorator);
           List<Friend> friends = new ArrayList<Friend>();
           Address tomAddress = new Address("14 High Steet", "London", "UK");
           Member tom = new Member("Tom", 30, tomAddress);
           List<Message> tomMessages = new ArrayList<Message>();
           tomMessages.add(new Message("Hello", "Hi, this is a message, Bye"));
           tomMessages.add(new Message("Hi Tom", "This is another quick message"));
           Address jimAddress = new Address("14 Main Road", "London", "UK");
           Member jim = new GoldMember("Jim", 30, jimAddress);
           List<Message> jimMessages = new LinkedList<Message>();
           jimMessages.add(new Message("Hello Jim", "Hi Jim, here is a message"));
           jimMessages.add(new Message("Hi", "Yet another message"));
           friends.add(new Friend(tom, tomMessages, Status.ACTIVE));
           friends.add(new Friend(jim, jimMessages, Status.INACTIVE));
           FriendList original = new FriendList(friends);
           manipulator.Resolve(ArrayList.class, "list");
           manipulator.Resolve(LinkedList.class, "linked-list");
           manipulator.Resolve(Member.class, "member");
           manipulator.Resolve(GoldMember.class, "gold-member");
           StringWriter text = new StringWriter();
           serializer.Write(original, text);
           String result = text.toString();
           FriendList recovered = serializer.Read(FriendList.class, result);
           AssertEquals(original.Friends.getClass(), recovered.Friends.getClass());
           AssertEquals(original.Friends.get(0).Status, recovered.Friends.get(0).Status);
           AssertEquals(original.Friends.get(0).Member.Name, recovered.Friends.get(0).Member.Name);
           AssertEquals(original.Friends.get(0).Member.Age, recovered.Friends.get(0).Member.Age);
           AssertEquals(original.Friends.get(0).Member.Address.City, recovered.Friends.get(0).Member.Address.City);
           AssertEquals(original.Friends.get(0).Member.Address.Country, recovered.Friends.get(0).Member.Address.Country);
           AssertEquals(original.Friends.get(0).Member.Address.Street, recovered.Friends.get(0).Member.Address.Street);
           AssertEquals(original.Friends.get(1).Member.Name, recovered.Friends.get(1).Member.Name);
           AssertEquals(original.Friends.get(1).Member.Age, recovered.Friends.get(1).Member.Age);
           AssertEquals(original.Friends.get(1).Member.Address.City, recovered.Friends.get(1).Member.Address.City);
           AssertEquals(original.Friends.get(1).Member.Address.Country, recovered.Friends.get(1).Member.Address.Country);
           AssertEquals(original.Friends.get(1).Member.Address.Street, recovered.Friends.get(1).Member.Address.Street);
           validate(serializer, original);
       }
   }
}
