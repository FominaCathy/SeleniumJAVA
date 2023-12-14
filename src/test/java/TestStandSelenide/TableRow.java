package TestStandSelenide;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import java.time.Duration;


public class TableRow {
    SelenideElement row;

    public TableRow(SelenideElement row) {
        this.row = row;
    }

    public String getTitle() {
        return row.$x("./td[2]").should(Condition.visible).text();
    }

    public String getStatus() {
        return row.$x("./td[3]").should(Condition.visible).text();
    }

    public void clickDelete(String text) {
        String buttonText = String.format("./td/button[text()='%s']", text);
        row.$x(buttonText).should(Condition.visible).click();
    }

    public void waitRestore(String text) {
        String buttonText = String.format("./td/button[text()='%s']", text);
        row.$x(buttonText).should(Condition.text(text), Duration.ofSeconds(10));
    }


    public SelenideElement getBtnStudyGroup() {
        return row.$x("./td[4]/button/span[@class='mdc-button__label']").should(Condition.visible);
    }


    public SelenideElement getBtnZoomIn() {
        return row.$x("./td[4]/button[text()='zoom_in']").should(Condition.visible);
    }


}
