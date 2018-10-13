package co.zonetechpark.booktest.booktest.core.internal;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;

import static java.time.format.DateTimeFormatter.ofPattern;

public interface ApplicationConstant {

    interface  MailSenderAddress {
        String NOREPLY = "CyberStars <jtestermails@gmail.com>";
        String SUPPORT = "CyberStars SUPPORT <jtestermails@gmail.com>";
        String CONTACT = "CyberStars CONTACT <jtestermails@gmail.com>";
    }

    interface SMSMessageType{
        String NORMAL="normal";
        String PORT="port";
    }


    interface MailMessageType {
        String HTML="html";
        String PLAIN="textMessage";
        String EMAIL_TEMPLATE_ENCODING = "UTF-8";
        String SUCCESS_RESPONSE = "Mail Sent Successfully";
    }

    public interface SecurityConstants {
        String APP_NAME = "CIT";
        String API_SECRET = "Y@WN447rub404*j@d35h0l@#";
        Long JWT_EXPIRES_IN = 3600000L; // 1 hr
        String TOKEN_PREFIX = "Bearer ";
        String HEADER_STRING = "Authorization";
    }

    public interface Validations{
        public static final String TOKEN_INVALID = "invalid_Token";
        public static final String TOKEN_EXPIRED = "expired";
        public static final String TOKEN_VALID = "valid";
        public static final String VALID = "valid";
    }

    public interface JMSQueue {
        public static final String MAIL_QUEUE = "MAIL_QUEUE";
        public static final String SMS_QUEUE = "SMS_QUEUE";
        public static final String NOTIFICATION_QUEUE = "NOTIFY_QUEUE";
        public static final String BROKER_URL = "tcp://localhost:61616";
        public static final String USER = "admin";
        public static final String PASS = "admin";
    }

    public interface InfoBip {
        public static String baseUrl = "https://api.infobip.com";
        public static String SMS_USERNAME = "cyberstarsIT";
        public static String SMS_PASS = "YAWN447rub404";
    }

    public static final DateTimeFormatter FORMATTER = ofPattern("dd::MM::yyyy");
    public SimpleDateFormat FORMATTER_ = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

}
