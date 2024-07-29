package com.company.module.utils;

import com.company.module.common.EKey;
import com.fasterxml.jackson.core.type.TypeReference;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.*;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.util.JSONObjectUtils;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.crypto.*;
import java.io.*;
import java.math.BigInteger;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.*;
import java.util.*;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUtils {
    private static final Map<EKey, RSAPublicKey> publicKeys = new HashMap<>();
    private static final Map<EKey, RSAPrivateKey> privateKeys = new HashMap<>();
    private static final String ALGORITHM_RSA = "RSA";
    private static final String ALGORITHM_AES = "AES";
    private static final JWSAlgorithm JWS_ALGORITHM = JWSAlgorithm.RS256;
    private static final JWEAlgorithm ALG = JWEAlgorithm.RSA_OAEP_256;
    private static final EncryptionMethod ENC = EncryptionMethod.A128CBC_HS256;

    public static void loadPublicKey(EKey ekey, String filePath) throws Exception {
        X509EncodedKeySpec spec = new X509EncodedKeySpec(loadFile(filePath));
        KeyFactory kf = KeyFactory.getInstance("RSA");
        publicKeys.put(ekey, (RSAPublicKey) kf.generatePublic(spec));
    }

    public static void loadPrivateKey(EKey ekey, String filePath) throws Exception {
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(loadFile(filePath));
        KeyFactory kf = KeyFactory.getInstance("RSA");
        privateKeys.put(ekey, (RSAPrivateKey) kf.generatePrivate(spec));
    }

    private static byte[] loadFile(String filePath) throws Exception {
        File file = new File(filePath);
        FileInputStream fis = new FileInputStream(file);
        try (fis; DataInputStream dis = new DataInputStream(fis)) {
            byte[] keyBytes = new byte[(int) file.length()];
            dis.readFully(keyBytes);
            return keyBytes;
        } catch (Exception e) {
            log.error("Load file error: {}", e.getMessage());
            throw e;
        }
    }

    public static String decrypt(EKey ekey, String jwe) throws Exception {
        // decrypt data in jwe
        JWEObject jweObject = JWEObject.parse(jwe);
        jweObject.decrypt(new RSADecrypter(privateKeys.get(ekey)));
        return jweObject.getPayload().toString();
    }

    public static String encrypt(EKey ekey, String data) throws Exception {
        // encrypt data format jwe
        JWEObject jweObject = new JWEObject(new JWEHeader(ALG, ENC), new Payload(data));
        jweObject.encrypt(new RSAEncrypter(publicKeys.get(ekey), generateSK()));
        return jweObject.serialize();
    }

    public static String sign(EKey ekey, String data) throws Exception {
        // Convert to JWK format
        RSAKey jwk = new RSAKey.Builder(generateRASKey(privateKeys.get(ekey))).privateKey(privateKeys.get(ekey)).build();
        // Create RSA-signer with the private keyprivateKeyIW
        JWSSigner signer = new RSASSASigner(jwk);
        JWSHeader jwsHeader = new JWSHeader(JWS_ALGORITHM);
        JWSObject jwsObject = new JWSObject(jwsHeader, new Payload(data));
        jwsObject.sign(signer);
        return jwsObject.serialize();
    }

    public static boolean verify(EKey ekey, String data, String jws) throws Exception {
        JWSVerifier verifier = new RSASSAVerifier(publicKeys.get(ekey));

        JWSObject jwsObject = JWSObject.parse(jws);
        if (jwsObject.verify(verifier)) {
            if (Objects.nonNull(jwsObject.getPayload().toJSONObject())) {
                return Objects.equals(jwsObject.getPayload().toJSONObject(), JSONObjectUtils.parse(data));
            }
            return Objects.equals(jwsObject.getPayload().toString(), data);
        }
        return false;
    }

    private static RSAPublicKey generateRASKey(RSAPrivateKey rsaPrivateKey) throws NoSuchAlgorithmException, InvalidKeySpecException {
        KeyFactory kf = KeyFactory.getInstance(ALGORITHM_RSA);
        RSAPrivateKeySpec rsaPrivateKeySpec = kf.getKeySpec(rsaPrivateKey, RSAPrivateKeySpec.class);

        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(rsaPrivateKeySpec.getModulus(), BigInteger.valueOf(65537));
        return (RSAPublicKey) kf.generatePublic(keySpec);
    }

    private static SecretKey generateSK() throws NoSuchAlgorithmException {
        KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM_AES);
        keyGenerator.init(SecurityUtils.ENC.cekBitLength());
        return keyGenerator.generateKey();
    }

    public static Map<String, Object> extractSecurity(String token) {
        try {
            var chunks = token.split("\\.");
            var payload = new String(Base64.getUrlDecoder().decode(chunks[1]));
            return MapperUtils.readValue(payload, new TypeReference<>() {});
        } catch (Exception e) {
            log.info("Cannot decode subject from token {}", token);
            return null;
        }
    }
}
