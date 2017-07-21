package de.dk.bininja.net.packet.download;

import static de.dk.bininja.net.packet.download.DownloadPacket.DownloadPacketType.REQUEST;

import java.net.URL;

public class DownloadRequestPacket extends DownloadPacket {
   private static final long serialVersionUID = 5145983506127775440L;

   private final URL url;

   public DownloadRequestPacket(URL url) {
      super(REQUEST);
      this.url = url;
   }

   public URL getUrl() {
      return url;
   }

   @Override
   public String toString() {
      return String.format("DownloadRequestPacket { url=%s }", url);
   }

}