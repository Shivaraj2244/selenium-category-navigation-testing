package pages;

import config.TestConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

/** AutomationExercise home page. */
public class HomePage extends BasePage {

    private static final By LOGO = By.cssSelector("header#header img[alt='Website for automation practice']");

    public HomePage(WebDriver driver) {
        super(driver);
    }

    public HomePage open() {
        open(TestConfig.baseUrl() + "/", "the AutomationExercise home page");
        waitForVisible(LOGO, "the AutomationExercise logo");
        return this;
    }

    public boolean isLogoVisible() {
        return waitForVisible(LOGO, "the store logo").isDisplayed();
    }

    public CategoryPage openProducts() {
        CategoryPage products = new CategoryPage(driver);
        return products.openProducts();
    }
}
