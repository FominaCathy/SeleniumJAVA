package TestStandSelenide;


import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;

import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


public class TestStandGBSld {
    static WebDriver chromeDriver;
    private static String login = "Student-3";
    private static String userName = "3 Student";
    private static String password = "56856478f0";
    private static String txtError = "401\nInvalid credentials.";
    private static String urlBase = "https://test-stand.gb.ru/login";
    private final int countGroup = 5;

    @BeforeEach
    void openWin() {
        Selenide.open(urlBase);
        chromeDriver = WebDriverRunner.getWebDriver();
    }


    @Test
    @DisplayName("Отображение ошибки при авторизации без логина и пароля")
    @Severity(SeverityLevel.CRITICAL)
    void invalidAuthorizeEmptyData() {
        LoginPageSld loginPage = Selenide.page(LoginPageSld.class);//new TestStandGB.LoginPage(chromeDriver, wait);
        loginPage.login();
        loginPage.checkButtonVisibility();
        assertTrue(loginPage.getMsgError().contains(txtError));
    }

    @Test
    @DisplayName("проверка валидной авторизации")
    @Severity(SeverityLevel.BLOCKER)
    void checkValidAuthorize(){
        validAuthorize();
    }


    @Test
    @DisplayName("Изменение статуса группы")
    @Severity(SeverityLevel.NORMAL)
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


    @Test
    @DisplayName("проверка количество студентов в списке")
    @Severity(SeverityLevel.NORMAL)
    void checkListStudy() {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String myGroup = "myGroup" + timestamp.getTime();

        MainPageSld mainPage = addStudyInGroup(myGroup, countGroup);
        StudyTable studyTable = mainPage.openListStudy(myGroup);

        assertEquals(countGroup, studyTable.getCountStudy());
    }

    @Test
    @DisplayName("проверка изменения статуса первого студента")
    @Severity(SeverityLevel.NORMAL)
    void activeAndInactiveStudy() {

        Timestamp timestamp = new Timestamp(System.currentTimeMillis());
        String myGroup = "myGroup" + timestamp.getTime();

        MainPageSld mainPage = addStudyInGroup(myGroup, countGroup);
        StudyTable studyTable = mainPage.openListStudy(myGroup);

        assertEquals("active", studyTable.getStatusFirsStudy());

        studyTable.successDeleteFirstStudy();
        assertEquals("block", studyTable.getStatusFirsStudy());

        studyTable.successRestoreFirstStudy();
        assertEquals("active", studyTable.getStatusFirsStudy());
    }

    @Test
    @DisplayName("проверка отображения имени")
    @Severity(SeverityLevel.NORMAL)
    void checkName() {
        validAuthorize();
        MainPageSld mainPage = Selenide.page(MainPageSld.class);
        mainPage.clickProfile();
        ProfilePage profilePage = Selenide.page(ProfilePage.class);

        assertEquals(userName, profilePage.getFullNameInfoAddition());
        assertEquals(userName, profilePage.getAdditionName());
    }


    @Test
    @DisplayName("проверка добавления файла в поле 'new avatar'")
    @Severity(SeverityLevel.MINOR)
    void checkAddAvatar() {
        validAuthorize();
        MainPageSld mainPage = Selenide.page(MainPageSld.class);
        mainPage.clickProfile();
        ProfilePage profilePage = Selenide.page(ProfilePage.class);

        profilePage.openMoreOption();

        assertEquals("", profilePage.getAvatarValue());
        profilePage.setAvatar(new File("src/test/resources/avatar.png"));
        assertEquals("avatar.png", profilePage.getAvatarValue());
    }

    @Test
    @DisplayName("Установка даты рождения")
    @Severity(SeverityLevel.MINOR)
    void checkAddBirthdate() {
        validAuthorize();
        MainPageSld mainPage = Selenide.page(MainPageSld.class);
        mainPage.clickProfile();
        ProfilePage profilePage = Selenide.page(ProfilePage.class);
        Random rnd = new Random();
        LocalDate dateBirth =
                LocalDate.of(rnd.nextInt(1900, 2020), rnd.nextInt(1, 13), rnd.nextInt(1, 29));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        String dateString = dateBirth.format(formatter);
        profilePage.openMoreOption();
        profilePage.setBirthdate(dateString);
        profilePage.editingProfileSave();
        profilePage.setCloseFormEdit();

        profilePage.successSetBirthdate(dateString);
        screenshot();

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

        return mainPage;
    }

    /**
     * "Добавление студентов в группу с помощью иконки ‘+’"
     */
    private MainPageSld addStudyInGroup(String myGroup, int countStudy) {

        MainPageSld mainPage = addGroup(myGroup);
        mainPage.successAddStudy(myGroup, countStudy);
        return mainPage;
    }

    private void screenshot() {
        //скриншот
        File screenshot = ((TakesScreenshot) chromeDriver).getScreenshotAs(OutputType.FILE);
        try {
            FileUtils.copyFile(screenshot, new File("src\\test\\resources\\screenshot.png"));
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
    }

    @AfterEach
    void closeWin() {
        WebDriverRunner.closeWebDriver();
    }
}
