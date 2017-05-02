package be.ehb.exceptions;

public abstract class AbstractRestException extends RuntimeException {

    private transient String serverStack;

    public AbstractRestException() {
    }

    public AbstractRestException(String message) {
        super(message);
    }

    public AbstractRestException(Throwable cause) {
        super(cause);
    }

    public AbstractRestException(String message, Throwable cause) {
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
