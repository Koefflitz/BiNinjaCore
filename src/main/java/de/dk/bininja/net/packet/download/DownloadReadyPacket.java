package de.dk.bininja.net.packet.download;

public class DownloadReadyPacket extends DownloadPacket {
   private static final long serialVersionUID = 4172182177984443212L;

   public DownloadReadyPacket() {
      super(DownloadPacketType.READY);
   }

   @Override
   public String toString() {
      return "DownloadReadyPacket";
   }
}