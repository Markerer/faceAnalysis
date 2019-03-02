package exception;

public class RequestFailedException extends Exception {

	private static final long serialVersionUID = -1707108893952767997L;
	
	public RequestFailedException() {
		super("Request nem sikerült");
	}
	
	public RequestFailedException(String msg) {
		super(msg);
	}

}
