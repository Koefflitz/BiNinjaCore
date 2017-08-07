package de.dk.bininja.ui.cli;

/**
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 */
public class CliCommandResult {
   private final boolean worked;
   private final String message;

   public CliCommandResult(boolean worked, String message) {
      this.worked = worked;
      this.message = message;
   }

   public String getMessage() {
      return message;
   }

   public boolean worked() {
      return worked;
   }
}
