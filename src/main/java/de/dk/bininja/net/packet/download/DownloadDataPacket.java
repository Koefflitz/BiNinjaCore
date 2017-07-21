package de.dk.bininja.net.packet.download;

import static de.dk.bininja.net.packet.download.DownloadPacket.DownloadPacketType.DATA;

public class DownloadDataPacket extends DownloadPacket {
   private static final long serialVersionUID = -3440834789681561367L;

   private final byte[] payload;

   public DownloadDataPacket(byte[] payload) {
      super(DATA);
      this.payload = payload;
   }

   public byte[] getPayload() {
      return payload;
   }

   @Override
   public String toString() {
      return String.format("DownloadDataPacket { payload=%s }", payload);
   }

}