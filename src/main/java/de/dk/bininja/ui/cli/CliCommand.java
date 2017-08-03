package de.dk.bininja.ui.cli;

import java.io.IOException;

public abstract class CliCommand<C> {
   protected final String name;
   private final String regex;

   public CliCommand(String name, String regex) {
      this.name = name;
      this.regex = regex;
   }

   public boolean matches(String input) {
      return input.matches(regex);
   }

   public String getName() {
      return name;
   }

   public CliCommandResult execute(String input, C controller) throws IOException, InterruptedException {
      if (!matches(input))
         return new CliCommandResult(false, "Wrong Syntax of command " + name);

      return checkedExecute(input, controller);
   }

   protected abstract CliCommandResult checkedExecute(String input, C controller) throws IOException, InterruptedException;
   public abstract void printUsage();
}