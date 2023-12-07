package TestStandGB;

import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.apache.commons.io.FileUtils;


import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Duration;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TestStandGB {
    static WebDriver chromeDriver;
    private static String login = "Student-3";
    private static String password = "56856478f0";
    private static String urlBase = "https://test-stand.gb.ru/login";
    private static ChromeOptions options;
    private static WebDriverWait wait;

    @BeforeAll
    public static void init() {
        System.setProperty("webdriver.chrome.driver", "src\\test\\resources\\chromedriver.exe");
        options = new ChromeOptions();
        options.addArguments("--headless");
    }

    @BeforeEach
    void openWin() {
        chromeDriver = new ChromeDriver(options);
        chromeDriver.manage().window().minimize();
        wait = new WebDriverWait(chromeDriver, Duration.ofSeconds(15));
    }


    void login() {
        chromeDriver.get(urlBase);
        WebElement usernameField = wait.until(ExpectedConditions.visibilityOfElementLocated
                (By.cssSelector("form#login input[type='text']")));
        WebElement passwordField = wait.until(ExpectedConditions.visibilityOfElementLocated
                (By.cssSelector("form#login input[type='password']")));
        WebElement button = wait.until(ExpectedConditions.visibilityOfElementLocated
                (By.cssSelector("form#login button")));
        usernameField.sendKeys(login);
        passwordField.sendKeys(password);
        button.click();
        wait.until(ExpectedConditions.invisibilityOf(button));
        String text = wait.until(ExpectedConditions.visibilityOfElementLocated(By.partialLinkText(login))).getText();
        assertEquals(("Hello, " + login), text);
    }

    @Test
    void addGrupp() {
        login();
        WebElement createBtn = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("button#create-btn")));
        createBtn.click();

        WebElement fieldGroupName = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("div.mdc-dialog__surface input")));

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String myGroup = "myGroup" + timestamp.getTime();
        fieldGroupName.sendKeys(myGroup);

        WebElement btnSave = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector("div.submit button")));
        btnSave.click();

        WebElement btnClose = wait.until(ExpectedConditions.visibilityOfElementLocated(
                By.cssSelector(".form-modal-header button")));
        btnClose.click();

        wait.until(ExpectedConditions.textToBePresentInElementLocated(
                By.cssSelector("div.mdc-data-table"), myGroup));

        //скриншот
        File screenshot = ((TakesScreenshot)chromeDriver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(screenshot, new File("src\\test\\resources\\screenshot.png"));
        } catch (IOException e){
            System.out.println(e.getMessage());
        }
    }


    @AfterEach
    void closeWin() {
        chromeDriver.quit();
    }
}
