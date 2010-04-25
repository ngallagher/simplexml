#region License
//
// Caller.cs June 2007
//
// Copyright (C) 2007, Niall Gallagher <niallg@users.sf.net>
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
// implied. See the License for the specific language governing
// permissions and limitations under the License.
//
#endregion
#region Using directives
using System;
#endregion
namespace SimpleFramework.Xml.Core {
   /// <summary>
   /// The <c>Caller</c> acts as a means for the schema to invoke
   /// the callback methods on an object. This ensures that the correct
   /// method is invoked within the schema class. If the annotated method
   /// accepts a map then this will provide that map to the method. This
   /// also ensures that if specific annotation is not present in the
   /// class that no action is taken on a persister callback.
   /// </summary>
   class Caller {
      /// <summary>
      /// This is the pointer to the schema class commit function.
      /// </summary>
      private readonly Function commit;
      /// <summary>
      /// This is the pointer to the schema class validation function.
      /// </summary>
      private readonly Function validate;
      /// <summary>
      /// This is the pointer to the schema class persist function.
      /// </summary>
      private readonly Function persist;
      /// <summary>
      /// This is the pointer to the schema class complete function.
      /// </summary>
      private readonly Function complete;
      /// <summary>
      /// This is the pointer to the schema class replace function.
      /// </summary>
      private readonly Function replace;
      /// <summary>
      /// This is the pointer to the schema class resolve function.
      /// </summary>
      private readonly Function resolve;
      /// <summary>
      /// This is the context that is used to invoke the functions.
      /// </summary>
      private readonly Context context;
      /// <summary>
      /// Constructor for the <c>Caller</c> object. This is used
      /// to wrap the schema class such that callbacks from the persister
      /// can be dealt with in a seamless manner. This ensures that the
      /// correct function and arguments are provided to the functions.
      /// element and attribute XML annotations scanned from
      /// </summary>
      /// <param name="schema">
      /// this is the scanner that contains the functions
      /// </param>
      /// <param name="context">
      /// this is the context used to acquire the session
      /// </param>
      public Caller(Scanner schema, Context context) {
         this.validate = schema.getValidate();
         this.complete = schema.getComplete();
         this.replace = schema.getReplace();
         this.resolve = schema.getResolve();
         this.persist = schema.getPersist();
         this.commit = schema.getCommit();
         this.context = context;
      }
      /// <summary>
      /// This is used to replace the deserialized object with another
      /// instance, perhaps of a different type. This is useful when an
      /// XML schema class acts as a reference to another XML document
      /// which needs to be loaded externally to create an object of
      /// a different type.
      /// </summary>
      /// <param name="source">
      /// the source object to invoke the function on
      /// </param>
      /// <returns>
      /// this returns the object that acts as the replacement
      /// </returns>
      public Object Replace(Object source) {
         if(replace != null) {
            return replace.Call(context, source);
         }
         return source;
      }
      /// <summary>
      /// This is used to replace the deserialized object with another
      /// instance, perhaps of a different type. This is useful when an
      /// XML schema class acts as a reference to another XML document
      /// which needs to be loaded externally to create an object of
      /// a different type.
      /// </summary>
      /// <param name="source">
      /// the source object to invoke the function on
      /// </param>
      /// <returns>
      /// this returns the object that acts as the replacement
      /// </returns>
      public Object Resolve(Object source) {
         if(resolve != null) {
            return resolve.Call(context, source);
         }
         return source;
      }
      /// <summary>
      /// This method is used to invoke the provided objects commit function
      /// during the deserialization process. The commit function must be
      /// marked with the <c>Commit</c> annotation so that when the
      /// object is deserialized the persister has a chance to invoke the
      /// function so that the object can build further data structures.
      /// </summary>
      /// <param name="source">
      /// this is the object that has just been deserialized
      /// </param>
      public void Commit(Object source) {
         if(commit != null) {
            commit.Call(context, source);
         }
      }
      /// <summary>
      /// This method is used to invoke the provided objects validation
      /// function during the deserialization process. The validation function
      /// must be marked with the <c>Validate</c> annotation so that
      /// when the object is deserialized the persister has a chance to
      /// invoke that function so that object can validate its field values.
      /// </summary>
      /// <param name="source">
      /// this is the object that has just been deserialized
      /// </param>
      public void Validate(Object source) {
         if(validate != null) {
            validate.Call(context, source);
         }
      }
      /// <summary>
      /// This method is used to invoke the provided objects persistence
      /// function. This is invoked during the serialization process to
      /// get the object a chance to perform an necessary preparation
      /// before the serialization of the object proceeds. The persist
      /// function must be marked with the <c>Persist</c> annotation.
      /// </summary>
      /// <param name="source">
      /// the object that is about to be serialized
      /// </param>
      public void Persist(Object source) {
         if(persist != null) {
            persist.Call(context, source);
         }
      }
      /// <summary>
      /// This method is used to invoke the provided objects completion
      /// function. This is invoked after the serialization process has
      /// completed and gives the object a chance to restore its state
      /// if the persist function required some alteration or locking.
      /// This is marked with the <c>Complete</c> annotation.
      /// </summary>
      /// <param name="source">
      /// this is the object that has been serialized
      /// </param>
      public void Complete(Object source) {
         if(complete != null) {
            complete.Call(context, source);
         }
      }
   }
}
