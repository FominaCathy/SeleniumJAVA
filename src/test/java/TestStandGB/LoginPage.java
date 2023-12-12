package TestStandGB;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 * страница авторизаии
 */
public class LoginPage {
    WebDriver driver;
    private final WebDriverWait wait;
    @FindBy(css = "form#login button")
    private WebElement buttonLogin;
    @FindBy(css = "form#login input[type='text']")
    private WebElement usernameField;
    @FindBy(css = "form#login input[type='password']")
    private WebElement passwordField;
    @FindBy(css = "div.error-block")
    private WebElement errorMsg;

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        PageFactory.initElements(driver, this);
    }

    void login(String login, String password) {
        wait.until(ExpectedConditions.visibilityOf(usernameField));
        wait.until(ExpectedConditions.visibilityOf(passwordField));
        wait.until(ExpectedConditions.visibilityOf(buttonLogin));
        usernameField.sendKeys(login);
        passwordField.sendKeys(password);
        buttonLogin.click();
    }

    void login() {
        wait.until(ExpectedConditions.visibilityOf(buttonLogin));
        buttonLogin.click();
    }

    String getMsgError() {
        return wait.until(ExpectedConditions.visibilityOf(errorMsg)).getText();
    }

    void checkButtonInvisibility() {
        wait.until(ExpectedConditions.invisibilityOf(buttonLogin));
    }

    void checkButtonVisibility() {
        wait.until(ExpectedConditions.visibilityOf(buttonLogin));
    }


}
