package de.dk.bininja.net;

import java.util.ArrayList;

public interface DownloadListener {
   public void stateChanged(DownloadState state);
   public void loadProgress(double progress, long receivedBytes, long total);
   public void writeProgress(double progress, long writtenBytes, long total);

   public static class DownloadListenerChain extends ArrayList<DownloadListener> implements DownloadListener {
      private static final long serialVersionUID = -6961295371791275802L;

      @Override
      public void stateChanged(DownloadState state) {
         Object[] listeners = toArray();
         for (Object l : listeners)
            ((DownloadListener) l).stateChanged(state);
      }

      @Override
      public void loadProgress(double progress, long receivedBytes, long total) {
         Object[] listeners = toArray();
         for (Object l : listeners)
            ((DownloadListener) l).loadProgress(progress, receivedBytes, total);
      }

      @Override
      public void writeProgress(double progress, long writtenBytes, long total) {
         Object[] listeners = toArray();
         for (Object l : listeners)
            ((DownloadListener) l).writeProgress(progress, writtenBytes, total);
      }
   }
}