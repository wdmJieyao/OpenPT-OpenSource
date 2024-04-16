package fun.hzaw.trackerservice.utils;

import com.dampcake.bencode.BencodeOutputStream;
import fun.hzaw.trackerservice.exception.TrackerBencodeException;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.net.URLCodec;
import org.apache.commons.lang3.StringUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Map;

/**
 * @program: OpenPT
 * @description: 编码转换工具
 * @author: Luke
 * @create: 2024/4/12
 **/
public class EncodeConvertUtils {

    private EncodeConvertUtils() {
    }

    private static final URLCodec URL_CODEC = new URLCodec();

    /**
     * 字符转换为URL编码
     *
     * @param str
     * @return
     */
    public static String urlEncode(String str) {
        if (StringUtils.isBlank(str)) {
            throw new TrackerBencodeException("Parameter conversion exception!");
        }

        try {
            return URL_CODEC.encode(str);
        } catch (EncoderException e) {
            throw new TrackerBencodeException("Parameter conversion exception!!!!");
        }
    }

    /**
     * URL编码解码
     *
     * @param bytes
     * @return
     */
    public static byte[] urlDecode(byte[] bytes) {
        if (bytes == null || bytes.length == 0) {
            throw new TrackerBencodeException("Parameter conversion exception!!!");
        }

        try {
            return URL_CODEC.decode(bytes);
        } catch (DecoderException e) {
            throw new TrackerBencodeException("Parameter conversion exception!!");
        }
    }

    public static <K, V> String encode(Map<K, V> data) {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream()) {
            try (BencodeOutputStream bencoder = new BencodeOutputStream(out)) {
                bencoder.writeDictionary(data);
                return out.toString();
            }
        } catch (IOException e) {
            throw new TrackerBencodeException("data bencode conversion exception!!");
        }
    }

}
