package de.dk.bininja.net;

import java.io.IOException;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import de.dk.bininja.pack.Packer;
import de.dk.bininja.pack.UnpackException;
import de.dk.util.Serializer;
import de.dk.util.net.Connection;
import de.dk.util.net.ReadingException;
import de.dk.util.net.Receiver;

/**
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 */
public class Base64Connection extends Connection {
   public static final char MSG_DELIMITER = '.';
   public static final byte[] MSG_DELIMITER_AS_BYTEARRAY = ("" + MSG_DELIMITER).getBytes();
   public static final int PORT = 10000;
   public static final int DEFAULT_BUFFER_SIZE = 64000;

   private final Packer packer = new Packer();
   private final Serializer serializer = new Serializer();
   private byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
   private String stringBuffer = "";

   public Base64Connection(Socket socket, Receiver receiver) throws IOException {
      super(socket, receiver);
   }

   public Base64Connection(String host, int port, Receiver receiver) throws UnknownHostException, IOException {
      super(host, port, receiver);
   }

   public Base64Connection(Socket socket) throws IOException {
      super(socket);
   }

   public Base64Connection(String host, int port) throws UnknownHostException, IOException {
      super(host, port);
   }

   @Override
   public void send(Serializable msg) throws IOException {
      synchronized (out) {
         sendRaw(packer.pack(serializer.serialize(msg)));
      }
   }

   public void sendRaw(String msg) throws IOException {
      synchronized (out) {
         out.write(msg.getBytes());
         out.write(MSG_DELIMITER_AS_BYTEARRAY);
         out.flush();
      }
   }

   @Override
   protected Object[] readObject() throws IOException, ReadingException {
      while (!socket.isClosed()) {
         byte[] buffer = this.buffer;
         int readBytes = in.read(buffer);
         if (readBytes == -1)
            break;

         stringBuffer += new String(buffer, 0, readBytes);
         int endIndex;
         List<Object> result = new ArrayList<>();
         while ((endIndex = stringBuffer.indexOf(MSG_DELIMITER)) != -1) {
            String data = stringBuffer.substring(0, endIndex);
            Object packet = processReceivedMsg(data);

            stringBuffer = stringBuffer.substring(endIndex + 1);

            if (packet != null)
               result.add(packet);
         }
         if (!result.isEmpty())
            return result.toArray(new Object[result.size()]);
      }
      return null;
   }

   protected Object processReceivedMsg(String msg) throws ReadingException {
      byte[] unpacked;
      try {
         unpacked = packer.unpack(msg);
      } catch (UnpackException e) {
         throw new ReadingException("Corrupt message received: " + msg, e);
      }
      Object object;
      try {
         object = serializer.deserialize(unpacked);
      } catch (ClassNotFoundException | IOException e) {
         throw new ReadingException("Corrupt message received: " + unpacked, e);
      }

      return object;
   }

   public void setBufferSize(int length) throws IllegalArgumentException {
      if (length <= 0)
         throw new IllegalArgumentException("Buffer size may not be less than 1");

      if (length != buffer.length)
         this.buffer = new byte[length];
   }
}
