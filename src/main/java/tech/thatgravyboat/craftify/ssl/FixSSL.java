/*
 * Copyright (c) Forge Development LLC and contributors
 * SPDX-License-Identifier: LGPL-2.1-only
 *
 *
 * Edited by: ThatGravyBoat
 * Edited on: 2023-01-31
 * Edited Comment: Switched LambdaExceptionUtils to a copied one and changed logger and added field to store the SSLContext instead of setting the default.
 */

package tech.thatgravyboat.craftify.ssl;

import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManagerFactory;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.Certificate;
import java.security.cert.CertificateException;
import java.util.Collections;
import java.util.Map;
import java.util.stream.Collectors;

import static tech.thatgravyboat.craftify.ssl.LambdaExceptionUtils.rethrowBiConsumer;
import static tech.thatgravyboat.craftify.ssl.LambdaExceptionUtils.rethrowFunction;


/**
 * This class fixes older Java SSL setups which don't contain the correct root certificates to trust Let's Encrypt
 * https endpoints.
 * <p>
 * It uses a secondary JKS keystore: lekeystore.jks, which contains the two root certificate keys as documented here:
 * <a href="https://letsencrypt.org/certificates/">https://letsencrypt.org/certificates/</a>
 * <p>
 * To create the keystore, the following commands were run:
 * <pre>
 *     keytool -import -alias letsencryptisrgx1 -file isrgrootx1.pem -keystore lekeystore.jks -storetype jks -storepass supersecretpassword -v
 *     keytool -import -alias identrustx3 -file identrustx3.pem -keystore lekeystore.jks -storetype jks -storepass supersecretpassword -v
 * </pre>
 *
 * The PEM files were obtained from the above URL.
 */
public class FixSSL {

    public static SSLContext context = null;

    public static void fixup() {
        try {
            final KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            Path ksPath = Paths.get(System.getProperty("java.home"),"lib", "security", "cacerts");
            keyStore.load(Files.newInputStream(ksPath), "changeit".toCharArray());
            final Map<String, Certificate> jdkTrustStore = Collections.list(keyStore.aliases()).stream().collect(Collectors.toMap(a -> a, rethrowFunction(keyStore::getCertificate)));

            final KeyStore leKS = KeyStore.getInstance(KeyStore.getDefaultType());
            final InputStream leKSFile = FixSSL.class.getResourceAsStream("/lekeystore.jks");
            leKS.load(leKSFile, "supersecretpassword".toCharArray());
            final Map<String, Certificate> leTrustStore = Collections.list(leKS.aliases()).stream().collect(Collectors.toMap(a -> a, rethrowFunction(leKS::getCertificate)));

            final KeyStore mergedTrustStore = KeyStore.getInstance(KeyStore.getDefaultType());
            mergedTrustStore.load(null, new char[0]);
            jdkTrustStore.forEach(rethrowBiConsumer(mergedTrustStore::setCertificateEntry));
            leTrustStore.forEach(rethrowBiConsumer(mergedTrustStore::setCertificateEntry));

            final TrustManagerFactory instance = TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());
            instance.init(mergedTrustStore);
            final SSLContext tls = SSLContext.getInstance("TLS");
            tls.init(null, instance.getTrustManagers(), null);
            context = tls;
            System.out.println("[Craftify]: Added Lets Encrypt root certificates as additional trust");
        } catch (KeyStoreException | IOException | NoSuchAlgorithmException | CertificateException | KeyManagementException e) {
            System.out.println("[Craftify]: Failed to load lets encrypt certificate. Expect problems");
            e.printStackTrace();
        }

    }
}