package de.dk.bininja.ui.cli;

/**
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 */
public class CliCommandResult {
   private final boolean commandCorrect;
   private final String message;
   private boolean block;
   private long timeout;

   public CliCommandResult(boolean commandCorrect, String message, boolean block, long timeout) {
      this.commandCorrect = commandCorrect;
      this.message = message;
      this.block = block;
      this.timeout = timeout;
   }

   public CliCommandResult(boolean commandCorrect, String message, boolean block) {
      this(commandCorrect, message, block, 0);
   }

   public CliCommandResult(boolean commandCorrect, String message) {
      this(commandCorrect, message, false);
   }

   public void waitFor() throws InterruptedException {
      if (!block)
         return;

      synchronized (this) {
         wait(timeout);
      }
   }

   public void finished() {
      if (!block)
         return;

      synchronized (this) {
         notifyAll();
      }
   }

   public String getMessage() {
      return message;
   }

   public boolean wasCommandCorrect() {
      return commandCorrect;
   }
}
