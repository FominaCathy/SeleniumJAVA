package TestStandSelenide;


import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import jdk.jfr.Description;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TestStandGBSld {
    static WebDriver chromeDriver;
    private static String login = "Student-3";
    private static String userName = "3 Student";
    private static String password = "56856478f0";
    private static String txtError = "401";
    private static String urlBase = "https://test-stand.gb.ru/login";


    @BeforeEach
    void openWin() {
        Selenide.open(urlBase);
        chromeDriver = WebDriverRunner.getWebDriver();
    }


    @Test
    @Description("Отображение ошибки при авторизации без логина и пароля")
    void invalidAuthorizeEmptyData() {
        LoginPageSld loginPage = Selenide.page(LoginPageSld.class);//new TestStandGB.LoginPage(chromeDriver, wait);
        loginPage.login();
        loginPage.checkButtonVisibility();
        assertTrue(loginPage.getMsgError().contains(txtError));
    }

    /**
     * валидная авторизация
     */
    private void validAuthorize() {
        LoginPageSld loginPage = Selenide.page(LoginPageSld.class);
        loginPage.login(login, password);
        loginPage.checkButtonInvisibility();

        MainPageSld mainPage = Selenide.page(MainPageSld.class);

        assertEquals(("Hello, " + login), mainPage.getGreeting());
    }

    /**
     * добавление группы
     *
     * @param myGroup - название группы
     * @return объект класса MainPage
     */
    private MainPageSld addGroup(String myGroup) {
        validAuthorize();
        MainPageSld mainPage = Selenide.page(MainPageSld.class);
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
        MainPageSld mainPage = addGroup(myGroup);

        assertEquals("active", mainPage.getStatusRow(myGroup));

        mainPage.successDelete(myGroup);
        assertEquals("inactive", mainPage.getStatusRow(myGroup));

        mainPage.successRestore(myGroup);
        assertEquals("active", mainPage.getStatusRow(myGroup));
    }

    /**
     * "Добавление студентов в группу с помощью иконки ‘+’"
     */
    private MainPageSld addStudyInGroup(String myGroup, String countStudy) {

        MainPageSld mainPage = addGroup(myGroup);
        //TODO подумать как сделать правильно
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
        MainPageSld mainPage = addStudyInGroup(myGroup, countGroup);
        StudyTable studyTable = mainPage.openListStudy(myGroup);

        assertEquals(Integer.parseInt(countGroup), studyTable.getCountStudy());
    }

    @Test
    @Description("проверка изменения статуса первого студента")
    void activeAndInactiveStudy() {
        //TODO invalid test
        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String myGroup = "myGroup" + timestamp.getTime();
        String countGroup = "5";
        MainPageSld mainPage = addStudyInGroup(myGroup, countGroup);
        StudyTable studyTable = mainPage.openListStudy(myGroup);

        assertEquals("active", studyTable.getStatusFirsStudy());

        studyTable.successDeleteFirstStudy();
        assertEquals("block", studyTable.getStatusFirsStudy());

        studyTable.successRestoreFirstStudy();
        assertEquals("active", studyTable.getStatusFirsStudy());
    }

    @Test
    void checkName() {
        validAuthorize();
        MainPageSld mainPage = Selenide.page(MainPageSld.class);
        mainPage.clickProfile();
        ProfilePage profilePage = Selenide.page(ProfilePage.class);

        assertEquals(userName, profilePage.getFullName());
        assertEquals(userName, profilePage.getAdditionName());
    }

    @AfterEach
    void closeWin() {
        WebDriverRunner.closeWebDriver();
    }
}
