package exception;

public class NoImageLoadedException extends Exception{

	private static final long serialVersionUID = 7749973702875164662L;
	
	public NoImageLoadedException() {
		super("Nincs kép betöltve");
	}
	
	public NoImageLoadedException(String msg) {
		super(msg);
	}

}
