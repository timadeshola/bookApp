package co.zonetechpark.booktest.booktest.service.impl;

import co.zonetechpark.booktest.booktest.jpa.entity.User;
import co.zonetechpark.booktest.booktest.service.ApplicationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;
import javax.servlet.http.HttpServletRequest;
import java.security.spec.AlgorithmParameterSpec;
import java.security.spec.KeySpec;
import java.util.Date;
import java.util.Locale;

/**
 * Created by timmy on 4/1/2018.
 */
@Service
@Slf4j
public class ApplicationServiceImpl implements ApplicationService {

    private MessageSource messageSource;
    private ObjectMapper jacksonObjectMapper;

    @Autowired
    public ApplicationServiceImpl(MessageSource messageSource, ObjectMapper jacksonObjectMapper) {
        this.messageSource = messageSource;
        this.jacksonObjectMapper = jacksonObjectMapper;
    }

    private static final byte[]	SALT = { (byte) 0x21, (byte) 0x21, (byte) 0xF0, (byte) 0x55, (byte) 0xC3, (byte) 0x9F, (byte) 0x5A, (byte) 0x75						};

    private final static int	ITERATION_COUNT	= 31;

    private String DEFUALT_KEY="#$%z0N3t3chP@rK@rs@#$90@$%&%";


    public ApplicationServiceImpl(){}

    public Locale getDefaultApplicationLocale(){
        return Locale.ENGLISH;
    }

    public String getResponseMessage(String messageKey,Locale locale){
        String message ="";
        try {
            message = messageSource.getMessage(messageKey, new Object[0], (locale!=null)?locale:getDefaultApplicationLocale());
        }catch(Exception err){
            log.error("Error",err);
        }
        return message;
    }

    public String getMessage(String key, Locale locale, Object ... object){
        String message ="";
        try{
            message = messageSource.getMessage(key,object,locale);
        }catch(Exception err){
            log.error("Error",err);
        }
        return message;
    }

    public ObjectMapper getJacksonObjectMapper() {
        return jacksonObjectMapper;
    }

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

    public String getAppUrl(HttpServletRequest request) {
        return request.getScheme() + "//" + request.getServerName() + ":" + request.getServerPort() + request.getContextPath();
    }

    public String getLoggedInUserName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return ((User) authentication.getPrincipal()).getUsername();
        }
        return null;
    }

    public String getLoggedInFirstName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return ((User) authentication.getPrincipal()).getFirstName();
        }
        return null;
    }

    public String getLoggedInFullName() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return ((User) authentication.getPrincipal()).getFirstName() + " " + ((User) authentication.getPrincipal()).getLastName();
        }
        return null;
    }

    public Long getLoggedInUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return ((User) authentication.getPrincipal()).getId();
        }
        return null;
    }

    public Date getLoggedInUserTime() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (!(authentication instanceof AnonymousAuthenticationToken)) {
            return ((User) authentication.getPrincipal()).getLastLoginDate();
        }
        return null;
    }

    public static void main(String[] args) {
        String token = "1XBo_ySP9lhFkYrxndOZoA==";
        ApplicationServiceImpl applicationService = new ApplicationServiceImpl();

        System.out.println(applicationService.decryptStringData(token));
    }
}
