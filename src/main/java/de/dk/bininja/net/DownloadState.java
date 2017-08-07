package de.dk.bininja.net;

/**
 * @author David Koettlitz
 * <br>Erstellt am 07.08.2017
 */
public enum DownloadState {
   INITIALIZING,
   RUNNING,
   ERROR,
   CANCELLED,
   LOADING_FINISHED,
   COMPLETE;
}
