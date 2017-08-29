package de.dk.bininja.net.packet;

import java.util.Objects;

import de.dk.bininja.net.ConnectionType;

public class ConnectionRequestPacket extends BiNinjaPacket {
   private static final long serialVersionUID = 2451268204296101160L;

   private final boolean secure;
   private final ConnectionType connectionType;

   public ConnectionRequestPacket(boolean secure) {
      this.secure = secure;
      this.connectionType = null;
   }

   public ConnectionRequestPacket(ConnectionType connectionType) {
      this.secure = false;
      this.connectionType = Objects.requireNonNull(connectionType);
   }

   public boolean isSecure() {
      return secure;
   }

   public ConnectionType getConnectionType() {
      return connectionType;
   }

   @Override
   public String toString() {
      return "ConnectionRequestPacket { "
             + "connectionType=" + (connectionType == null ? "<not set>" : connectionType.getString()) + ", "
             + "secure=" + secure
             + " }";
   }

}
