package de.dk.bininja.net;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.Deque;
import java.util.Iterator;
import java.util.LinkedList;

import de.dk.bininja.pack.Packer;
import de.dk.bininja.pack.UnpackException;
import de.dk.util.Serializer;
import de.dk.util.SimpleSerializer;
import de.dk.util.net.Coder;
import de.dk.util.net.Connection;
import de.dk.util.net.ReadingException;
import de.dk.util.net.Receiver;

/**
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 */
public class Base64Connection extends Connection {
   private static final String ENCODING = "UTF-8";
   private static final Charset CHARSET;

   public static final int PORT = 10000;
   public static final int DEFAULT_BUFFER_SIZE = 64000;

   public static final char MSG_DELIMITER = '.';
   public static final byte[] MSG_DELIMITER_AS_BYTEARRAY;

   private final Packer packer = new Packer();
   private final Serializer serializer = new SimpleSerializer();
   private final Deque<Coder> coders = new LinkedList<>();

   private byte[] buffer = new byte[DEFAULT_BUFFER_SIZE];
   private String stringBuffer = "";

   private long bytesSend;
   private long bytesReceived;

   static {
      try {
         CHARSET = Charset.forName(ENCODING);
      } catch (IllegalArgumentException e) {
         throw new Error("Encoding " + ENCODING + " not supported.", e);
      }
      MSG_DELIMITER_AS_BYTEARRAY = ("" + MSG_DELIMITER).getBytes(CHARSET);
   }

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
         byte[] data = serializer.serialize(msg);
         for (Coder coder : coders)
            data = coder.encode(data);

         sendRaw(packer.pack(data));
      }
   }

   public void sendRaw(byte[] data) throws IOException {
      synchronized (out) {
         bytesSend += data.length + MSG_DELIMITER_AS_BYTEARRAY.length;
         out.write(data);
         out.write(MSG_DELIMITER_AS_BYTEARRAY);
         out.flush();
      }
   }

   @Override
   public Object readObject() throws IOException, ReadingException {
      while (!socket.isClosed()) {
         int endIndex;
         if ((endIndex = stringBuffer.indexOf(MSG_DELIMITER)) != -1) {
            String data = stringBuffer.substring(0, endIndex);
            Object packet = processReceivedMsg(data);

            stringBuffer = stringBuffer.substring(endIndex + 1);

            if (packet != null)
               return packet;
         }

         byte[] buffer = this.buffer;
         int readBytes = in.read(buffer);
         if (readBytes == -1)
            break;

         bytesReceived += readBytes;
         stringBuffer += new String(buffer, 0, readBytes, CHARSET);
      }
      return null;
   }

   protected Object processReceivedMsg(String msg) throws ReadingException {
      byte[] unpacked;
      try {
         unpacked = packer.unpack(msg.getBytes(CHARSET));
      } catch (UnpackException e) {
         throw new ReadingException("Corrupt message received: " + msg, e);
      }

      Iterator<Coder> iterator = coders.descendingIterator();
      while (iterator.hasNext())
         try {
            unpacked = iterator.next()
                               .decode(unpacked);
         } catch (IOException e) {
            throw new ReadingException(e);
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

   /**
    * Add a coder to (de/en)-code the (in/out)-coming data.
    * The <code>coder</code> will be wrapped around the other added coders.
    * This leads to the following behaviour:<br>
    * While encoding, the coder will be used after the other added coders.<br>
    * While decoding, the coder will be used before the other added coders.
    *
    * @param coder The coder to be used when sending/receiving data.
    */
   public void appendCoder(Coder coder) {
      coders.addLast(coder);
   }

   /**
    * Add a coder to (de/en)-code the (in/out)-coming data.
    * The coder will be wrapped into the other added coders.
    * This leads to the following behaviour:<br>
    * While encoding, the <code>coder</code> will be used before the other added <code>coder</code>s.<br>
    * While decoding, the <code>coder</code> will be used after the other added <code>coder</code>s.
    *
    * @param coder The coder to be used when sending/receiving data.
    */
   public void presetCoder(Coder coder) {
      coders.addFirst(coder);
   }

   /**
    * Remove an added coder from being used when sending/receiving data.
    *
    * @param coder The coder to be removed.
    */
   public void removeCoder(Coder coder) {
      coders.remove(coder);
   }

   public long getBytesSent() {
      return bytesSend;
   }

   public long getBytesReceived() {
      return bytesReceived;
   }

   public ObjectInputStream getObjectOutput() {
      try {
         return new ObjectInput();
      } catch (SecurityException | IOException e) {
         throw new IllegalStateException(e);
      }
   }

   private class ObjectInput extends ObjectInputStream {

      public ObjectInput() throws IOException, SecurityException {

      }

      @Override
      protected Object readObjectOverride() throws IOException {
         return Base64Connection.this.readObject();
      }

   }
}
