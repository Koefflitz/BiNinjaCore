package de.dk.bininja.net.packet.download;

import static de.dk.bininja.net.packet.download.DownloadPacket.DownloadPacketType.CANCEL;

/**
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 */
public class DownloadCancelPacket extends DownloadPacket {
   private static final long serialVersionUID = 9164845659354179311L;
   private String msg;

   public DownloadCancelPacket(String msg) {
      super(CANCEL);
      this.msg = msg;
   }

   public DownloadCancelPacket() {
      this(null);
   }

   public String getMsg() {
      return msg;
   }

   @Override
   public String toString() {
      return String.format("DownloadCancelPacket { msg=%s }", msg);
   }

}
