package de.dk.bininja.net.packet.admin;

import java.util.Collection;

import de.dk.bininja.net.ConnectionDetails;
import de.dk.bininja.net.ConnectionType;

/**
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 */
public class ConnectionDetailsPacket extends AdminPacket {
   private static final long serialVersionUID = -3698121898801236246L;

   private final ConnectionType connectionType;
   private Collection<ConnectionDetails> connectionDetails;

   public ConnectionDetailsPacket(ConnectionType connectionType, Collection<ConnectionDetails> connectionDetails) {
      super(AdminPacketType.CONNECTION_DETAILS);
      this.connectionType = connectionType;
      this.connectionDetails = connectionDetails;
   }

   public ConnectionDetailsPacket(ConnectionType connectionType) {
      this(connectionType, null);
   }

   public ConnectionType getConnectionType() {
      return connectionType;
   }

   public Collection<ConnectionDetails> getConnectionDetails() {
      return connectionDetails;
   }

   public void setConnectionDetails(Collection<ConnectionDetails> connectionDetails) {
      this.connectionDetails = connectionDetails;
   }

   @Override
   public String toString() {
      return "ConnectionDetailsPacket { connectionType=" + connectionType
                                        + ", connectionDetails=" + connectionDetails + " }";
   }
}
