package de.dk.bininja.opt;

import java.io.Closeable;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.GeneralSecurityException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.KeySpec;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Objects;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import de.dk.util.net.security.SessionKeyArrangement;
import de.dk.util.opt.ArgumentModel;
import de.dk.util.opt.ArgumentParserBuilder;
import de.dk.util.opt.ExpectedOption;
import de.dk.util.opt.OptionBuilder;

public class ParsedSecurityArguments {
   private static final Logger LOGGER = LoggerFactory.getLogger(ParsedSecurityArguments.class);

   public static final String NAME = "secure";
   private static final String DESCRIPTION = "Allow secure connections with encrypted messages.";

   private PublicKey publicKey;
   private PrivateKey privateKey;

   public ParsedSecurityArguments() {

   }

   public static ArgumentParserBuilder build(ArgumentParserBuilder builder) {
      ArgumentParserBuilder argBuilder = builder.buildCommand(NAME)
                                                .setDescription(DESCRIPTION)
                                                .buildParser();
      for (Option opt : Option.values())
         opt.build(argBuilder);

      return argBuilder.build()
                       .build();
   }

   public static ParsedSecurityArguments parse(ArgumentModel model) throws IOException {
      if (model == null)
         return null;

      ParsedSecurityArguments result = new ParsedSecurityArguments();

      PublicKey publicKey = loadKey(model, PublicKey.class);
      PrivateKey privateKey = loadKey(model, PrivateKey.class);

      result.setPublicKey(publicKey);
      result.setPrivateKey(privateKey);
      return result;
   }

   private static <K extends Key> K loadKey(ArgumentModel model, Class<K> keyType) throws IOException {
      InputStream in = null;
      Option option;

      if (keyType.equals(PublicKey.class)) {
         option = Option.PUBLIC_KEY;
      } else if (keyType.equals(PrivateKey.class)) {
         option = Option.PRIVATE_KEY;
      } else {
         throw new IllegalArgumentException("Unsupported keytype: " + keyType
                                            + " - Only PublicKey and PrivateKey supported.");
      }

      String keyFileName = model.getOptionValue(option.longKey);
      try {
         in = new FileInputStream(keyFileName);
         return (K) loadKey(in, keyType);
      } catch (IOException | GeneralSecurityException e) {
         String msg = String.format("Could not read %s key from \"%s\"",
                                    keyType.equals(PublicKey.class) ? "public" : "private",
                                    keyFileName);
         throw new IOException(msg, e);
      } finally {
         close(in, "An error occured while closing Inputstream on file " + keyFileName);
      }
   }

   @SuppressWarnings("unchecked")
   private static <K extends Key> K loadKey(InputStream in, Class<K> keyType) throws IOException,
                                                                                     GeneralSecurityException {
      byte[] keyBytes = new byte[in.available()];
      in.read(keyBytes);
      KeyFactory keyFactory = KeyFactory.getInstance(SessionKeyArrangement.DEFAULT_ASYMMETRIC_ALGORITHM);
      if (keyType.isAssignableFrom(PublicKey.class)) {
         KeySpec spec = new X509EncodedKeySpec(keyBytes);
         return (K) keyFactory.generatePublic(spec);
      } else if (keyType.isAssignableFrom(PrivateKey.class)) {
         KeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
         return (K) keyFactory.generatePrivate(spec);
      }

      throw new IllegalArgumentException("Unsupported keytype: " + keyType
                                         + " - Only PublicKey and PrivateKey supported.");
   }

   private static void close(Closeable closable, String errorMsg) {
      if (closable == null)
         return;

      try {
         closable.close();
      } catch (IOException e) {
         LOGGER.warn(errorMsg, e);
      }
   }

   public PublicKey getPublicKey() {
      return publicKey;
   }

   public void setPublicKey(PublicKey publicKey) {
      this.publicKey = publicKey;
   }

   public PrivateKey getPrivateKey() {
      return privateKey;
   }

   public void setPrivateKey(PrivateKey privateKey) {
      this.privateKey = privateKey;
   }

   public KeyPair getKeys() {
      return new KeyPair(publicKey, privateKey);
   }

   private static enum Option {
      PUBLIC_KEY("public", "public key", "The public key", true),
      PRIVATE_KEY("private", "private key", "The private key", true);

      private final char key;
      private final String longKey;
      private final String name;
      private final String description;

      private final boolean expectsValue;
      private final boolean mandatory;

      private Option(char key, String longKey, String name, String description, boolean expectsValue, boolean mandatory) {
         this.key = key;
         this.longKey = longKey;
         this.name = Objects.requireNonNull(name);
         this.description = Objects.requireNonNull(description);
         this.expectsValue = expectsValue;
         this.mandatory = mandatory;
      }

      private Option(char key, String name, String description) {
         this(key, null, name, description, false, false);
      }

      private Option(char key, String name, String description, boolean expectsValue) {
         this(key, null, name, description, expectsValue, false);
      }

      private Option(char key, String name, String description, boolean expectsValue, boolean mandatory) {
         this(key, null, name, description, expectsValue, mandatory);
      }

      private Option(String longKey, String name, String description) {
         this(ExpectedOption.NO_KEY, longKey, name, description, false, false);
      }

      private Option(String longKey, String name, String description, boolean expectsValue) {
         this(ExpectedOption.NO_KEY, longKey, name, description, expectsValue, false);
      }

      private Option(String longKey, String name, String description, boolean expectsValue, boolean mandatory) {
         this(ExpectedOption.NO_KEY, longKey, name, description, expectsValue, mandatory);
      }

      public ArgumentParserBuilder build(ArgumentParserBuilder builder) {
         OptionBuilder oBuilder;
         if (key != ExpectedOption.NO_KEY) {
            oBuilder = builder.buildOption(key, name)
                              .setLongKey(longKey);
         } else {
            oBuilder = builder.buildOption(longKey, name);
         }

         return oBuilder.setDescription(description)
                        .setMandatory(mandatory)
                        .setExpectsValue(expectsValue)
                        .build();
      }
   }

}
