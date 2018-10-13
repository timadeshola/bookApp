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

public class AppUtils {

    public static final DateTimeFormatter DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    public static final DateFormat DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd");
    public static final String DEFAULT_DATE_TIME_FORMAT = "yyyy-MM-dd HH:mm:ss.SSS";

    public static <T> String toJSON(T t) {
        return new Gson().toJson(t);
    }

    public static <T> T fromJson(String data, Type type) {
        return new Gson().fromJson(data, type);
    }

    public static <T> T fromJson(String data, Class<T> type) {
        return new Gson().fromJson(data, type);
    }

    public static LocalDateTime parseLocalDateTime(String date) {
        return LocalDateTime.parse(date, DATE_TIME_FORMATTER);
    }

    public static Date parseDate(String date) throws ParseException {
        return new SimpleDateFormat().parse(date);
    }

    public static Timestamp parseTimestamp(String data) throws ParseException {
        return new Timestamp(DATE_FORMAT.parse(data).getTime());
    }

    public static Timestamp parseTimestamp(String data, String format) throws ParseException {
        return new Timestamp(new SimpleDateFormat(format).parse(data).getTime());
    }

    public static String capitalise(String data) {
        if (StringUtils.isEmpty(data)) {
            return data;
        }
        return data.toUpperCase();
    }

    public static byte[] resizeImage(byte[] fileData, int width, int height) {
        ByteArrayInputStream in = new ByteArrayInputStream(fileData);
        try {
            BufferedImage img = ImageIO.read(in);
            if (height == 0) {
                height = (width * img.getHeight()) / img.getWidth();
            }
            if (width == 0) {
                width = (height * img.getWidth()) / img.getHeight();
            }
            Image scaledImage = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
            BufferedImage imageBuff = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            imageBuff.getGraphics().drawImage(scaledImage, 0, 0, new Color(0, 0, 0), null);

            ByteArrayOutputStream buffer = new ByteArrayOutputStream();

            ImageIO.write(imageBuff, "jpg", buffer);

            return buffer.toByteArray();
        } catch (IOException e) {
            throw new IllegalArgumentException("IOException in scale", e.getCause());
        }
    }

    private static BufferedImage resizeImage(BufferedImage img, int height, int width) {
        Image tmp = img.getScaledInstance(width, height, Image.SCALE_SMOOTH);
        BufferedImage resized = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = resized.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();
        return resized;
    }

    private static byte[] resizeImage(MultipartFile in, String type, int scale) {
        //--Create a new BufferedImage from our byte[]
        BufferedImage bm = new BufferedImage(scale, scale, BufferedImage.TYPE_INT_RGB);
        try {
            bm = ImageIO.read(in.getInputStream());
        } catch (Exception e) {
            System.err.println("Exception: " + e);
        }

        //--Scale it
        AffineTransform tx = AffineTransform.getScaleInstance(scale, scale);
        AffineTransformOp op = new AffineTransformOp(tx, 3);

        //--Dies here
        op.filter(bm, null);

        //--Convert it back to a byte[]
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        try {
            ImageIO.write(bm, type, output);
        } catch (Exception e) {
            System.err.println("Exception: " + e);
        }

        byte[] bytesOut = output.toByteArray();

        return bytesOut;
    }
}
