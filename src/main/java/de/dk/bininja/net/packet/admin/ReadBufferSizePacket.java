package de.dk.bininja.net.packet.admin;

/**
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 */
public class ReadBufferSizePacket extends AdminPacket {
   private static final long serialVersionUID = -6727471639111032937L;

   private int bufferSize;

   public ReadBufferSizePacket() {
      this(-1);
   }

   public ReadBufferSizePacket(int bufferSize) {
      super(AdminPacketType.READ_BUFFER_SIZE);
      this.bufferSize = bufferSize;
   }

   public int getBufferSize() {
      return bufferSize;
   }

   public void setBufferSize(int bufferSize) {
      this.bufferSize = bufferSize;
   }

   @Override
   public String toString() {
      return "ReadBufferSizePacket { bufferSize=" + (bufferSize == -1 ? "<not set>" : bufferSize) + " }";
   }
}
