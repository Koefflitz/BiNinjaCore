package de.dk.bininja.net;

public class ConnectionRefusedException extends Exception {
   private static final long serialVersionUID = -1861930427787452128L;

   public ConnectionRefusedException(String message) {
      super(message);
   }

   public ConnectionRefusedException(Throwable cause) {
      super(cause);
   }

   public ConnectionRefusedException(String message, Throwable cause) {
      super(message, cause);
   }

}
