package de.dk.bininja.net.packet;

import java.io.Serializable;

public abstract class BiNinjaPacket implements Serializable {
   private static final long serialVersionUID = 3199036077552480808L;

   public BiNinjaPacket() {

   }

   @Override
   public abstract String toString();
}