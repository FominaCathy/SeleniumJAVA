package TestStandSelenide;

import com.codeborne.selenide.*;

import static com.codeborne.selenide.Selenide.*;

public class MainPageSld {

    //region WebElements
    private final StudyTable studyTable = Selenide.page(StudyTable.class);

    private SelenideElement helloField = $("li.mdc-menu-surface--anchor");
    private SelenideElement createBtn = $("button#create-btn");
    private SelenideElement fieldGroupName = $("form#update-item input");
    private SelenideElement btnSave = $("form#update-item button[type='submit']");
    private SelenideElement btnClose = $(".form-modal-header button");
    private SelenideElement table = $("div.mdc-data-table");
    private ElementsCollection listRow = $$("table[aria-label='Tutors list'] tbody tr");
    private ElementsCollection listStudy = $$("table[aria-label='User list'] tbody tr");
    private SelenideElement fieldCountLogins = $("form#generate-logins input");
    private SelenideElement btnCreatingLogins = $("form#generate-logins div.submit button");
    private SelenideElement btnCloseLogins = $x("//*[@id='generateStudentsForm-title']/../button");
    //@FindBy(xpath = "//ul[@class='mdc-deprecated-list']//span[text()='Profile']" )
    private SelenideElement menuProfile = $x("//nav//li[contains(@class,'mdc-menu-surface--anchor')]//span[text()='Profile']");

    //endregion WebElements

    public void clickProfile() {
        helloField.should(Condition.visible).click();
        menuProfile.should(Condition.visible).click();
    }

    public String getGreeting() {
        return helloField.should(Condition.visible).getText();
    }

    /**
     * добавление новой группы
     *
     * @param myGroup название группы
     */
    private void addNewGroup(String myGroup) {
        createBtn.should(Condition.visible).click();
        fieldGroupName.should(Condition.visible).setValue(myGroup);
        fieldGroupName.should(Condition.value(myGroup));
        btnSave.should(Condition.visible).click();
        btnClose.should(Condition.visible).click();
    }

    /**
     * проверка отображения группы
     *
     * @param myGroup
     */
    public void successAddNewDroup(String myGroup) {
        addNewGroup(myGroup);
        table.should(Condition.visible).should(Condition.text(myGroup));
    }

    private TableRow getRow(String myGroup) {
        listRow.should(CollectionCondition.sizeGreaterThan(0));
        return listRow
                .asDynamicIterable()
                .stream()
                .map(TableRow::new)
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
     * добавление кол-ва студентов в группу
     *
     * @param myGroup    название группу
     * @param countStudy кол-во студентов
     */
    private void addStudy(String myGroup, String countStudy) {
        getRow(myGroup).getBtnStudyGroup().click();
        //
        fieldCountLogins.should(Condition.visible).setValue(countStudy);
        btnCreatingLogins.should(Condition.visible).click();
        btnCloseLogins.should(Condition.visible).click();
    }

    /**
     * проверка отображения кол-ва студентов
     *
     * @param myGroup
     * @param countStudy
     */
    public void successAddStudy(String myGroup, String countStudy) {
        addStudy(myGroup, countStudy);
        getRow(myGroup).getBtnStudyGroup().should(Condition.text(countStudy));
    }

    public StudyTable openListStudy(String myGroup) {
        getRow(myGroup).getBtnZoomIn().click();
        studyTable.checkTableVisible();
        return studyTable;

    }
}
