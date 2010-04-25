#region Using directives
using SimpleFramework.Xml.Core;
using SimpleFramework.Xml;
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   public class NamespaceTest : ValidationTestCase {
      @Root
      @NamespaceList({
         @Namespace(prefix="tax", reference="http://www.domain.com/tax"),
         @Namespace(reference="http://www.domain.com/default")
      })
      @Namespace(prefix="per", reference="http://www.domain.com/person")
      private static class Person {
         @Element
         private Profession job;
         @Element
         private String name;
         @Element
         private String value;
         @Attribute
         private int age;
         private Person() {
            super();
         }
         public Person(String name, String value, int age, Profession job) {
            this.name = name;
            this.value = value;
            this.age  = age;
            this.job = job;
         }
         public Profession Job {
            get {
               return job;
            }
         }
         //public Profession GetJob() {
         //   return job;
         //}
            return name;
         }
      }
      @Root
      @Namespace(prefix="jb", reference="http://www.domain.com/job")
      private static class Profession {
         @Element
         private String title;
         @Attribute
         @Namespace(reference="http://www.domain.com/tax")
         private int salary;
         @Attribute
         private int experience;
         @Namespace
         @Element
         private Employer employer;
         private Profession() {
            super();
         }
         public Profession(String title, int salary, int experience, Employer employer) {
            this.title = title;
            this.salary = salary;
            this.experience = experience;
            this.employer = employer;
         }
         public Employer Employer {
            get {
               return employer;
            }
         }
         //public Employer GetEmployer() {
         //   return employer;
         //}
            return title;
         }
      }
      @Root
      private static class Employer {
         @Element
         @Namespace(reference="http://www.domain.com/employer")
         private String name;
         @Element
         @Namespace(prefix="count", reference="http://www.domain.com/count")
         private int employees;
         @Element
         private String address;
         @Attribute
         private bool international;
         private Employer() {
            super();
         }
         public Employer(String name, String address, bool international, int employees) {
            this.name = name;
            this.employees = employees;
            this.address = address;
            this.international = international;
         }
         public String Address {
            get {
               return address;
            }
         }
         //public String GetAddress() {
         //   return address;
         //}
            return name;
         }
      }
      public void TestNamespace() {
         Persister persister = new Persister();
         Employer employer = new Employer("Spam Soft", "Sesame Street", true, 1000);
         Profession job = new Profession("Software Engineer", 10, 12, employer);
         Person person = new Person("John Doe", "Person", 30, job);
         persister.write(person, System.out);
         validate(persister, person);
      }
   }
}
