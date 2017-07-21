package de.dk.bininja.pack;

public class UnpackException extends Exception {
   private static final long serialVersionUID = -674636549881682329L;

   public UnpackException(String message) {
      super(message);
   }

   public UnpackException(Throwable cause) {
      super(cause);
   }

   public UnpackException(String message, Throwable cause) {
      super(message, cause);
   }

}