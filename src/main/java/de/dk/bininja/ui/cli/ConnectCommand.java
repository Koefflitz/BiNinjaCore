package de.dk.bininja.ui.cli;

import java.io.IOException;

import de.dk.bininja.net.Base64Connection;
import de.dk.bininja.net.ConnectionRefusedException;
import de.dk.opt.ArgumentModel;
import de.dk.opt.ArgumentParser;
import de.dk.opt.ArgumentParserBuilder;
import de.dk.opt.ex.ArgumentParseException;

public class ConnectCommand extends CliCommand<CliController> {
   private static final String NAME = "connect";
   private static final String ARG_HOST = "host";
   private static final String ARG_HOST_DESCRIPTION = "The host to connect to.";
   private static final char OPT_KEY_PORT = 'p';
   private static final String OPT_LONGKEY_PORT = "port";
   private static final String OPT_PORT_DESCRIPTION = "The port to connect on"
                                                      + "(default " + Base64Connection.PORT + ")";

   private static final ArgumentParser PARSER = buildParser();

   public ConnectCommand() {
      super(NAME);
   }

   private static ArgumentParser buildParser() {
      return ArgumentParserBuilder.begin()
                                  .addArgument(ARG_HOST, ARG_HOST_DESCRIPTION)
                                  .buildOption(OPT_KEY_PORT)
                                     .setLongKey(OPT_LONGKEY_PORT)
                                     .setDescription(OPT_PORT_DESCRIPTION)
                                     .setExpectsValue(true)
                                     .build()
                                  .buildAndGet();
   }

   @Override
   protected CliCommandResult execute(String input, CliController controller) throws InterruptedException {
      String[] tokens = input.split("\\s+");
      if (tokens.length < 1)
         return new CliCommandResult(false, "No arguments specified.");

      ArgumentModel parsedArgs;
      try {
         parsedArgs = PARSER.parseArguments(1, tokens);
      } catch (ArgumentParseException e) {
         return new CliCommandResult(false, e.getMessage());
      }

      String host = parsedArgs.getArgumentValue(ARG_HOST);
      String portString = parsedArgs.getOptionValue(OPT_KEY_PORT);
      int port;
      if (portString == null) {
         port = Base64Connection.PORT;
      } else {
         try {
            port = Integer.parseInt(portString);
         } catch (NumberFormatException e) {
            return new CliCommandResult(false, "Invalid port: " + portString);
         }
      }
      try {
         controller.connect(host, port);
         return new CliCommandResult(true, null);
      } catch (ConnectionRefusedException e) {
         String msg = "The server refused the conntection request." + e.getMessage() == null ? "" : ("" + e.getMessage());
         return new CliCommandResult(true, msg);
      } catch (IOException e) {
         return new CliCommandResult(true, "Could not connect to " + host + ".\n" + e.getMessage());
      }
   }

   @Override
   public void printUsage() {
      System.out.println("connect");
      System.out.println("Connect to the server");
      PARSER.printUsage(System.out);
   }

}
