package de.dk.bininja.net;

import static de.dk.bininja.net.DownloadState.CANCELLED;
import static de.dk.bininja.net.DownloadState.COMPLETE;
import static de.dk.bininja.net.DownloadState.ERROR;
import static de.dk.bininja.net.DownloadState.INITIALIZING;

import java.net.URLConnection;

import de.dk.bininja.net.DownloadListener.DownloadListenerChain;
import de.dk.bininja.net.packet.download.DownloadCancelPacket;
import de.dk.bininja.net.packet.download.DownloadDataPacket;
import de.dk.bininja.net.packet.download.DownloadHeaderPacket;
import de.dk.bininja.net.packet.download.DownloadPacket;
import de.dk.bininja.net.packet.download.DownloadReadyPacket;
import de.dk.bininja.net.packet.download.DownloadRequestPacket;
import de.dk.util.channel.ChannelListener;

public abstract class Download extends Thread implements ChannelListener<DownloadPacket> {
   private static final String URL_HEADER_FIELD_FILENAME = "Content-Disposition";

   private DownloadState state;
   protected DownloadListenerChain listeners = new DownloadListenerChain();

   private final Object mutex = new Object();

   protected long length = -1;
   private long loadedBytes;
   private long writtenBytes;

   private long startTime = -1;
   private long lastReceiveTime = -1;
   private int firstByteCount = -1;
   private float loadSpeed;

   public Download() {
      setState(INITIALIZING);
   }

   public static String getFilename(URLConnection connection) throws NoSuchFieldException {
      String filenameIndicator = "filename=";
      String rawValue = connection.getHeaderField(URL_HEADER_FIELD_FILENAME);
      if (rawValue == null || !rawValue.contains(filenameIndicator)) {
         String msg = "The url doesn't provide information about the filename."
                      + String.format("The value of the headerfield \"%s\" was \"%s\"",
                      URL_HEADER_FIELD_FILENAME,
                      rawValue);
         throw new NoSuchFieldException(msg);
      }

      int beginIndex = rawValue.indexOf(filenameIndicator) + filenameIndicator.length();
      return rawValue.substring(beginIndex).replace("\"", "");
   }

   @Override
   public void received(DownloadPacket packet) {
      switch (packet.getType()) {
      case REQUEST:
         request((DownloadRequestPacket) packet);
         break;

      case HEADER:
         header((DownloadHeaderPacket) packet);
         break;

      case DATA:
         data((DownloadDataPacket) packet);
         break;

      case COMPLETE:
         finish();
         break;

      case CANCEL:
         cancel((DownloadCancelPacket) packet);
         break;

      case READY:
         ready((DownloadReadyPacket) packet);
         break;
      }
   }

   public void waitFor() throws InterruptedException {
      synchronized (mutex) {
         if (getDownloadState() == CANCELLED || getDownloadState() == ERROR || getDownloadState() == COMPLETE)
            return;

         mutex.wait();
      }
   }

   protected abstract void request(DownloadRequestPacket packet);
   protected abstract void header(DownloadHeaderPacket packet);
   protected abstract void ready(DownloadReadyPacket packet);
   protected abstract void data(DownloadDataPacket packet);
   protected abstract void finish();
   protected abstract void cancel(DownloadCancelPacket packet);

   protected long getLength() {
      return length;
   }

   protected long getLoadedBytes() {
      return loadedBytes;
   }

   protected void received(int byteCount) {
      loadedBytes += byteCount;
      if (listeners != null) {
         listeners.loadProgress(1d * loadedBytes / length,
                                loadedBytes, length,
                                updateSpeed(byteCount));
      }
   }

   protected long getWrittenBytes() {
      return writtenBytes;
   }

   protected void written(int byteCount) {
      writtenBytes += byteCount;
      if (listeners != null) {
         listeners.writeProgress(1d * writtenBytes / length,
                                 writtenBytes,
                                 length);
      }
   }

   private float updateSpeed(int byteCount) {
      long now = System.currentTimeMillis();
      if (startTime == -1) {
         startTime = now;
         firstByteCount = byteCount;
      } else {
         loadSpeed = byteCount * 1000f / (now - lastReceiveTime);
      }
      lastReceiveTime = now;
      return loadSpeed;
   }

   public double getProgress() {
      return length == -1 ? -1 : 1d * loadedBytes / length;
   }

   public void addListener(DownloadListener listener) {
      if (listener != null)
         listeners.add(listener);
   }

   public void removeListener(DownloadListener listener) {
      listeners.remove(listener);
   }

   public float getLoadSpeed() {
      return loadSpeed;
   }

   public DownloadState getDownloadState() {
      return state;
   }

   protected void setState(DownloadState state) {
      if (state == COMPLETE) {
         long delta = System.currentTimeMillis() - startTime;
         loadSpeed = 1000f * (loadedBytes - firstByteCount) / delta;
      }

      synchronized (mutex) {
         this.state = state;
         if (state == CANCELLED || state == ERROR || state == COMPLETE)
            mutex.notify();
      }
      if (listeners != null)
         listeners.stateChanged(state, this);
   }

   @Override
   public String toString() {
      return getClass().getSimpleName() + " { state=" + state + " }";
   }
}
