package de.dk.bininja.ui.cli;

import java.io.BufferedReader;
import java.io.IOException;

public abstract class CliBase {
   protected static final long DEFAULT_READ_INTERVAL = 128;
   protected static final String HELP = "help";
   protected static final String SHORT_HELP = "h";

   protected final BufferedReader in;
   private long readInterval;

   public CliBase(BufferedReader in, long readInterval) {
      this.in = in;
      this.readInterval = readInterval;
   }

   public CliBase(BufferedReader in) {
      this(in, DEFAULT_READ_INTERVAL);
   }

   protected static boolean isHelp(String input) {
      return input.equals(HELP) || input.equals(SHORT_HELP);
   }

   public String prompt() throws IOException, InterruptedException {
      System.out.print(getPrompt());
      while (!in.ready())
         Thread.sleep(readInterval);

      String input = in.readLine();
      if (input == null)
         throw new IOException("End of stream has been reached.");

      return input.trim();
   }

   public abstract void close();
   protected abstract String getPrompt();

   public long getReadInterval() {
      return readInterval;
   }

   public void setReadInterval(long readInterval) {
      this.readInterval = readInterval;
   }

}
