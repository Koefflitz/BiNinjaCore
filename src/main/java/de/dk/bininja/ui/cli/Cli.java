package de.dk.bininja.ui.cli;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Collection;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.dk.bininja.net.Base64Connection;
import de.dk.bininja.net.ConnectionRefusedException;
import de.dk.util.StringUtils;

/**
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 */
public class Cli<C extends CliController> {
   private static final Logger LOGGER = LoggerFactory.getLogger(Cli.class);

   private static final long DEFAULT_READ_INTERVAL = 128;
   private static final String HELP = "help";
   private static final String SHORT_HELP = "h";

   protected final BufferedReader in;
   protected final C controller;
   protected final Collection<CliCommand<? super C>> commands;
   private long readInterval = DEFAULT_READ_INTERVAL;
   private Thread runningThread;

   private String promptConnected;
   private String promptNotConnected;

   private boolean connected;
   private boolean running;

   public Cli(C controller,
              Collection<CliCommand<? super C>> commands,
              BufferedReader in,
              String promptConnected,
              String promptNotConnected) {

      this.controller = controller;
      this.commands = Objects.requireNonNull(commands);
      this.promptConnected = promptConnected;
      this.promptNotConnected = promptNotConnected;
      this.in = in;
   }

   public Cli(C controller,
              Collection<CliCommand<? super C>> commands,
              String promptConnected,
              String promptNotConnected) {

      this(controller,
           commands,
           new BufferedReader(new InputStreamReader(System.in)),
           promptConnected,
           promptNotConnected);
   }

   public Cli(C controller, Collection<CliCommand<? super C>> commands) {
      this(controller, commands, "", "");
   }

   private static boolean isHelp(String input) {
      return input.equals(HELP) || input.equals(SHORT_HELP);
   }

   public CliCommand<? super C> parse(String input) {
      for (CliCommand<? super C> cmd : commands) {
         if (input.startsWith(cmd.getName()))
            return cmd;
      }
      return null;
   }

   public void start() {
      running = true;
      this.runningThread = Thread.currentThread();

      if (!connected) {
         if (!connect())
            controller.exit();
      }

      while (running) {
         String input;
         try {
            input = prompt();
         } catch (IOException | InterruptedException e) {
            if (running) {
               System.err.println("Input was closed unexpectedly... ");
               e.printStackTrace(System.err);
               running = false;
            }
            break;
         }

         enter(input);
      }
      System.out.println("BiNinja admintool out.");
   }

   public void enter(String input) {
      if (isHelp(input)) {
         printHelp();
         return;
      }
      CliCommand<? super C> cmd = parse(input);
      if (cmd == null) {
         System.out.println("Command " + input + " not found.");
         System.out.println("Type h or help to get some help.");
         return;
      }
      CliCommandResult result;
      try {
         result = cmd.execute(input, controller);
      } catch (IOException e) {
         System.err.println("Error executing command " + input);
         e.printStackTrace(System.err);
         return;
      } catch (InterruptedException e) {
         return;
      }

      if (result.getMessage() != null) {
         System.out.println(result.getMessage());
         if (!result.worked())
            cmd.printUsage();
      }
   }

   private boolean connect() {
      System.out.println("Not connected. Starting connect procedure.");
      System.out.println("Please enter the host to connect to");
      String host;
      try {
         host = caughtPrompt();
      } catch (IOException | InterruptedException e) {
         return false;
      }

      System.out.println("Now enter a port (or just enter for default port " + Base64Connection.PORT + "): ");
      String portString;
      try {
         portString = caughtPrompt();
      } catch (IOException | InterruptedException e) {
         return false;
      }
      int port;
      if (StringUtils.isBlank(portString)) {
         port = Base64Connection.PORT;
      } else {
         try {
            port = Integer.parseInt(portString);
         } catch (NumberFormatException e) {
            System.out.println(portString + " is not a valid port.");
            return false;
         }
      }
      try {
         controller.connect(host, port);
      } catch (IOException | ConnectionRefusedException e) {
         System.out.println("Could not connect to " + host + ":" + port);
         System.out.println(e.getMessage());
         return false;
      }
      return true;
   }

   public String prompt() throws IOException, InterruptedException {
      System.out.print(connected ? promptConnected : promptNotConnected);
      while (!in.ready())
         Thread.sleep(readInterval);

      String input = in.readLine();
      if (input == null)
         throw new IOException("End of stream has been reached.");

      return input.trim();
   }

   public String caughtPrompt() throws IOException, InterruptedException {
      try {
         return prompt();
      } catch (IOException | InterruptedException e) {
         if (running) {
            System.err.println("Input was closed unexpectedly... ");
            e.printStackTrace(System.err);
            running = false;
         }
         throw e;
      }
   }

   private void printHelp() {
      System.out.println("This tool expects one of the following commands:");
      for (CliCommand<? super C> cmd : commands) {
         System.out.println();
         cmd.printUsage();
      }
   }

   public void show(String format, Object... args) {
      System.out.printf(format + "\n", args);
   }

   public void showError(String errorMsg, Object... args) {
      System.err.printf(errorMsg + "\n", args);
   }

   public void close() {
      LOGGER.debug("Closing the cli.");
      running = false;
      if (runningThread != null && runningThread != Thread.currentThread())
         runningThread.interrupt();
   }

   public long getReadInterval() {
      return readInterval;
   }

   public void setReadInterval(long readInterval) {
      this.readInterval = readInterval;
   }

   public String getPromptConnected() {
      return promptConnected;
   }

   public void setPromptConnected(String promptConnected) {
      this.promptConnected = promptConnected == null ? "" : promptConnected;
   }

   public String getPromptNotConnected() {
      return promptNotConnected;
   }

   public void setPromptNotConnected(String promptNotConnected) {
      this.promptNotConnected = promptNotConnected == null ? "" : promptNotConnected;
   }

   public void setConnected(boolean connected) {
      this.connected = connected;
   }

   public boolean isRunning() {
      return running;
   }
}
