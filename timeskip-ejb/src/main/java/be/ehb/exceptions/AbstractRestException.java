package be.ehb.exceptions;

public abstract class AbstractRestException extends RuntimeException {

    private transient String serverStack;

    AbstractRestException() {
    }

    AbstractRestException(String message) {
        super(message);
    }

    AbstractRestException(Throwable cause) {
        super(cause);
    }

    AbstractRestException(String message, Throwable cause) {
        super(message, cause);
    }

    public String getServerStack() {
        return serverStack;
    }

    public void setServerStack(String stacktrace) {
        this.serverStack = stacktrace;
    }

    public abstract int getHttpCode();

    public abstract int getErrorCode();

}
