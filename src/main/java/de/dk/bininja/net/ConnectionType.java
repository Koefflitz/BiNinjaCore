package de.dk.bininja.net;

/**
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 */
public enum ConnectionType {
   CLIENT("client", "download client"),
   ADMIN("admin", "admin client"),
   ALL("all", "total");

   private final String string;
   private final String description;

   private ConnectionType(String string, String description) {
      this.string = string;
      this.description = description;
   }

   public static ConnectionType parse(String string) {
      for (ConnectionType type : values()) {
         if (type.string.equals(string))
            return type;
      }
      return null;
   }

   public String getString() {
      return string;
   }

   public String getDescription() {
      return description;
   }

   @Override
   public String toString() {
      return string;
   }
}
