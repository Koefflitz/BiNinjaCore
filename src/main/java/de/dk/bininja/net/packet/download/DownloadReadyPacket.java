package de.dk.bininja.net.packet.download;

/**
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 */
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
