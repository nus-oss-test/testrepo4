package teammates.test.pageobjects;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import teammates.common.util.Const;

public class FeedbackQuestionSubmitPage extends AppPage {

	public FeedbackQuestionSubmitPage(Browser browser) {
		super(browser);
	}
	
	@Override
	protected boolean containsExpectedPageContents() {
		return getPageSource().contains("<h1>Submit Feedback Question</h1>");
	}

	public void fillResponseTextBox(int responseNumber, String text) {
		WebElement element = browser.driver.findElement(
				By.name(Const.ParamsNames.FEEDBACK_RESPONSE_TEXT + "-1-" + responseNumber));
		fillTextBox(element, text);
	}
	
	public void clickSubmitButton() {
		WebElement button = browser.driver.findElement(By.id("response_submit_button"));
		button.click();
	}
}
