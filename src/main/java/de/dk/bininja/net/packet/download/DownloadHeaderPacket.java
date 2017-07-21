package de.dk.bininja.net.packet.download;

import static de.dk.bininja.net.packet.download.DownloadPacket.DownloadPacketType.HEADER;

public class DownloadHeaderPacket extends DownloadPacket {
   private static final long serialVersionUID = -3678424741415605265L;

   private final long length;
   private final String filename;

   public DownloadHeaderPacket(long length, String filename) {
      super(HEADER);
      this.length = length;
      this.filename = filename;
   }

   public long getLength() {
      return length;
   }

   public String getFilename() {
      return filename;
   }

   @Override
   public String toString() {
      return String.format("DownloadHeaderPacket { length=%s }", length);
   }
}