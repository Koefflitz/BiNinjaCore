package de.dk.bininja.net;

import java.io.Serializable;

import de.dk.util.unit.memory.MemoryValue;

/**
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 */
public class ConnectionMetadata implements Serializable {
   private static final long serialVersionUID = -66870433382761578L;

   private String host;
   private int port;
   private ConnectionType type;
   private boolean secure;
   private long timeStamp;
   private long bytesSend;
   private long bytesReceived;

   public ConnectionMetadata(String host,
                             int port,
                             ConnectionType type,
                             boolean secure,
                             long timeStamp,
                             long bytesReceived,
                             long bytesSend) {
      this.host = host;
      this.port = port;
      this.type = type;
      this.secure = secure;
      this.timeStamp = timeStamp;
      this.bytesReceived = bytesReceived;
      this.bytesSend = bytesSend;
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

   public boolean isSecure() {
      return secure;
   }

   public void setSecure(boolean secure) {
      this.secure = secure;
   }

   public long getTimeStamp() {
      return timeStamp;
   }

   public void setTimeStamp(long timeStamp) {
      this.timeStamp = timeStamp;
   }

   public MemoryValue getBytesSent() {
      return new MemoryValue(bytesSend);
   }

   public void setBytesSend(long bytesSend) {
      this.bytesSend = bytesSend;
   }

   public MemoryValue getBytesReceived() {
      return new MemoryValue(bytesReceived);
   }

   public void setBytesReceived(long bytesReceived) {
      this.bytesReceived = bytesReceived;
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
      ConnectionMetadata other = (ConnectionMetadata) obj;
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
      StringBuilder builder = new StringBuilder(type.toString());
      builder.append(" connection to ")
             .append(host)
             .append(':')
             .append(port);

      if (secure)
         builder.append(" (encrypted)");

      return builder.toString();
   }
}
