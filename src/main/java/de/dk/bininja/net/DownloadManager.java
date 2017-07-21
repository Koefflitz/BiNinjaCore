package de.dk.bininja.net;

import static de.dk.bininja.net.DownloadState.INITIALIZING;
import static de.dk.bininja.net.DownloadState.RUNNING;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public class DownloadManager<D extends Download> implements Iterable<D> {
   private final List<D> downloads = new LinkedList<>();

   public DownloadManager() {

   }

   public void add(D download) {
      downloads.add(download);
      download.addListener(new DownloadListenerAdapter(download));
   }

   public int size() {
      return downloads.size();
   }

   @Override
   public Iterator<D> iterator() {
      return downloads.iterator();
   }

   private class DownloadListenerAdapter implements DownloadListener {
      private Download download;

      public DownloadListenerAdapter(Download download) {
         this.download = download;
      }

      @Override
      public void stateChanged(DownloadState state) {
         if (state != RUNNING && state != INITIALIZING)
            downloads.remove(download);
      }

      @Override
      public void loadProgress(double progress, long receivedBytes, long total) {}

      @Override
      public void writeProgress(double progress, long writtenBytes, long total) {}
   }
}