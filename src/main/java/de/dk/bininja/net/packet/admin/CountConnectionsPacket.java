package de.dk.bininja.net.packet.admin;

import de.dk.bininja.net.ConnectionType;

public class CountConnectionsPacket extends AdminPacket {
   private static final long serialVersionUID = -3040126379611584743L;

   private final ConnectionType connectionType;

   public CountConnectionsPacket(ConnectionType connectionType) {
      super(AdminPacketType.COUNT_CONNECTIONS);
      this.connectionType = connectionType;
   }

   public ConnectionType getConnectionType() {
      return connectionType;
   }

   @Override
   public String toString() {
      return "CountConnectionsPacket { connectionType=" + connectionType + " }";
   }
}