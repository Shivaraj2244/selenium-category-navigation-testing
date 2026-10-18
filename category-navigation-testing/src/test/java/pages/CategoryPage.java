package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import config.TestConfig;
import java.util.List;
import java.util.Locale;

/** Products/category pages and their category menu and breadcrumbs. */
public class CategoryPage extends BasePage {

    private static final By PAGE_HEADING = By.cssSelector("div.features_items h2.title");
    private static final By CATEGORY_MENU = By.id("accordian");
    private static final By BREADCRUMB = By.cssSelector("div.breadcrumbs ol.breadcrumb");
    private static final By BREADCRUMB_ITEMS = By.cssSelector("div.breadcrumbs ol.breadcrumb > li");

    public CategoryPage(WebDriver driver) {
        super(driver);
    }

    public String getHeading() {
        return waitForVisible(PAGE_HEADING, "the category page heading")
            .getAttribute("textContent").replace('\u00A0', ' ')
            .replaceAll("\\s+", " ").trim();
    }

    public CategoryPage openProducts() {
        open(TestConfig.baseUrl() + "/products", "the products page");
        waitForVisible(PAGE_HEADING, "the products page heading");
        return this;
    }

    public boolean isCategoryMenuVisible() {
        return waitForVisible(CATEGORY_MENU, "the category menu").isDisplayed();
    }

    private static By categoryToggle(String name) {
        return By.id("category-" + name.toLowerCase(Locale.ROOT));
    }

    // ----- sub-categories -----

    private static By subCategoryLink(String path) {
        return By.cssSelector("#accordian a[href='" + path + "']");
    }

    public boolean isCategoryVisible(String name) {
        return waitForVisible(categoryToggle(name), "category '" + name + "'").isDisplayed();
    }

    public CategoryPage openCategory(String name, String path) {
        click(categoryToggle(name), "category '" + name + "'");
        clickPresent(subCategoryLink(path), "subcategory link '" + path + "'");
        return new CategoryPage(driver);
    }

    public boolean isSubCategoryVisible(String path) {
        return waitForVisible(subCategoryLink(path), "sub-category tile '" + path + "'").isDisplayed();
    }

    public CategoryPage openSubCategory(String path) {
        click(subCategoryLink(path), "sub-category tile '" + path + "'");
        return new CategoryPage(driver);
    }

    // ----- breadcrumb -----

    public boolean isBreadcrumbVisible() {
        return waitForVisible(BREADCRUMB, "the breadcrumb bar").isDisplayed();
    }

    /** Breadcrumb labels from left to right. */
    public List<String> getBreadcrumbLabels() {
        waitForVisible(BREADCRUMB, "the breadcrumb bar");
        List<WebElement> items = waitForAllVisible(BREADCRUMB_ITEMS, "breadcrumb items");
        List<String> labels = new ArrayList<>();
        for (WebElement item : items) {
            labels.add(item.getAttribute("textContent").replace('\u00A0', ' ')
                    .replaceAll("\\s+", " ").trim());
        }
        return labels;
    }

    public CategoryPage clickBreadcrumbLink(String label) {
        By link = By.xpath("//div[contains(@class,'breadcrumbs')]//a[normalize-space(.)='" + label + "']");
        clickPresent(link, "breadcrumb link '" + label + "'");
        return new CategoryPage(driver);
    }

    public HomePage clickHomeBreadcrumb(String homeLabel) {
        clickBreadcrumbLink(homeLabel);
        return new HomePage(driver);
    }
}
