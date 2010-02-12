package org.simpleframework.xml.stream;

import javax.xml.stream.XMLEventReader;
import javax.xml.stream.events.XMLEvent;

class StreamReader implements EventReader {
   
   private final XMLEventReader reader;
   
   public StreamReader(XMLEventReader reader) {
      this.reader = reader;
   }

   public NodeEvent next() throws Exception {
      XMLEvent event = reader.nextEvent();
      
      if(event.isStartElement()) {
         return new StartEvent(event);
      } else if(event.isCharacters()) {
         return new CharacterEvent(event);
      } else if(event.isEndElement()) {
         return new EndEvent();
      }
      return next();
   }

   public NodeEvent peek() throws Exception {
      XMLEvent event = reader.peek();
      
      if(event.isStartElement()) {
         return new StartEvent(event);
      }else if(event.isCharacters()) {
         return new CharacterEvent(event);
      } else if(event.isEndElement()) {
         return new EndEvent();
      } else {
         reader.next();
      }
      return peek();
   }
}
