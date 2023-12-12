package TestStandSelenide;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class ProfilePage {
    SelenideElement additionName = $("div.mdc-card h2");
    SelenideElement fullName
            = $x("//h3/following-sibling::div//div[contains(text(), 'Full name')]/following-sibling::div");

    public String getAdditionName() {
        return additionName.should(Condition.visible).text();
    }

    public String getFullName() {
        return fullName.should(Condition.visible).getText();
    }

}
