package TestStandSelenide;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;


public class StudySld {
    private final SelenideElement studyRow;

    public StudySld(SelenideElement studyRow) {
        this.studyRow = studyRow;
    }

    public String getStatus() {
        return studyRow.$x("./td[4]").should(Condition.visible).text();

    }

    public void clickDelete(String text) {
        String buttonText = String.format("./td/button[text()='%s']", text);
        studyRow.$x(buttonText).should(Condition.visible).click();
    }

    public void waitRestore(String text) {
        String buttonText = String.format("./td/button[text()='%s']", text);
        studyRow.$x(buttonText).should(Condition.visible, Duration.ofSeconds(10));
    }
}
