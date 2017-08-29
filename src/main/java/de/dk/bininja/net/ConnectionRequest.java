package de.dk.bininja.net;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Objects;

import javax.crypto.SecretKey;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.dk.bininja.net.packet.ConnectionAnswerPacket;
import de.dk.bininja.net.packet.ConnectionRequestPacket;
import de.dk.util.net.Connection;
import de.dk.util.net.Receiver;
import de.dk.util.net.security.CipherCoderAdapter;
import de.dk.util.net.security.SessionKeyArrangement;

public class ConnectionRequest implements Receiver {
   private static final Logger LOGGER = LoggerFactory.getLogger(ConnectionRequest.class);

   private String host;
   private int port;

   private SessionKeyBuilder sessionKeyBuilder;

   private ConnectionAnswerPacket answer;

   private final Object mutex = new Object();

   public ConnectionRequest(String host, int port, SessionKeyBuilder sessionKeyBuilder) {
      this.host = Objects.requireNonNull(host);
      this.port = port;
      this.sessionKeyBuilder = sessionKeyBuilder;
   }

   public ConnectionRequest(String host, int port) {
      this(host, port, null);
   }

   public Base64Connection request(ConnectionType type, long timeout) throws InterruptedException,
                                                                             IOException,
                                                                             ConnectionRefusedException {
      Base64Connection connection = new Base64Connection(host, port);

      if (isSecure()) {
         ConnectionRequestPacket packet = new ConnectionRequestPacket(true);
         LOGGER.debug("Sending " + packet + " to request a secure connection");
         connection.send(packet);
         LOGGER.debug("Arranging the session key.");
         SessionKeyArrangement builder = new SessionKeyArrangement(connection, connection.getObjectOutput());
         SecretKey sessionKey = sessionKeyBuilder.buildSessionKey(builder);
         if (sessionKey != null) {
            try {
               connection.appendCoder(new CipherCoderAdapter(sessionKey));
            } catch (NullPointerException | GeneralSecurityException e) {
               LOGGER.error("The could not create coder with the sessionKey: " + sessionKey);
               close(connection);
               throw new IOException("Could not create coder.", e);
            }
            LOGGER.debug("Session key arranged.");
         }
      }

      connection.addReceiver(this);
      connection.start();

      LOGGER.debug("Telling the server, that I am the admin tool.");
      synchronized (mutex) {
         try {
            connection.send(new ConnectionRequestPacket(type));
         } catch (IOException e) {
            LOGGER.error("Could not tell the server who I am.", e);
            close(connection);
            throw e;
         }
         LOGGER.debug("Waiting for an answer from the server...");
         mutex.wait(timeout);
      }

      connection.removeReceiver(this);

      if (answer == null) {
         close(connection);
         throw new IOException("The connection request timed out.");
      }

      if (!answer.isAccept()) {
         close(connection);
         throw new ConnectionRefusedException(answer.getMsg());
      }

      return connection;
   }

   @Override
   public void receive(Object msg) throws IllegalArgumentException {
      try {
         this.answer = (ConnectionAnswerPacket) msg;
      } catch (ClassCastException e) {
         String errorMsg = "Expecting a ConnectionAnswerPacket but received something else: " + msg;
         throw new IllegalArgumentException(errorMsg, e);
      }

      LOGGER.debug("Received an answer from the server: " + answer);

      synchronized (mutex) {
         mutex.notify();
      }
   }

   public boolean isSecure() {
      return sessionKeyBuilder != null;
   }

   public SessionKeyBuilder getCrypterBuilder() {
      return sessionKeyBuilder;
   }

   public void setCrypterBuilder(SessionKeyBuilder crypterBuilder) {
      this.sessionKeyBuilder = crypterBuilder;
   }

   private void close(Connection c) {
      LOGGER.debug("Closing the connection to " + c.getInetAddress());
      c.removeReceiver(this);
      try {
         c.close();
      } catch (IOException e) {
         LOGGER.warn("Error closing the connection", e);
      }
   }
}
