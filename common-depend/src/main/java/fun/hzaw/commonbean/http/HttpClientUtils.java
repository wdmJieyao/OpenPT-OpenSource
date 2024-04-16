package fun.hzaw.commonbean.http;

import cn.hutool.core.lang.Assert;
import cn.hutool.http.HttpStatus;
import cn.hutool.log.Log;
import cn.hutool.log.LogFactory;
import com.alibaba.fastjson2.JSON;
import com.google.common.collect.Queues;
import fun.hzaw.commonbean.constants.HttpConstant;
import fun.hzaw.commonbean.constants.ThreadConstant;
import fun.hzaw.commonbean.enums.HttpHeaderEnum;
import fun.hzaw.commonbean.exception.CommonBaseErrorCode;
import fun.hzaw.commonbean.exception.CommonBaseException;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.net.CookieManager;
import java.net.URI;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.util.ArrayDeque;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public class HttpClientUtils {

    private static final Log log = LogFactory.get();

    private final static String NAME_PREFIX = "common-hzaw-http-client";

    /**
     * 默认cookie管理器
     */
    private final static CookieManager DF_COOKIE_MANAGER = new CookieManager();

    /**
     * 默认核心线程池
     */
    private final static ThreadPoolExecutor DF_THREAD_POOL_EXECUTOR = new ThreadPoolExecutor(
            // 核心线程数
            ThreadConstant.CPU_CORE_SIZE,
            // 最大线程数
            ThreadConstant.DF_MAXIMUM_POOL_SIZE,
            // 闲置存活时间
            ThreadConstant.DF_KEEP_ALIVE_TIME,
            // 闲置线程存活时间单位
            TimeUnit.SECONDS,
            // 等待队列
            new LinkedBlockingQueue<>(ThreadConstant.DF_WAIT_LIST_SIZE),
            // 线程工厂
            task -> new Thread(task, NAME_PREFIX),
            // 拒绝策略
            new ThreadPoolExecutor.CallerRunsPolicy()
    );

    /**
     * 默认HTTP客户端
     */
    private static final HttpClient DF_CLIENT = HttpClient.newBuilder()
            .version(HttpClient.Version.HTTP_2)
            .connectTimeout(Duration.ofSeconds(20))
            // 线程池
            .executor(DF_THREAD_POOL_EXECUTOR)
            // Cookie管理器
            .cookieHandler(DF_COOKIE_MANAGER)
            .build();

    /**
     * 请求头转换
     *
     * @param headerMap 请求头Map
     * @return 请求头 String[]
     */
    private static String[] convertHeaderMapToStringList(Map<String, String> headerMap) {
        // 保证数据有序
        ArrayDeque<String> headerDeque = Queues.newArrayDeque();
        Optional.ofNullable(headerMap)
                .filter(MapUtils::isNotEmpty)
                .ifPresent(headerReq -> headerReq.entrySet().stream()
                        .filter(currHeader -> StringUtils.isNoneBlank(currHeader.getKey(), currHeader.getValue()))
                        // 请求头放入队列中
                        .forEach(currHeader -> {
                            headerDeque.addLast(currHeader.getKey());
                            headerDeque.addLast(currHeader.getValue());
                        })
                );
        return headerDeque.isEmpty() ? new String[]{
                HttpHeaderEnum.USER_AGENT.getHeaderName(),
                HttpHeaderEnum.USER_AGENT.getHeaderValue(),
                HttpHeaderEnum.CONTENT_TYPE_TEXT.getHeaderName(),
                HttpHeaderEnum.CONTENT_TYPE_TEXT.getHeaderValue()
        } : headerDeque.toArray(new String[]{});
    }

    /**
     * GET 请求
     *
     * @param url    请求路径
     * @param header 请求头
     * @return true-请求成功  false-请求失败
     */
    public static boolean doGet(String url, Map<String, String> header) {
        Assert.isTrue(StringUtils.isNotBlank(url), "url can be not blank!!!");
        log.info("doGet..url:{}..header:{}", url, header);

        try {
            // 创建请求对象
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(HttpConstant.TIMEOUT))
                    .headers(convertHeaderMapToStringList(header))
                    .GET()
                    .build();

            Assert.isTrue(request.headers().map().containsKey(HttpHeaderEnum.CONTENT_TYPE_FROM.getHeaderName()), "The Content-Type type must be specified in the request header!!!");
            HttpResponse<String> response = DF_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            Integer statusCode = Optional.ofNullable(response)
                    .map(HttpResponse::statusCode)
                    .orElse(HttpStatus.HTTP_INTERNAL_ERROR);
            log.info("doGet..url:{}..statusCode:{}", url, statusCode);
            return Objects.equals(HttpStatus.HTTP_OK, statusCode);
        } catch (IOException | InterruptedException e) {
            throw new CommonBaseException(CommonBaseErrorCode.CALL_REMOTE_ERROR);
        }
    }


    /**
     * GET 请求
     *
     * @param url    请求路径
     * @param header 请求头
     * @return T-返回对象
     */
    public static <T> T doGet(String url, Map<String, String> header, Class<T> t) {
        Assert.isTrue(StringUtils.isNotBlank(url), "url can be not blank!!!");
        log.info("doGet..url:{}..header:{}", url, header);

        try {
            // 创建请求对象
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(url))
                    .timeout(Duration.ofSeconds(HttpConstant.TIMEOUT))
                    .headers(convertHeaderMapToStringList(header))
                    .GET()
                    .build();
            Assert.isTrue(request.headers().map().containsKey(HttpHeaderEnum.CONTENT_TYPE_FROM.getHeaderName()), "The Content-Type type must be specified in the request header!!!");
            HttpResponse<String> response = DF_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            // 响应码
            Integer statusCode = Optional.ofNullable(response)
                    .map(HttpResponse::statusCode)
                    .orElse(HttpStatus.HTTP_INTERNAL_ERROR);
            // 请求结果体
            String responseBody = Optional.ofNullable(response)
                    .map(HttpResponse::body)
                    .orElse("");
            log.info("doGet..url:{}..statusCode:{}..responseBody:{}", url, statusCode, responseBody);

            if (!Objects.equals(HttpStatus.HTTP_OK, statusCode)) {
                throw new CommonBaseException(statusCode, CommonBaseErrorCode.INTERFACE_ERROR.getMsg());
            }

            return StringUtils.equals(t.getName(), String.class.getName()) ? (T) responseBody : JSON.parseObject(responseBody, t);
        } catch (IOException | InterruptedException e) {
            throw new CommonBaseException(CommonBaseErrorCode.CALL_REMOTE_ERROR);
        }
    }


    /**
     * GET 请求
     *
     * @param httpReqSupplier 请求体
     * @param t               返回参数
     * @param <T>             返回参数
     * @return 响应参数
     */
    public static <T> T doGet(Supplier<HttpRequest> httpReqSupplier, Class<T> t) {
        Objects.requireNonNull(httpReqSupplier, "supplier can be not null!!!");

        try {
            HttpRequest request = httpReqSupplier.get();
            log.info("doGet..url:{}..headers:{}", request.uri(), request.headers());
            Assert.isTrue(request.headers().map().containsKey(HttpHeaderEnum.CONTENT_TYPE_FROM.getHeaderName()), "The Content-Type type must be specified in the request header!!!");

            HttpResponse<String> response = DF_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            // 响应码
            Integer statusCode = Optional.ofNullable(response)
                    .map(HttpResponse::statusCode)
                    .orElse(HttpStatus.HTTP_INTERNAL_ERROR);
            // 请求结果体
            String responseBody = Optional.ofNullable(response)
                    .map(HttpResponse::body)
                    .orElse("");
            log.info("doGet..statusCode:{}..responseBody:{}", statusCode, responseBody);

            if (!Objects.equals(HttpStatus.HTTP_OK, statusCode)) {
                throw new CommonBaseException(statusCode, CommonBaseErrorCode.INTERFACE_ERROR.getMsg());
            }

            return StringUtils.equals(t.getName(), String.class.getName()) ? (T) responseBody : JSON.parseObject(responseBody, t);
        } catch (IOException | InterruptedException e) {
            throw new CommonBaseException(CommonBaseErrorCode.CALL_REMOTE_ERROR);
        }
    }


    /**
     * POS  JSON格式请求
     *
     * @param url    请求路径
     * @param param  请求参数
     * @param header 请求头
     */
    public static <T> T doPostWithJson(String url, Object param, Map<String, String> header, Class<T> t) {
        Assert.isTrue(StringUtils.isNotBlank(url), "url can be not blank!!!");

        log.info("doPostWithJson..url:{}..param:{}..header:{}", url, param, header);
        try {
            HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                    .headers(convertHeaderMapToStringList(header))
                    .timeout(Duration.ofSeconds(HttpConstant.TIMEOUT))
                    .POST(HttpRequest.BodyPublishers.ofString(JSON.toJSONString(param)))
                    .build();

            Assert.isTrue(request.headers().map().containsKey(HttpHeaderEnum.CONTENT_TYPE_FROM.getHeaderName()), "The Content-Type type must be specified in the request header!!!");
            HttpResponse<String> response = DF_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            // 响应码
            Integer statusCode = Optional.ofNullable(response)
                    .map(HttpResponse::statusCode)
                    .orElse(HttpStatus.HTTP_INTERNAL_ERROR);
            // 请求结果体
            String responseBody = Optional.ofNullable(response)
                    .map(HttpResponse::body)
                    .orElse("");
            log.info("doPostWithJson..statusCode:{}..responseBody:{}", statusCode, responseBody);
            if (!Objects.equals(HttpStatus.HTTP_OK, statusCode)) {
                throw new CommonBaseException(statusCode, CommonBaseErrorCode.INTERFACE_ERROR.getMsg());
            }

            return StringUtils.equals(t.getName(), String.class.getName()) ? (T) responseBody : JSON.parseObject(responseBody, t);
        } catch (IOException | InterruptedException e) {
            throw new CommonBaseException(CommonBaseErrorCode.CALL_REMOTE_ERROR);
        }
    }

    /**
     * POS  FROM表单
     *
     * @param url    请求路径
     * @param param  请求参数
     * @param header 请求头
     */
    public static <T> T doPostWithFrom(String url, Map<String, String> param, Map<String, String> header, Class<T> t) {
        Assert.isTrue(StringUtils.isNotBlank(url), "url can be not blank!!!");

        log.info("doPostWithFrom..url:{}..param:{}..header:{}", url, param, header);
        try {
            String formString = param.entrySet().stream()
                    .map(curr -> String.format("%s=%s", URLEncoder.encode(curr.getKey(), StandardCharsets.UTF_8), URLEncoder.encode(curr.getValue(), StandardCharsets.UTF_8)))
                    .collect(Collectors.joining("&"));
            // 设置请求头
            header.put(HttpHeaderEnum.CONTENT_TYPE_FROM.getHeaderName(), HttpHeaderEnum.CONTENT_TYPE_FROM.getHeaderValue());
            HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                    .headers(convertHeaderMapToStringList(header))
                    .timeout(Duration.ofSeconds(HttpConstant.TIMEOUT))
                    .POST(HttpRequest.BodyPublishers.ofString(formString))
                    .build();

            HttpResponse<String> response = DF_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            // 响应码
            Integer statusCode = Optional.ofNullable(response)
                    .map(HttpResponse::statusCode)
                    .orElse(HttpStatus.HTTP_INTERNAL_ERROR);
            // 请求结果体
            String responseBody = Optional.ofNullable(response)
                    .map(HttpResponse::body)
                    .orElse("");
            log.info("doPostWithFrom..statusCode:{}..responseBody:{}", statusCode, responseBody);
            if (!Objects.equals(HttpStatus.HTTP_OK, statusCode)) {
                throw new CommonBaseException(statusCode, CommonBaseErrorCode.INTERFACE_ERROR.getMsg());
            }

            return StringUtils.equals(t.getName(), String.class.getName()) ? (T) responseBody : JSON.parseObject(responseBody, t);
        } catch (IOException | InterruptedException e) {
            throw new CommonBaseException(CommonBaseErrorCode.CALL_REMOTE_ERROR);
        }
    }

    /**
     * POS  FROM表单
     *
     * @param url    请求路径
     * @param param  请求参数
     * @param header 请求头
     */
    public static <T> T doPostWithFrom(String url, Map<String, Object> param, Map<String, String> header, CookieManager cookieManager, Class<T> t) {
        Assert.isTrue(StringUtils.isNotBlank(url), "url can be not blank!!!");
        Assert.notNull(cookieManager, "cookieManager can be not null!!!");

        log.info("doPostWithFrom..url:{}..param:{}..header:{}", url, param, header);
        try {
            String formString = param.entrySet().stream()
                    .map(curr -> String.format("%s=%s", URLEncoder.encode(curr.getKey(), StandardCharsets.UTF_8), URLEncoder.encode(curr.getValue().toString(), StandardCharsets.UTF_8)))
                    .collect(Collectors.joining("&"));
            // 设置请求头
            header.put(HttpHeaderEnum.CONTENT_TYPE_FROM.getHeaderName(), HttpHeaderEnum.CONTENT_TYPE_FROM.getHeaderValue());
            HttpRequest request = HttpRequest.newBuilder(URI.create(url))
                    .headers(convertHeaderMapToStringList(header))
                    .timeout(Duration.ofSeconds(HttpConstant.TIMEOUT))
                    .POST(HttpRequest.BodyPublishers.ofString(formString))
                    .build();

            // 创建请求客户端
            HttpClient currHttpClient = HttpClient.newBuilder()
                    .version(HttpClient.Version.HTTP_2)
                    .connectTimeout(Duration.ofSeconds(HttpConstant.CONNECT_TIMEOUT))
                    // 线程池
                    .executor(DF_THREAD_POOL_EXECUTOR)
                    // Cookie管理器
                    .cookieHandler(cookieManager)
                    .build();
            HttpResponse<String> response = currHttpClient.send(request, HttpResponse.BodyHandlers.ofString());
            // 响应码
            Integer statusCode = Optional.ofNullable(response)
                    .map(HttpResponse::statusCode)
                    .orElse(HttpStatus.HTTP_INTERNAL_ERROR);
            // 请求结果体
            String responseBody = Optional.ofNullable(response)
                    .map(HttpResponse::body)
                    .orElse("");
            log.info("doPostWithFrom..statusCode:{}..responseBody:{}", statusCode, responseBody);
            if (!Objects.equals(HttpStatus.HTTP_OK, statusCode)) {
                throw new CommonBaseException(statusCode, CommonBaseErrorCode.INTERFACE_ERROR.getMsg());
            }

            return StringUtils.equals(t.getName(), String.class.getName()) ? (T) responseBody : JSON.parseObject(responseBody, t);
        } catch (IOException | InterruptedException e) {
            throw new CommonBaseException(CommonBaseErrorCode.CALL_REMOTE_ERROR);
        }
    }

    /**
     * POS  JSON格式请求
     */
    public static <T> T doPost(Supplier<HttpRequest> requestSupplier, Class<T> t) {
        Objects.requireNonNull(requestSupplier, "requestSupplier can be not null!");

        try {
            HttpRequest request = requestSupplier.get();
            // 校验请求方式
            Assert.isTrue(StringUtils.equals(HttpConstant.POST_METHOD, request.method()), CommonBaseErrorCode.REQUEST_METHOD_ERROR.getMsg());
            // 校验请求头
            Assert.isTrue(request.headers().map().containsKey(HttpHeaderEnum.CONTENT_TYPE_FROM.getHeaderName()), "The Content-Type type must be specified in the request header!!!");
            log.info("doPost..url:{}..header:{}", request.uri(), request.headers());
            HttpResponse<String> response = DF_CLIENT.send(request, HttpResponse.BodyHandlers.ofString());
            // 响应码
            Integer statusCode = Optional.ofNullable(response)
                    .map(HttpResponse::statusCode)
                    .orElse(HttpStatus.HTTP_INTERNAL_ERROR);
            // 请求结果体
            String responseBody = Optional.ofNullable(response)
                    .map(HttpResponse::body)
                    .orElse("");
            log.info("doPost..statusCode:{}..responseBody:{}", statusCode, responseBody);
            if (!Objects.equals(HttpStatus.HTTP_OK, statusCode)) {
                throw new CommonBaseException(statusCode, CommonBaseErrorCode.INTERFACE_ERROR.getMsg());
            }

            return StringUtils.equals(t.getName(), String.class.getName()) ? (T) responseBody : JSON.parseObject(responseBody, t);
        } catch (IOException | InterruptedException e) {
            throw new CommonBaseException(CommonBaseErrorCode.CALL_REMOTE_ERROR);
        }
    }

    // public static void main(String[] args) throws Exception {
    //     HashMap<String, String> header = Maps.newHashMap();
    //     header.put(HttpHeaderEnum.USER_AGENT.getHeaderName(), HttpHeaderEnum.USER_AGENT.getHeaderValue());
    //
    //     // doGet("https://api.uutool.cn/ip/cz/183.13.121.170", header);
    //
    //     // JSONObject jsonObject = doGet("https://api.uutool.cn/ip/cz/183.13.121.170", header, JSONObject.class);
    //     // System.out.println(jsonObject);
    //
    //     // JSONObject doGet = doGet(() -> HttpRequest.newBuilder()
    //     //                 .GET()
    //     //                 .uri(URI.create("https://api.uutool.cn/ip/cz/183.13.121.170"))
    //     //                 .headers(convertHeaderMapToStringList(header))
    //     //                 .build(),
    //     //         JSONObject.class
    //     // );
    //     //
    //     // System.out.println(doGet);
    //
    //
    //     // String response = doPostWithJson("http://localhost:8080/app/info", null, header, String.class);
    //     // System.out.println(response);
    //
    //
    //     // String response = doPost(() -> HttpRequest.newBuilder(URI.create("http://localhost:8080/app/info"))
    //     //                 .headers(convertHeaderMapToStringList(header))
    //     //                 .timeout(Duration.ofSeconds(HttpConstant.TIMEOUT))
    //     //                 .POST(HttpRequest.BodyPublishers.)
    //     //                 .build(),
    //     //         String.class);
    //     // System.out.println(response);
    //     // HashMap<@Nullable String, @Nullable String> params = Maps.newHashMap();
    //     // params.put("username", "li2016131245");
    //     // params.put("password", "#$jfFj6RbqUPaC2y85");
    //     // String response = doPostWithFrom("http://home.hzaw.org:8001/api/v2/auth/login", params, Maps.newHashMap(), String.class);
    //     // System.out.println(response);
    //     // List<HttpCookie> httpCookies = DF_COOKIE_MANAGER.getCookieStore().get(URI.create("http://home.hzaw.org"));
    //     // System.out.println(httpCookies);
    //     //
    //     //
    //     // JSONArray objects = doPostWithFrom("http://home.hzaw.org:8001/api/v2/torrents/info", Maps.newHashMap(), Maps.newHashMap(), JSONArray.class);
    //     // System.out.println(objects);
    // }


}
