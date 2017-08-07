package de.dk.bininja.ui.cli;

import java.io.IOException;

import de.dk.bininja.net.ConnectionRefusedException;

/**
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 */
public interface CliController {
   public void connect(String host, int port) throws IOException, ConnectionRefusedException;
   public void exit();
}
