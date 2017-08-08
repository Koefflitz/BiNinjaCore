package de.dk.bininja.ui.cli;

import java.io.IOException;

public abstract class CliCommand<C> {
   protected final String name;

   public CliCommand(String name) {
      this.name = name;
   }

   protected abstract CliCommandResult execute(String input, C controller) throws IOException, InterruptedException;
   public abstract void printUsage();

   public String getName() {
      return name;
   }
}