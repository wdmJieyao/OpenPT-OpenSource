package fun.hzaw.trackerservice.exception;

/**
 * @program: OpenPT
 * @description: trackerBencode异常
 * @author: Luke
 * @create: 2024/4/12
 **/
public class TrackerBencodeException extends RuntimeException{

    public TrackerBencodeException() {
        super();
    }

    public TrackerBencodeException(String message) {
        super(message);
    }

    public TrackerBencodeException(String message, Throwable cause) {
        super(message, cause);
    }

    public TrackerBencodeException(Throwable cause) {
        super(cause);
    }

    protected TrackerBencodeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
