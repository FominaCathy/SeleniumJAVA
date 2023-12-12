package TestStandSelenide;

import com.codeborne.selenide.CollectionCondition;
import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;


import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$$;


public class StudyTable {

    SelenideElement rootList = $("div.mdc-data-table");
    ElementsCollection listStudy = $$("table[aria-label='User list'] tbody tr");

    public void checkTableVisible() {
        rootList.should(Condition.visible);
    }

    /**
     * получаем первого студента
     *
     * @return
     */
    private StudySld getStudyFirst() {
        listStudy.should(CollectionCondition.sizeGreaterThan(0));
        return listStudy
                .asDynamicIterable()
                .stream()
                .map(StudySld::new)
                .findFirst()
                .orElseThrow();
    }

    public int getCountStudy() {
        return listStudy.should(CollectionCondition.sizeGreaterThan(0)).size();
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
