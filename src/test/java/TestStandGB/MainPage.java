package TestStandGB;

import TestStandSelenide.StudyTable;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

/**
 * основная страница ЛК
 */
public class MainPage {
    private WebDriver driver;
    private final WebDriverWait wait;
    private final StudyTable studyTable = new StudyTable();

    //region WebElements
    @FindBy(css = "li.mdc-menu-surface--anchor")
    private WebElement helloField;
    @FindBy(css = "button#create-btn")
    private WebElement createBtn;
    @FindBy(css = "form#update-item input")
    private WebElement fieldGroupName;
    @FindBy(css = "form#update-item button[type='submit']")
    private WebElement btnSave;
    @FindBy(css = ".form-modal-header button")
    private WebElement btnClose;
    @FindBy(css = "div.mdc-data-table")
    private WebElement table;
    @FindBy(css = "table[aria-label='Tutors list'] tbody tr")
    private List<WebElement> listRow;
    @FindBy(css = "table[aria-label='User list'] tbody tr")
    private List<WebElement> listStudy;
    @FindBy(css = "form#generate-logins input")
    private WebElement fieldCountLogins;
    @FindBy(css = "form#generate-logins div.submit button")
    private WebElement btnCreatingLogins;
    @FindBy(xpath = "//*[@id='generateStudentsForm-title']/../button")
    private WebElement btnCloseLogins;

    //endregion WebElements

    public MainPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
        PageFactory.initElements(driver, this);
    }

    public String getGreeting() {
        return wait.until(ExpectedConditions.visibilityOf(helloField)).getText();
    }

    /**
     * добавление новой группы
     *
     * @param myGroup название группы
     */
    private void addNewGroup(String myGroup) {
        wait.until(ExpectedConditions.visibilityOf(createBtn));
        createBtn.click();

        wait.until(ExpectedConditions.visibilityOf(fieldGroupName));
        fieldGroupName.sendKeys(myGroup);

        wait.until(ExpectedConditions.textToBePresentInElementValue(fieldGroupName, myGroup));
        wait.until(ExpectedConditions.visibilityOf(btnSave));
        btnSave.click();

        wait.until(ExpectedConditions.visibilityOf(btnClose));
        btnClose.click();
    }

    /**
     * проверка отображения группы
     *
     * @param myGroup - название группы
     */
    public void successAddNewDroup(String myGroup) {
        addNewGroup(myGroup);
        wait.until(ExpectedConditions.textToBePresentInElement(table, myGroup));
    }

    private TableRow getRow(String myGroup) {
        return listRow.stream()
                .map(x -> new TableRow(x))
                .filter(x -> x.getTitle().equals(myGroup))
                .findFirst()
                .orElseThrow();
    }


    public String getStatusRow(String myGroup) {
        return getRow(myGroup).getStatus();
    }

    public void successDelete(String myGroup) {
        getRow(myGroup).clickDelete("delete");
        getRow(myGroup).waitRestore("restore_from_trash");
    }

    public void successRestore(String myGroup) {
        getRow(myGroup).clickDelete("restore_from_trash");
        getRow(myGroup).waitRestore("delete");
    }

    /**
     * добавление студентов в группу
     *
     * @param myGroup    название группы
     * @param countStudy кол-во студентов
     */
    private void addStudy(String myGroup, String countStudy) {
        getRow(myGroup).getBtnStudyGroup().click();
        //
        wait.until(ExpectedConditions.visibilityOf(fieldCountLogins));
        fieldCountLogins.sendKeys(countStudy);
        wait.until(ExpectedConditions.visibilityOf(btnCreatingLogins));
        btnCreatingLogins.click();
        wait.until(ExpectedConditions.visibilityOf(btnCloseLogins));
        btnCloseLogins.click();
    }

    /**
     * проверка отображения кол-ва студентов
     *
     * @param myGroup    название группы
     * @param countStudy кол-во студентов
     */
    public void successAddStudy(String myGroup, String countStudy) {
        addStudy(myGroup, countStudy);
        wait.until(ExpectedConditions.textToBePresentInElement(getRow(myGroup).getBtnStudyGroup(), countStudy));
    }

    /**
     * получаем первого студента
     *
     * @return первого студента
     */
    private Study getStudyFirst() {
        return listStudy.stream()
                .map(x -> new Study(x))
                .findFirst()
                .orElseThrow();
    }

    public int getCountStudy(String myGroup) {
        getRow(myGroup).getBtnZoomIn().click();
        wait.until(ExpectedConditions.visibilityOfAllElements(listStudy));
        return listStudy.size();
    }

    public void openListStudy(String myGroup) {
        getRow(myGroup).getBtnZoomIn().click();
        wait.until(ExpectedConditions.visibilityOfAllElements(listStudy));
    }

    public void successDeleteFirstStudy() {
        getStudyFirst().clickDelete("delete");
        getStudyFirst().waitRestore("restore_from_trash");
    }

    public void successRestoreFirstStudy() {
        getStudyFirst().clickDelete("restore_from_trash");
        getStudyFirst().waitRestore("delete");
    }

    public String getStatusFirsStudy() {
        return getStudyFirst().getStatus();
    }
}
