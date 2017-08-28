package de.dk.bininja.net;

import java.io.IOException;

import javax.crypto.SecretKey;

import de.dk.util.net.security.SessionKeyArrangement;

public interface SessionKeyBuilder {
   public SecretKey buildSessionKey(SessionKeyArrangement builder) throws IOException;
}
