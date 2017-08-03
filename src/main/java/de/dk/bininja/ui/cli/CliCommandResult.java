package de.dk.bininja.ui.cli;

import java.util.Objects;

public class CliCommandResult {
   private final boolean worked;
   private final String message;

   public CliCommandResult(boolean worked, String message) {
      this.worked = worked;
      this.message = Objects.requireNonNull(message);
   }

   public String getMessage() {
      return message;
   }

   public boolean worked() {
      return worked;
   }
}