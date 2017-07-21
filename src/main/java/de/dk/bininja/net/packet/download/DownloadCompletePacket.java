package de.dk.bininja.net.packet.download;

import static de.dk.bininja.net.packet.download.DownloadPacket.DownloadPacketType.COMPLETE;

public class DownloadCompletePacket extends DownloadPacket {
   private static final long serialVersionUID = 2920560124687758910L;

   public DownloadCompletePacket() {
      super(COMPLETE);
   }

   @Override
   public String toString() {
      return "DownloadCompletePacket";
   }

}