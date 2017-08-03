package de.dk.bininja.net;

import java.io.Serializable;

public class ConnectionDetails implements Serializable {
   private static final long serialVersionUID = -66870433382761578L;

   private String host;
   private int port;
   private ConnectionType type;

   public ConnectionDetails(String host, int port, ConnectionType type) {
      this.host = host;
      this.port = port;
      this.type = type;
   }

   public String getHost() {
      return host;
   }

   public void setHost(String host) {
      this.host = host;
   }

   public int getPort() {
      return port;
   }

   public void setPort(int port) {
      this.port = port;
   }

   public ConnectionType getType() {
      return type;
   }

   public void setType(ConnectionType type) {
      this.type = type;
   }

   @Override
   public int hashCode() {
      final int prime = 31;
      int result = 1;
      result = prime * result + ((this.host == null) ? 0 : this.host.hashCode());
      result = prime * result + this.port;
      result = prime * result + ((this.type == null) ? 0 : this.type.hashCode());
      return result;
   }

   @Override
   public boolean equals(Object obj) {
      if (this == obj)
         return true;
      if (obj == null)
         return false;
      if (getClass() != obj.getClass())
         return false;
      ConnectionDetails other = (ConnectionDetails) obj;
      if (this.host == null) {
         if (other.host != null)
            return false;
      } else if (!this.host.equals(other.host))
         return false;
      if (this.port != other.port)
         return false;
      if (this.type != other.type)
         return false;
      return true;
   }

   @Override
   public String toString() {
      return type + " connection to " + host + "@" + port;
   }
}