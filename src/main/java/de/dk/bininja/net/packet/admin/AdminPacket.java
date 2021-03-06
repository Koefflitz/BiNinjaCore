package de.dk.bininja.net.packet.admin;

import de.dk.bininja.net.packet.BiNinjaPacket;

/**
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 */
public class AdminPacket extends BiNinjaPacket {
   private static final long serialVersionUID = 7186280991000983529L;

   private final AdminPacketType type;

   public AdminPacket(AdminPacketType type) {
      this.type = type;
   }

   public AdminPacketType getType() {
      return type;
   }

   @Override
   public String toString() {
      return "AdminPacket { type=" + type + " }";
   }

   public static enum AdminPacketType {
      COUNT_CONNECTIONS,
      CONNECTION_DETAILS,
      SET_BUFFER_SIZE,
      READ_BUFFER_SIZE,
      SHUTDOWN;
   }
}
