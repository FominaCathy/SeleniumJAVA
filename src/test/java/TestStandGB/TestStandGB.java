package TestStandGB;

import jdk.jfr.Description;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.time.Duration;
import java.util.HashMap;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@Disabled
public class TestStandGB {
    private static WebDriver driver;
    private static String login = "Student-3";
    private static String password = "56856478f0";
    private static String txtError = "401";

    private static String urlBase = "https://test-stand.gb.ru/login";
    private static ChromeOptions options;

    private static WebDriverWait wait;

    @BeforeAll
    public static void init()  {
        HashMap<String, Object> capability = new HashMap<>();
        capability.put("enableVNC", true);
        capability.put("enableLog", true);
        options = new ChromeOptions();
        options.setCapability("selenoid:options", capability);
    }

    @BeforeEach
    void openWin() throws MalformedURLException {
        driver = new RemoteWebDriver(new URL("http://localhost:4444/wd/hub"), options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(15));
    }

    @Test
    @Description("Отображение ошибки при авторизации без логина и пароля")
    void invalidAuthorizeEmptyData() {
        driver.get(urlBase);
        LoginPage loginPage = new LoginPage(driver, wait);
        loginPage.login();
        loginPage.checkButtonVisibility();
        assertTrue(loginPage.getMsgError().contains(txtError));
    }

    @Test
    void checkValidAuthorize(){
        validAuthorize();

    }

    /**
     * валидная авторизация
     */
    private void validAuthorize() {
        driver.get(urlBase);
        LoginPage loginPage = new LoginPage(driver, wait);
        loginPage.login(login, password);
        loginPage.checkButtonInvisibility();

        MainPage mainPage = new MainPage(driver, wait);
        assertEquals(("Hello, " + login), mainPage.getGreeting());
    }

    /**
     * добавление группы
     *
     * @param myGroup - название группы
     * @return объект класса MainPage
     */
    private MainPage addGroup(String myGroup) {
        validAuthorize();
        MainPage mainPage = new MainPage(driver, wait);
        mainPage.successAddNewDroup(myGroup);

        return mainPage;
    }

    @Test
    @Description("Изменение статуса группы")
    void activeAndInactiveGroup() {
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String myGroup = "myGroup" + timestamp.getTime();
        MainPage mainPage = addGroup(myGroup);

        assertEquals("active", mainPage.getStatusRow(myGroup));

        mainPage.successDelete(myGroup);
        assertEquals("inactive", mainPage.getStatusRow(myGroup));

        mainPage.successRestore(myGroup);
        assertEquals("active", mainPage.getStatusRow(myGroup));
    }

    /**
     * "Добавление студентов в группу с помощью иконки ‘+’"
     */
    private MainPage addStudyInGroup(String myGroup, int countStudy) {

        MainPage mainPage = addGroup(myGroup);
        mainPage.successAddStudy(myGroup, countStudy);
        return mainPage;
    }

    @Test
    @Description("проверка количество студентов в списке")
    void checkListStudy() {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String myGroup = "myGroup" + timestamp.getTime();
        int countGroup = 5;
        MainPage mainPage = addStudyInGroup(myGroup, countGroup);

        assertEquals(countGroup, mainPage.getCountStudy(myGroup));
//        screenshot();
    }

    @Test
    @Description("проверка изменения статуса первого студента")
    void activeAndInactiveStudy() {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String myGroup = "myGroup" + timestamp.getTime();
        int countGroup = 5;
        MainPage mainPage = addStudyInGroup(myGroup, countGroup);

        mainPage.openListStudy(myGroup);

        assertEquals("active", mainPage.getStatusFirsStudy());

        mainPage.successDeleteFirstStudy();
        assertEquals("block", mainPage.getStatusFirsStudy());

        mainPage.successRestoreFirstStudy();
        assertEquals("active", mainPage.getStatusFirsStudy());
    }

    private void screenshot() {
        //скриншот
        File screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(screenshot, new File("src\\test\\resources\\screenshot.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @AfterEach
    void closeWin() {
        driver.quit();
    }
}
