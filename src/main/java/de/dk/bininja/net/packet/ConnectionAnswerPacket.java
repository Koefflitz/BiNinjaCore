package de.dk.bininja.net.packet;

public class ConnectionAnswerPacket extends BiNinjaPacket {
   private static final long serialVersionUID = -114923760514744677L;

   private boolean accept;
   private String msg;

   public ConnectionAnswerPacket(boolean accept, String msg) {
      this.accept = accept;
      this.msg = msg;
   }

   public ConnectionAnswerPacket(boolean accept) {
      this(accept, null);
   }

   public boolean isAccept() {
      return this.accept;
   }

   public void setAccept(boolean accept) {
      this.accept = accept;
   }

   public String getMsg() {
      return this.msg;
   }

   public void setMsg(String msg) {
      this.msg = msg;
   }

   @Override
   public String toString() {
      return "ConnectionAnswerPacket { accept=" + accept + ", msg=" + msg + " }";
   }
}
