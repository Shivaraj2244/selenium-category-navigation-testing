package tests;

import config.TestConfig;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import pages.CategoryPage;
import pages.HomePage;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/** Category navigation and breadcrumb tests for AutomationExercise. */
class CategoryNavigationTest extends BaseTest {

    private final String parentName = TestConfig.parentCategoryName();
    private final String parentPath = TestConfig.parentCategoryPath();
    private final String subName = TestConfig.subCategoryName();
    private final String subPath = TestConfig.subCategoryPath();
    private final String productsHeading = "All Products";
    private final String categoryHeading = "Men - Tshirts Products";

    private HomePage openHome() {
        return new HomePage(driver).open();
    }

    private CategoryPage openProducts() {
        CategoryPage products = openHome().openProducts();
        products.waitForPath(parentPath);
        return products;
    }

    private CategoryPage openTshirtsCategory() {
        CategoryPage tshirts = openProducts().openCategory(parentName, subPath);
        tshirts.waitForPath(subPath);
        return tshirts;
    }

    @Test
    @DisplayName("TC01 - Home page opens successfully")
    void tc01_home_page_opens() {
        HomePage home = openHome();
        assertEquals("/", home.currentPath());
        assertTrue(home.isLogoVisible(), "AutomationExercise logo should be visible");
    }

    @Test
    @DisplayName("TC02 - Products category menu is visible")
    void tc02_category_menu_is_visible() {
        CategoryPage products = openProducts();
        assertTrue(products.isCategoryMenuVisible(), "Category menu should be visible");
        assertTrue(products.isCategoryVisible(parentName), "Category menu should contain " + parentName);
    }

    @Test
    @DisplayName("TC03 - Products page opens from the home navigation")
    void tc03_products_page_opens() {
        CategoryPage products = openProducts();
        assertEquals(productsHeading, products.getHeading());
    }

    @Test
    @DisplayName("TC04 - Category menu opens the Men's Tshirts category")
    void tc04_category_navigation_opens_tshirts() {
        CategoryPage tshirts = openTshirtsCategory();
        assertEquals(categoryHeading, tshirts.getHeading());
    }

    @Test
    @DisplayName("TC05 - Breadcrumb is visible on the category page")
    void tc05_breadcrumb_is_visible() {
        assertTrue(openTshirtsCategory().isBreadcrumbVisible(), "Breadcrumb should be visible");
    }

    @Test
    @DisplayName("TC06 - Breadcrumb labels match the observed hierarchy")
    void tc06_breadcrumb_labels_match_hierarchy() {
        assertEquals(List.of("Products", "Men > Tshirts"), openTshirtsCategory().getBreadcrumbLabels());
    }

    @Test
    @DisplayName("TC07 - Breadcrumb items are in the correct order")
    void tc07_breadcrumb_order() {
        List<String> labels = openTshirtsCategory().getBreadcrumbLabels();
        assertEquals(2, labels.size(), "Expected Products and Men > Tshirts");
        assertEquals("Products", labels.get(0));
        assertEquals("Men > Tshirts", labels.get(1));
    }

    @Test
    @DisplayName("TC08 - Products breadcrumb opens the products page")
    void tc08_products_breadcrumb_navigates_to_parent() {
        CategoryPage products = openTshirtsCategory().clickBreadcrumbLink("Products");
        products.waitForPath(parentPath);
        assertEquals(productsHeading, products.getHeading());
    }

    @Test
    @DisplayName("TC09 - Products page exposes multiple top-level categories")
    void tc09_multiple_categories_are_available() {
        CategoryPage products = openProducts();
        assertTrue(products.isCategoryVisible("Men"));
        assertTrue(products.isCategoryVisible("Kids"));
    }

    @Test
    @DisplayName("TC10 - Men's Tshirts URL and heading are correct")
    void tc10_url_and_heading_match_category() {
        CategoryPage tshirts = openTshirtsCategory();
        assertEquals(subPath, tshirts.currentPath());
        assertEquals(categoryHeading, tshirts.getHeading());
        assertTrue(tshirts.getHeading().contains(subName));
    }

    @Test
    @DisplayName("TC11 - Missing category link gives a meaningful error")
    void tc11_missing_category_error_message() {
        CategoryPage products = openProducts();
        AssertionError error = org.junit.jupiter.api.Assertions.assertThrows(AssertionError.class,
                () -> products.openCategory(parentName, "/category_products/does-not-exist"));
        assertTrue(error.getMessage().contains("does-not-exist"),
                "Message should name the missing link: " + error.getMessage());
    }
}
