package fun.hzaw.commonbean.enums;

import cn.hutool.core.util.IdUtil;

/**
 * 请求头枚举
 */
public enum HttpHeaderEnum {

    USER_AGENT("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/117.0.0.0 Safari/537.36 Edg/117.0.2045.36"),
    REQ_UUID("REQ-UUID", IdUtil.fastSimpleUUID()),

    CONTENT_TYPE_FROM("Content-Type", "application/x-www-form-urlencoded"),
    CONTENT_TYPE_JSON("Content-Type", "application/json"),
    CONTENT_TYPE_TEXT("Content-Type", "text/plain"),
    CONTENT_TYPE_MULTIPART("Content-Type", "multipart/form-data"),
    /**
     * 二进制文件或图片或视频
     */
    CONTENT_TYPE_OCTET_STREAM("Content-Type", "application/octet-stream"),
    CONTENT_TYPE_XML("Content-Type", "application/xml"),
    CONTENT_TYPE_JAVASCRIPT("Content-Type", "application/javascript"),
    CONTENT_TYPE_JPEG("Content-Type", "image/jpeg"),
    CONTENT_TYPE_PNG("Content-Type", "image/png"),
    CONTENT_TYPE_PDF("Content-Type", "application/pdf"),
    CONTENT_TYPE_ZIP("Content-Type", "application/zip"),

    ;

    private final String headerName;

    private final String headerValue;


    HttpHeaderEnum(String headerName, String headerValue) {
        this.headerName = headerName;
        this.headerValue = headerValue;
    }

    public String getHeaderName() {
        return headerName;
    }

    public String getHeaderValue() {
        return headerValue;
    }
}
