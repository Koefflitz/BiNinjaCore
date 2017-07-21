package de.dk.bininja.net.packet.admin;

public class BooleanAnswerPacket extends AdminPacket {
   private static final long serialVersionUID = 1447039253552730600L;

   private final boolean result;
   private final String msg;

   public BooleanAnswerPacket(AdminPacketType type, boolean result, String msg) {
      super(type);
      this.result = result;
      this.msg = msg;
   }

   public BooleanAnswerPacket(AdminPacketType type, boolean result) {
      this(type, result, null);
   }

   public boolean getResult() {
      return result;
   }

   public String getMsg() {
      return msg;
   }

   @Override
   public String toString() {
      return "BooleanAnswerPacket {result=" + result + " }";
   }
}