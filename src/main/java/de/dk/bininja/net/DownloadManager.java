package de.dk.bininja.net;

import static de.dk.bininja.net.DownloadState.INITIALIZING;
import static de.dk.bininja.net.DownloadState.RUNNING;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 */
public class DownloadManager<D extends Download> implements Iterable<D> {
   private final List<D> downloads = new LinkedList<>();

   public DownloadManager() {

   }

   public void waitFor() throws InterruptedException {
      for (D download : downloads)
         download.waitFor();
   }

   public void add(D download) {
      downloads.add(download);
      download.addListener(new DownloadListenerAdapter());
   }

   public int size() {
      return downloads.size();
   }

   @Override
   public Iterator<D> iterator() {
      return downloads.iterator();
   }

   private class DownloadListenerAdapter implements DownloadListener {

      @Override
      public void stateChanged(DownloadState state, Download download) {
         if (state != RUNNING && state != INITIALIZING)
            downloads.remove(download);
      }

      @Override
      public void loadProgress(double progress, long receivedBytes, long total, float loadSpeed) {}

      @Override
      public void writeProgress(double progress, long writtenBytes, long total) {}
   }
}
