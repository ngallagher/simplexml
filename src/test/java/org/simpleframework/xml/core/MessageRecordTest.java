package org.simpleframework.xml.core;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import junit.framework.TestCase;

public class MessageRecordTest extends TestCase {
   
   public void testRecorder() {
      List<String> key = Arrays.asList("instrumentId");
      List<String> values = Arrays.asList("instrumentId", "futureBench", "futureSpread");
      Map<String, String> a = new HashMap<String, String>();
      Map<String, String> b = new HashMap<String, String>();
      Map<String, String> c = new HashMap<String, String>();
      Map<String, String> d = new HashMap<String, String>();
      Map<String, String> replaceD = new HashMap<String, String>();
      Map<String, String> replaceC = new HashMap<String, String>();
      a.put("instrumentId", "A");
      a.put("futureBench", "YMA");
      a.put("futureSpread", "0.4");
      b.put("instrumentId", "B");
      b.put("futureBench", "XMA");
      b.put("futureSpread", "0.1");
      c.put("instrumentId", "C");
      c.put("futureBench", "YMA");
      c.put("futureSpread", "0.456");
      d.put("instrumentId", "D");
      d.put("futureBench", "YMA");
      d.put("futureSpread", "0.9");
      replaceD.put("instrumentId", "D");
      replaceD.put("futureBench", "YMA");
      replaceD.put("futureSpread", "0.777");
      replaceC.put("instrumentId", "C");
      replaceC.put("futureBench", "YMA");
      replaceC.put("futureSpread", "0.777");
      SimpleMessageRecorder recorder = new SimpleMessageRecorder(key, values, 3);
      recorder.recordMessage("a",a);
      recorder.recordMessage("b",b);
      recorder.recordMessage("c",c);
      recorder.recordMessage("d",d);
      recorder.recordMessageError("b", "Error sending B");
      System.out.println(recorder.dump());
      recorder.recordMessage("replaceD",replaceD);
      System.out.println(recorder.dump());
      recorder.recordMessage("replaceC",replaceC);
      System.out.println(recorder.dump());
   }
   
   public static interface MessageRecorder {
      public void recordMessage(String uniqueKey, Map<String, String> attributes);
      public void recordMessageError(String uniqueKey, String status);
   }
   
   public static class SimpleMessageRecorder implements MessageRecorder {
      private final Map<Key, MessageRecord> records;
      private final List<String> values;
      private final List<String> key;
      private final MessageRecordKeyMapper mapper;
      private final MessageRecordBuilder builder;
      private final KeyCreator creator;
      public SimpleMessageRecorder(List<String> key, List<String> values, int capacity) {
         this.mapper = new MessageRecordKeyMapper();
         this.records = new LeastRecentlyUsedMap<Key, MessageRecord>(mapper, capacity);
         this.builder = new MessageRecordBuilder(values);
         this.creator = new KeyCreator(key);
         this.values = values;
         this.key = key;
      }
      public synchronized String dump() {
         DateFormat format = new SimpleDateFormat("dd-MMM-yyyy HH:mm:ss");
         StringBuilder builder = new StringBuilder();
         builder.append("<TABLE BORDER='1'>\n");
         for(String value : key) {
            builder.append("<TH>").append(value).append("</TH>");
         }
         for(String value : values) {
            if(key.contains(value)) {
               builder.append("<TH>").append(value).append("</TH>");
            }
         }
         builder.append("<TH>status</TH>");
         builder.append("<TH>time</TH>\n");
         Set<Key> set = records.keySet();
         for(Key key : set) {
            MessageRecord record = records.get(key);
            List<String> keys = key.getValues();
            for(String value : keys) {
               builder.append("<TD>").append(value).append("</TD>");
            }
            List<String> values = record.getValues();
            for(String value : values) {
               if(!keys.contains(value)) {
                  builder.append("<TD>").append(value).append("</TD>");
               }
            }
            String status = record.getStatus();
            Date timeStamp = record.getTimeStamp();
            String date = format.format(timeStamp);
            builder.append("<TD>");
            if(status != null) {
               builder.append(status);
            }
            builder.append("</TD>");           
            builder.append("<TD>").append(date).append("</TD>\n");
         }
         builder.append("</TABLE>");
         return builder.toString();
      }
      public synchronized void recordMessageError(String uniqueKey, String status) {
         Key key = mapper.findKey(uniqueKey);
         MessageRecord record = records.get(key);
         if(record != null) {
            record.setStatus(status);
         }
      }
      public synchronized void recordMessage(String uniqueKey, Map<String, String> attributes) {
         Key key = creator.create(attributes);
         MessageRecord record = builder.build(attributes, key);
         records.put(key, record);
         mapper.cache(key, uniqueKey);
      }
      
