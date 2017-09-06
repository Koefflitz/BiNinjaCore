package de.dk.bininja.net.packet.admin;

import java.util.Collection;

import de.dk.bininja.net.ConnectionMetadata;
import de.dk.bininja.net.ConnectionType;

/**
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 */
public class ConnectionMetaPacket extends AdminPacket {
   private static final long serialVersionUID = -3698121898801236246L;

   private final ConnectionType connectionType;
   private Collection<ConnectionMetadata> connectionDetails;

   public ConnectionMetaPacket(ConnectionType connectionType, Collection<ConnectionMetadata> connectionDetails) {
      super(AdminPacketType.CONNECTION_DETAILS);
      this.connectionType = connectionType;
      this.connectionDetails = connectionDetails;
   }

   public ConnectionMetaPacket(ConnectionType connectionType) {
      this(connectionType, null);
   }

   public ConnectionType getConnectionType() {
      return connectionType;
   }

   public Collection<ConnectionMetadata> getConnectionDetails() {
      return connectionDetails;
   }

   public void setConnectionMeta(Collection<ConnectionMetadata> connectionDetails) {
      this.connectionDetails = connectionDetails;
   }

   @Override
   public String toString() {
      return "ConnectionMetaPacket { connectionType=" + connectionType
                                        + ", connectionDetails=" + connectionDetails + " }";
   }
}
