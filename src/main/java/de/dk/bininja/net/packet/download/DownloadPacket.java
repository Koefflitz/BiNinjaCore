package de.dk.bininja.net.packet.download;

import de.dk.bininja.net.packet.BiNinjaPacket;

public abstract class DownloadPacket extends BiNinjaPacket {
   private static final long serialVersionUID = 4161060206419257857L;

   private final DownloadPacketType type;

   public DownloadPacket(DownloadPacketType type) {
      this.type = type;
   }

   public DownloadPacketType getType() {
      return type;
   }

   public static enum DownloadPacketType {
      REQUEST,
      HEADER,
      READY,
      DATA,
      COMPLETE,
      CANCEL;
   }
}