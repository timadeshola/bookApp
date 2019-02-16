package com.booktest.service.impl;

import com.booktest.service.ApplicationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;

@Service
@Slf4j
public class ApplicationServiceImpl implements ApplicationService {

    private static final byte[]	SALT = { (byte) 0x21, (byte) 0x21, (byte) 0xF0, (byte) 0x55, (byte) 0xC3, (byte) 0x9F, (byte) 0x5A, (byte) 0x75						};

    private final static int ITERATION_COUNT = 31;

    private String DEFUALT_KEY="#$%z0N3t3chP@rK@rs@#$90@$%&%";


    public ApplicationServiceImpl(){}

    public String encryptStringData(String data){
        return myEncryption(data,DEFUALT_KEY);
    }

    public String decryptStringData(String data){
        return myDecryption(data,DEFUALT_KEY);
    }

    protected String myEncryption(String text, String secretKey){
        String myEncryptedData ="";
        try{

            KeySpec keySpec = new PBEKeySpec(secretKey.toCharArray(), SALT, ITERATION_COUNT);
            AlgorithmParameterSpec paramSpec = new PBEParameterSpec(SALT, ITERATION_COUNT);

            SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);

            Cipher ecipher = Cipher.getInstance(key.getAlgorithm());
            ecipher.init(Cipher.ENCRYPT_MODE, key, paramSpec);

            byte[] enc = ecipher.doFinal(text.getBytes("UTF-8"));

            myEncryptedData = new String(Base64.encodeBase64(enc), "UTF-8");
            // escapes for url
            myEncryptedData =myEncryptedData.replace('+', '-').replace('/', '_').replace("%", "%25").replace("\n", "%0A");
        }catch (Exception e){ }
        return myEncryptedData;
    }

    protected String myDecryption(String textS, String secretKey){
        try {

            String input = textS.replace("%0A", "\n").replace("%25", "%").replace('_', '/').replace('-', '+');

            byte[] dec = Base64.decodeBase64(input.getBytes("UTF-8"));

            KeySpec keySpec = new PBEKeySpec(secretKey.toCharArray(), SALT, ITERATION_COUNT);
            AlgorithmParameterSpec paramSpec = new PBEParameterSpec(SALT, ITERATION_COUNT);

            SecretKey key = SecretKeyFactory.getInstance("PBEWithMD5AndDES").generateSecret(keySpec);

            Cipher dcipher = Cipher.getInstance(key.getAlgorithm());
            dcipher.init(Cipher.DECRYPT_MODE, key, paramSpec);

            byte[] decoded = dcipher.doFinal(dec);

            textS = new String(decoded, "UTF-8");

        }catch (Exception e){
            e.printStackTrace();
        }

        return textS;

    }

}
