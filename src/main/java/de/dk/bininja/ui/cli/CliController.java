package de.dk.bininja.ui.cli;

import java.io.IOException;
import java.net.UnknownHostException;

public interface CliController {
   public void connect(String host, int port) throws UnknownHostException, IOException;
   public void exit();
}