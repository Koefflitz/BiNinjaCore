package de.dk.bininja.net;

import java.io.IOException;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.dk.bininja.net.packet.ConnectionAnswerPacket;
import de.dk.util.net.Connection;
import de.dk.util.net.Receiver;

public class ConnectionRequest implements Receiver {
   private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionRequest.class);

   private String host;
   private int port;

   private ConnectionAnswerPacket answer;

   private final Object mutex = new Object();

   public ConnectionRequest(String host, int port) {
      this.host = Objects.requireNonNull(host);
      this.port = port;
   }

   public Base64Connection request(ConnectionType type, long timeout) throws InterruptedException,
                                                                             IOException,
                                                                             ConnectionRefusedException {
      Base64Connection connection = new Base64Connection(host, port, this);
      connection.start();
      synchronized (mutex) {
         connection.sendRaw(type.getString());
         mutex.wait(timeout);
      }
      if (answer == null) {
         close(connection);
         throw new IOException("The connection request timed out.");
      }
      if (!answer.isAccept()) {
         close(connection);
         throw new ConnectionRefusedException(answer.getMsg());
      }

      connection.setReceiver(null);
      return connection;
   }

   @Override
   public void receive(Object msg) throws IllegalArgumentException {
      if (!(msg instanceof ConnectionAnswerPacket))
         throw new IllegalArgumentException("A connection request only expects ConnectionsAnswerPackets.");

      this.answer = (ConnectionAnswerPacket) msg;
      synchronized (mutex) {
         mutex.notify();
      }
   }

   private void close(Connection c) {
      try {
         c.close();
      } catch (IOException e) {
         LOGGER.warn("Error closing the connection", e);
      }
   }
}
