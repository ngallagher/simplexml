package org.simpleframework.xml.benchmark.asm.accessor;

import static org.objectweb.asm.Opcodes.ACC_BRIDGE;
import static org.objectweb.asm.Opcodes.ACC_PUBLIC;
import static org.objectweb.asm.Opcodes.ACC_SUPER;
import static org.objectweb.asm.Opcodes.ACC_SYNTHETIC;
import static org.objectweb.asm.Opcodes.ALOAD;
import static org.objectweb.asm.Opcodes.ARETURN;
import static org.objectweb.asm.Opcodes.CHECKCAST;
import static org.objectweb.asm.Opcodes.GETFIELD;
import static org.objectweb.asm.Opcodes.INVOKESPECIAL;
import static org.objectweb.asm.Opcodes.INVOKEVIRTUAL;
import static org.objectweb.asm.Opcodes.PUTFIELD;
import static org.objectweb.asm.Opcodes.RETURN;
import static org.objectweb.asm.Opcodes.V1_6;

import java.net.URLClassLoader;

import org.objectweb.asm.AnnotationVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.FieldVisitor;
import org.objectweb.asm.MethodVisitor;
//import org.objectweb.asm.util.ASMifierClassVisitor;

/*
 
 
 
public class customer_Field implements PropertyAccessor<PurchaseOrder, Customer> {
   
   public void set(PurchaseOrder order, Customer customer) { 
      order.customer = customer; 
   }   
   public Customer get(PurchaseOrder order) { 
      return order.customer; 
   } 
} 

 */
public class AccessorFactory {

   /*
     You'll also have to create synthetic accessors using ASM's visitor classes:
    
    
{
mv = cw.visitMethod(ACC_STATIC + ACC_SYNTHETIC, "access$0",  
          "(Linject/PurchaseOrder;Linject/Customer;)V", null, null); 
mv.visitCode(); 
mv.visitVarInsn(ALOAD, 0); 
mv.visitVarInsn(ALOAD, 1); 
mv.visitFieldInsn(PUTFIELD, "inject/PurchaseOrder", 
          "customer", "Linject/Customer;"); 
mv.visitInsn(RETURN); 
mv.visitMaxs(2, 2); 
mv.visitEnd(); 
} 
{ 
mv = cw.visitMethod(ACC_STATIC + ACC_SYNTHETIC, "access$1",  
          "(Linject/PurchaseOrder;)Linject/Customer;", null, null); 
mv.visitCode(); 
mv.visitVarInsn(ALOAD, 0); 
mv.visitFieldInsn(GETFIELD, "inject/PurchaseOrder", " 
          customer", "Linject/Customer;"); 
mv.visitInsn(ARETURN); 
mv.visitMaxs(1, 1); 
mv.visitEnd(); 
} 

    */
   public static void main(String[] list) throws Exception {
      //ASMifierClassVisitor.main(new String[] { customer_Field.class.getName() }); 
      Class type = loadClass();
      PropertyAccessor accessor = (PropertyAccessor)type.newInstance();
      Customer customer = new Customer();
      PurchaseOrder order = new PurchaseOrder();
      accessor.set(order, customer);
      if(customer != order.getCustomer()) {
         throw new IllegalStateException("Dynamic code failed");
      } else {
         System.out.println("SUCCESS");
      }
      
   }
   
   public static Class loadClass() throws Exception {
      ASMClassLoader loader = new ASMClassLoader("org.simpleframework.xml.benchmark.asm.accessor.customer_Field", dump());
      return loader.loadClass("org.simpleframework.xml.benchmark.asm.accessor.customer_Field");      
   }
   
