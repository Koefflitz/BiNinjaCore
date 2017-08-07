package de.dk.bininja.net.packet.admin;

/**
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 */
public class CountConnectionsResultPacket extends AdminPacket {
   private static final long serialVersionUID = -2523549043379275266L;

   private final int count;

   public CountConnectionsResultPacket(int count) {
      super(AdminPacketType.COUNT_CONNECTIONS);
      this.count = count;
   }

   public int getCount() {
      return count;
   }

   @Override
   public String toString() {
      return "CountConnectionsResultPacket { count=" + count + "}";
   }
}
