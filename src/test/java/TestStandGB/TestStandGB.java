package TestStandGB;

import jdk.jfr.Description;
import org.junit.jupiter.api.*;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.Duration;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TestStandGB {
    private static WebDriver chromeDriver;
    private static String login = "Student-3";
    private static String password = "56856478f0";
    private static String txtError = "401";

    private static String urlBase = "https://test-stand.gb.ru/login";
    private static ChromeOptions options;
    private static WebDriverWait wait;

    @BeforeAll
    public static void init() {
        System.setProperty("webdriver.chrome.driver", "src\\test\\resources\\chromedriver.exe");
        options = new ChromeOptions();
        //  options.addArguments("--headless");
    }

    @BeforeEach
    void openWin() {
        chromeDriver = new ChromeDriver(options);
        chromeDriver.manage().window().maximize();
        wait = new WebDriverWait(chromeDriver, Duration.ofSeconds(15));
    }

    @Test
    @Description("Отображение ошибки при авторизации без логина и пароля")
    void invalidAuthorizeEmptyData() {
        chromeDriver.get(urlBase);
        LoginPage loginPage = new LoginPage(chromeDriver, wait);
        loginPage.login();
        loginPage.checkButtonVisibility();
        assertTrue(loginPage.getMsgError().contains(txtError));
    }

    /**
     * валидная авторизация
     */
    private void validAuthorize() {
        chromeDriver.get(urlBase);
        LoginPage loginPage = new LoginPage(chromeDriver, wait);
        loginPage.login(login, password);
        loginPage.checkButtonInvisibility();

        MainPage mainPage = new MainPage(chromeDriver, wait);
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
        MainPage mainPage = new MainPage(chromeDriver, wait);
        mainPage.successAddNewDroup(myGroup);

        //скриншот
        File screenshot = ((TakesScreenshot) chromeDriver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(screenshot, new File("src\\test\\resources\\screenshot.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
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
    private MainPage addStudyInGroup(String myGroup, String countStudy) {

        MainPage mainPage = addGroup(myGroup);
        mainPage.successAddStudy(myGroup, countStudy);
        return mainPage;
    }

    @Test
    @Description("проверка количество студентов в списке")
    void checkListStudy() {
        //TODO invalid test
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String myGroup = "myGroup" + timestamp.getTime();
        String countGroup = "5";
        MainPage mainPage = addStudyInGroup(myGroup, countGroup);

        assertEquals(Integer.parseInt(countGroup), mainPage.getCountStudy(myGroup));
    }

    @Test
    @Description("проверка изменения статуса первого студента")
    void activeAndInactiveStudy() {
        //TODO invalid test
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String myGroup = "myGroup" + timestamp.getTime();
        String countGroup = "5";
        MainPage mainPage = addStudyInGroup(myGroup, countGroup);

        mainPage.openListStudy(myGroup);

        assertEquals("active", mainPage.getStatusFirsStudy());

        mainPage.successDeleteFirstStudy();
        assertEquals("block", mainPage.getStatusFirsStudy());

        mainPage.successRestoreFirstStudy();
        assertEquals("active", mainPage.getStatusFirsStudy());
    }

    @AfterEach
    void closeWin() {
        chromeDriver.quit();
    }
}
