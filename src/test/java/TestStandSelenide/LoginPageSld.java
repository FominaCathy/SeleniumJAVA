package TestStandSelenide;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class LoginPageSld {

    private final SelenideElement loginButton = $("form#login button");
    private final SelenideElement usernameField = $("form#login input[type='text']");
    private final SelenideElement passwordField = $("form#login input[type='password']");
    private final SelenideElement errorMsg = $("div.error-block");

    void login(String login, String password) {
        usernameField.should(Condition.visible).setValue(login);
        passwordField.should(Condition.visible).setValue(password);
        loginButton.should(Condition.visible).click();
    }

    void login() {
        loginButton.should(Condition.visible).click();
    }

    String getMsgError() {
        return errorMsg.should(Condition.visible).getText();
    }

    void checkButtonInvisibility() {
        loginButton.should(Condition.hidden);
    }

    void checkButtonVisibility() {
        loginButton.should(Condition.visible);
    }



}