   public static byte[] dump () throws Exception {

      ClassWriter cw = new ClassWriter(0);
      FieldVisitor fv;
      MethodVisitor mv;
      AnnotationVisitor av0;

      cw.visit(V1_6, ACC_PUBLIC + ACC_SUPER, "org/simpleframework/xml/benchmark/asm/accessor/customer_Field", "Ljava/lang/Object;Lorg/simpleframework/xml/benchmark/asm/accessor/PropertyAccessor<Lorg/simpleframework/xml/benchmark/asm/accessor/PurchaseOrder;Lorg/simpleframework/xml/benchmark/asm/accessor/Customer;>;", "java/lang/Object", new String[] { "org/simpleframework/xml/benchmark/asm/accessor/PropertyAccessor" });

      {
      mv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null);
      mv.visitCode();
      mv.visitVarInsn(ALOAD, 0);
      mv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V");
      mv.visitInsn(RETURN);
      mv.visitMaxs(1, 1);
      mv.visitEnd();
      }
      {
      mv = cw.visitMethod(ACC_PUBLIC, "set", "(Lorg/simpleframework/xml/benchmark/asm/accessor/PurchaseOrder;Lorg/simpleframework/xml/benchmark/asm/accessor/Customer;)V", null, null);
      mv.visitCode();
      mv.visitVarInsn(ALOAD, 1);
      mv.visitVarInsn(ALOAD, 2);
      mv.visitFieldInsn(PUTFIELD, "org/simpleframework/xml/benchmark/asm/accessor/PurchaseOrder", "customer", "Lorg/simpleframework/xml/benchmark/asm/accessor/Customer;");
      mv.visitInsn(RETURN);
      mv.visitMaxs(2, 3);
      mv.visitEnd();
      }
      {
      mv = cw.visitMethod(ACC_PUBLIC, "get", "(Lorg/simpleframework/xml/benchmark/asm/accessor/PurchaseOrder;)Lorg/simpleframework/xml/benchmark/asm/accessor/Customer;", null, null);
      mv.visitCode();
      mv.visitVarInsn(ALOAD, 1);
      mv.visitFieldInsn(GETFIELD, "org/simpleframework/xml/benchmark/asm/accessor/PurchaseOrder", "customer", "Lorg/simpleframework/xml/benchmark/asm/accessor/Customer;");
      mv.visitInsn(ARETURN);
      mv.visitMaxs(1, 2);
      mv.visitEnd();
      }
      {
      mv = cw.visitMethod(ACC_PUBLIC + ACC_BRIDGE + ACC_SYNTHETIC, "get", "(Ljava/lang/Object;)Ljava/lang/Object;", null, null);
      mv.visitCode();
      mv.visitVarInsn(ALOAD, 0);
      mv.visitVarInsn(ALOAD, 1);
      mv.visitTypeInsn(CHECKCAST, "org/simpleframework/xml/benchmark/asm/accessor/PurchaseOrder");
      mv.visitMethodInsn(INVOKEVIRTUAL, "org/simpleframework/xml/benchmark/asm/accessor/customer_Field", "get", "(Lorg/simpleframework/xml/benchmark/asm/accessor/PurchaseOrder;)Lorg/simpleframework/xml/benchmark/asm/accessor/Customer;");
      mv.visitInsn(ARETURN);
      mv.visitMaxs(2, 2);
      mv.visitEnd();
      }
      {
      mv = cw.visitMethod(ACC_PUBLIC + ACC_BRIDGE + ACC_SYNTHETIC, "set", "(Ljava/lang/Object;Ljava/lang/Object;)V", null, null);
      mv.visitCode();
      mv.visitVarInsn(ALOAD, 0);
      mv.visitVarInsn(ALOAD, 1);
      mv.visitTypeInsn(CHECKCAST, "org/simpleframework/xml/benchmark/asm/accessor/PurchaseOrder");
      mv.visitVarInsn(ALOAD, 2);
      mv.visitTypeInsn(CHECKCAST, "org/simpleframework/xml/benchmark/asm/accessor/Customer");
      mv.visitMethodInsn(INVOKEVIRTUAL, "org/simpleframework/xml/benchmark/asm/accessor/customer_Field", "set", "(Lorg/simpleframework/xml/benchmark/asm/accessor/PurchaseOrder;Lorg/simpleframework/xml/benchmark/asm/accessor/Customer;)V");
      mv.visitInsn(RETURN);
      mv.visitMaxs(3, 3);
      mv.visitEnd();
      }
      cw.visitEnd();

      return cw.toByteArray();
      }   
}
