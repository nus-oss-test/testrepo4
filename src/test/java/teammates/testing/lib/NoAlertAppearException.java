package teammates.testing.lib;

@SuppressWarnings("serial")
public class NoAlertAppearException extends RuntimeException {
	public NoAlertAppearException(String clickedObj){
		super("No alert message appear when clicking "+clickedObj);
	}
}