      private static class MessageRecordKeyMapper implements LeastRecentlyUsedMap.RemovalListener<Key> {        
         private final Map<Key, String> keyMap;
         private final Map<String, Key> uniqueKeyMap;
         public MessageRecordKeyMapper() {
            this.uniqueKeyMap = new HashMap<String, Key>();
            this.keyMap = new HashMap<Key, String>();
         }
         public Key findKey(String uniqueKey) {
            return uniqueKeyMap.get(uniqueKey);
         }
         public void cache(Key key, String uniqueKey) {
            keyMap.put(key, uniqueKey);
            uniqueKeyMap.put(uniqueKey, key);
         }
         public void notifyRemoved(Key key) {
            String uniqueKey = keyMap.remove(key);
            if(uniqueKey != null) {
               uniqueKeyMap.remove(uniqueKey);
            }
         }
         
      }
   }
   
   private static class LeastRecentlyUsedMap<K, V> extends LinkedHashMap<K, V> {
      private final RemovalListener<K> listener;
      private final int capacity;
      public LeastRecentlyUsedMap(RemovalListener<K> listener, int capacity){
         this.listener = listener;
         this.capacity = capacity;
      }
      @Override
      public boolean removeEldestEntry(Map.Entry<K, V> entry) {
         K key = entry.getKey();
         int size = size();  
         if(size > capacity) {
            listener.notifyRemoved(key);
            return true;
         }
         return false;
      }
      public static interface RemovalListener<K> {
         public void notifyRemoved(K key);
      }
   }
   
   private static class MessageRecordBuilder {
      private final List<String> values;
      public MessageRecordBuilder(List<String> values) {
         this.values = values;
      }
      public MessageRecord build(Map<String, String> attributes, Key key) {
         List<String> list = new ArrayList<String>();
         for(String name : values) {
            String value = attributes.get(name);
            list.add(value);
         }
         return new MessageRecord(list, key);
      }
   }
   
   private static class MessageRecord {
      private List<String> values;
      private Key key;
      private String status;
      private Date timeStamp;
      public MessageRecord(List<String> values, Key key) {
         this.timeStamp = new Date();
         this.values = values;
         this.key = key;
      }
      public void setStatus(String status) {
         this.status = status;
      }
      public Date getTimeStamp() {
         return timeStamp;
      }
      public String getStatus() {
         return status;
      }
      public List<String> getValues() {
         return Collections.unmodifiableList(values);
      }
      public Key getKey() {
         return key;
      }
   }
   
   private static class KeyCreator {
      private final List<String> keys;
      private final int size;
      public KeyCreator(List<String> keys) {
         this.size = keys.size();
         this.keys = keys;
      }
      public Key create(Map<String, String> attributes) {
         String[] list = new String[size];
         int hash = 1;
         for(int i = 0; i < size; i++) {
            String key = keys.get(i);
            String value = attributes.get(key);
            if(value != null) {
               hash = 31 * hash + value.hashCode();
               list[i] = value;
            }
         }
         return new Key(list, hash);
      }
   }
   
   private static class Key {
      private final String[] list;
      private int hash;
      private Key(String[] list, int hash) {
         this.list = list;
         this.hash = hash;
      }
      public List<String> getValues() {
         return Arrays.asList(list);
      }
      public int hashCode() {
         return hash;
      }
      public boolean equals(Object value) {
         if(value instanceof Key) {
            return equals((Key)value);
         }
         return false;
      }
      public boolean equals(Key key) {
         if(key.list.length == list.length) {
            for(int i = 0; i < list.length; i++) {
               if(!key.list[i].equals(list[i])) {
                  return false;
               }
               return true;
            }
         }
         return false;
         
      }
   } 
}
