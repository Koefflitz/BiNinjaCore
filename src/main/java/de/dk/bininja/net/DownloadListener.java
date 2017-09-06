package de.dk.bininja.net;

import java.util.ArrayList;

/**
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 */
public interface DownloadListener {
   public void stateChanged(DownloadState state, Download download);
   public void loadProgress(double progress, long receivedBytes, long total, float speed);
   public void writeProgress(double progress, long writtenBytes, long total);

   public static class DownloadListenerChain extends ArrayList<DownloadListener>
                                             implements DownloadListener {
      private static final long serialVersionUID = -6961295371791275802L;

      @Override
      public void stateChanged(DownloadState state, Download download) {
         Object[] listeners = toArray();
         for (Object l : listeners)
            ((DownloadListener) l).stateChanged(state, download);
      }

      @Override
      public void loadProgress(double progress, long receivedBytes, long total, float speed) {
         Object[] listeners = toArray();
         for (Object l : listeners)
            ((DownloadListener) l).loadProgress(progress, receivedBytes, total, speed);
      }

      @Override
      public void writeProgress(double progress, long writtenBytes, long total) {
         Object[] listeners = toArray();
         for (Object l : listeners)
            ((DownloadListener) l).writeProgress(progress, writtenBytes, total);
      }
   }
}
