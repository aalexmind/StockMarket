package stockmarketui.util;

public class Status {

	public enum StatusType {
		CANCEL, ERROR, INFO, OK, WARNING
	}

	StatusType status;
	int code;
	int severity;
	String message;
	Throwable exception;

	public Status() {
		status = StatusType.OK;
	}

	public Status(int severity, String message) {
		this();
		this.severity = severity;
		this.message = message;
		switch (severity) {
		case 1:
			status = StatusType.ERROR;
			break;
		case 2:
			status = StatusType.WARNING;
			break;
		case 3:
			status = StatusType.INFO;
			break;
		default:
			break;
		}
	}

	public Status(int severity, String message, Throwable exception) {
		this(severity, message);
		this.exception = exception;
	}

	public Status(int severity, int code, String message, Throwable exception) {
		this(severity, message, exception);
		this.code = code;
	}

	public StatusType getStatus() {
		return status;
	}

	public void setStatus(StatusType status) {
		this.status = status;
	}

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
		if (code != 0) {
			this.severity = 1;
			this.status = StatusType.ERROR;
		}
	}

	public int getSeverity() {
		return severity;
	}

	public void setSeverity(int severity) {
		if (severity != 0) {
			this.status = StatusType.ERROR;
		}
		this.severity = severity;
	}

	public String getMessage() {
		return (message == null) ? "" : message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Throwable getException() {
		return exception;
	}

	public void setException(Throwable exception) {
		this.exception = exception;
	}

	public boolean isOK() {
		return status == StatusType.OK ? true : false;
	}

}
