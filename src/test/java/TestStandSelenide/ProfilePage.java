package TestStandSelenide;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import java.io.File;
import java.time.Duration;
import java.util.Objects;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class ProfilePage {
    private final SelenideElement additionName = $("div.mdc-card h2");
    private final SelenideElement fullNameInfo
            = $x("//h3/following-sibling::div//div[contains(text(), 'Full name')]/following-sibling::div");
    private final SelenideElement birthdateInfo
            = $x("//h3/following-sibling::div//div[contains(text(), 'Date of birth')]/following-sibling::div");
    private final SelenideElement editButton = $("button[title='More options']");
    private final SelenideElement formEdit = $("form#update-item");
    private final SelenideElement avatarFieldEdit = formEdit.$("input[type='file']");
    private final SelenideElement birthdateFieldEdit = formEdit.$("input[type='date']");
    private final SelenideElement saveEditProfileButton = formEdit.$("button[type='submit']");
    private final SelenideElement closeFormEditButton = $x("//button[text() = 'close']");

    public String getAdditionName() {
        return additionName.should(Condition.visible).text();
    }

    public String getFullNameInfoAddition() {
        return fullNameInfo.should(Condition.visible).getText();
    }

    public String getBirthdateInfoAddition() {
        return birthdateInfo.should(Condition.visible).getText();
    }

    public void openMoreOption() {
        editButton.should(Condition.visible).click();
    }

    public void setAvatar(File file) {
        avatarFieldEdit.should(Condition.visible).uploadFile(file);
    }

    public void setBirthdate(String dateBirth) {
        birthdateFieldEdit.should(Condition.visible).setValue(dateBirth);
    }

    public String getAvatarValue() {
        String avatarValue = avatarFieldEdit.should(Condition.visible).getValue();
        return Objects.requireNonNull(avatarValue).substring(avatarValue.lastIndexOf("\\") + 1);
    }


    public void editingProfileSave() {
        saveEditProfileButton.should(Condition.visible).click();
    }

    public void setCloseFormEdit() {
        closeFormEditButton.should(Condition.visible).click();
    }

    public void successSetBirthdate(String dateString) {
       birthdateInfo.should(Condition.text(dateString), Duration.ofSeconds(15));
    }
}
