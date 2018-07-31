package de.dk.bininja;

import de.dk.opt.ex.ArgumentParseException;

/**
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 */
public class InvalidArgumentException extends ArgumentParseException {
   private static final long serialVersionUID = 126652555748006069L;

   public InvalidArgumentException(String message) {
      super(message);
   }

   public InvalidArgumentException(Throwable cause) {
      super(cause);
   }

   public InvalidArgumentException(String message, Throwable cause) {
      super(message, cause);
   }
}
