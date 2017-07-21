package de.dk.bininja.net.packet.admin;

public class SetBufferSizePacket extends AdminPacket {
   private static final long serialVersionUID = -6071414974784315881L;

   private final int bufferSize;

   public SetBufferSizePacket(int bufferSize) {
      super(AdminPacketType.SET_BUFFER_SIZE);
      this.bufferSize = bufferSize;
   }

   public int getBufferSize() {
      return bufferSize;
   }

   @Override
   public String toString() {
      return "SetBufferSizePacket { bufferSize=" + bufferSize + " }";
   }

}