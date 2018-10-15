package co.zonetechpark.booktest.booktest.core.utils;

import com.google.gson.Gson;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.UUID;

public class AppUtils {

    public static final SimpleDateFormat FORMATTER_ = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

    public static String capitalise(String data) {
        if (StringUtils.isEmpty(data)) {
            return data;
        }
        return data.toUpperCase();
    }

    public static String generateUUIDNumber() {
        String randomString = UUID.randomUUID().toString();
        String[] splitArrays =randomString.split("-");
        String secondIndex = splitArrays[2];
        String thirdIndex = splitArrays[3];
        String values = secondIndex + "-" + thirdIndex;
        return AppUtils.capitalise(values);
    }

    public static <T> String toJSON(T t) {
        return new Gson().toJson(t);
    }

}
