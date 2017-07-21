package de.dk.bininja.pack;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Base64;
import java.util.Base64.Decoder;
import java.util.Base64.Encoder;
import java.util.Objects;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

public class Packer {
   private Deflater deflater;
   private Inflater inflater;
   private Encoder encoder;
   private Decoder decoder;

   public Packer() {
      this(new Deflater(), new Inflater(), Base64.getEncoder(), Base64.getDecoder());
   }

   public Packer(Deflater deflater, Inflater inflater, Encoder encoder, Decoder decoder) {
      this.deflater = Objects.requireNonNull(deflater);
      this.inflater = Objects.requireNonNull(inflater);
      this.encoder = Objects.requireNonNull(encoder);
      this.decoder = Objects.requireNonNull(decoder);
   }

   public String pack(byte[] content) throws IOException {
      deflater.setInput(content);
      deflater.finish();
      ByteArrayOutputStream outStream = new ByteArrayOutputStream();
      byte[] buffer = new byte[Math.max(content.length / 16, 16)];
      while (!deflater.finished()) {
         int deflatedBytes = deflater.deflate(buffer);
         outStream.write(buffer, 0, deflatedBytes);
      }
      deflater.reset();
      return encoder.encodeToString(outStream.toByteArray());
   }

   public byte[] unpack(String content) throws UnpackException {
      byte[] bytes;
      try {
         bytes = decoder.decode(content.getBytes());
      } catch (IllegalArgumentException e) {
         throw new UnpackException(e);
      }

      inflater.setInput(bytes);
      ByteArrayOutputStream outStream = new ByteArrayOutputStream();
      byte[] buffer = new byte[bytes.length];
      while (!inflater.finished()) {
         int inflatedBytes;
         try {
            inflatedBytes = inflater.inflate(buffer);
         } catch (DataFormatException e) {
            throw new UnpackException(e);
         }
         outStream.write(buffer, 0, inflatedBytes);
      }
      inflater.reset();
      return outStream.toByteArray();
   }

}