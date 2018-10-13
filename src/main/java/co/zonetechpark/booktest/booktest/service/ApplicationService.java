package co.zonetechpark.booktest.booktest.service;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Locale;

/**
 * Created by timmy on 4/1/2018.
 */
public interface ApplicationService {

    public Locale getDefaultApplicationLocale();

    public String getResponseMessage(String messageKey, Locale locale);

    public String getMessage(String key, Locale locale, Object... object);

    public ObjectMapper getJacksonObjectMapper();

    public String encryptStringData(String data);

    public String decryptStringData(String data);

    String getAppUrl(HttpServletRequest request);

    String getLoggedInUserName();

    String getLoggedInFirstName();

    String getLoggedInFullName();

    Long getLoggedInUserId();

    Date getLoggedInUserTime();
}
