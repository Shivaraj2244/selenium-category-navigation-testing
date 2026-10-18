package pages;

import config.TestConfig;
import org.openqa.selenium.By;
import org.openqa.selenium.ElementClickInterceptedException;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/** Shared explicit-wait helpers. Every failure carries a readable message. */
public abstract class BasePage {

    protected final WebDriver driver;

    protected BasePage(WebDriver driver) {
        this.driver = driver;
    }

    protected WebDriverWait waitOf(Duration timeout) {
        return new WebDriverWait(driver, timeout);
    }

    protected void open(String url, String description) {
        try {
            driver.get(url);
            failIfBlocked(description);
        } catch (TimeoutException e) {
            throw new AssertionError("Timed out loading " + description + " at " + url
                    + ". Check connectivity and the failure screenshot.", e);
        }
    }

    private void failIfBlocked(String description) {
        String title = driver.getTitle().toLowerCase();
        String body = driver.getPageSource().toLowerCase();
        if (title.contains("just a moment") || body.contains("verify you are human")
                || body.contains("cf-chl-")) {
            throw new AssertionError("Blocked while loading " + description
                    + ": a Cloudflare or human-verification challenge was displayed at "
                    + driver.getCurrentUrl());
        }
        if (title.contains("access denied") || title.contains("forbidden")
                || body.contains("access denied") || body.contains("403 forbidden")) {
            throw new AssertionError("Blocked while loading " + description
                    + ": access was denied at " + driver.getCurrentUrl());
        }
    }

    protected WebElement waitForVisible(By locator, String description) {
        try {
            failIfBlocked(description);
            return waitOf(TestConfig.timeout()).until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            throw new AssertionError("Timed out waiting for " + description + " " + locator
                    + " on page " + driver.getCurrentUrl()
                    + ". The page may be unavailable or its markup may have changed.", e);
        }
    }

    protected List<WebElement> waitForAllVisible(By locator, String description) {
        try {
            failIfBlocked(description);
            return waitOf(TestConfig.timeout())
                    .until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
        } catch (TimeoutException e) {
            throw new AssertionError("Timed out waiting for " + description + " " + locator
                    + " on page " + driver.getCurrentUrl()
                    + ". The page may be unavailable or its markup may have changed.", e);
        }
    }

    protected void click(By locator, String description, Duration timeout) {
        try {
            WebElement element = waitOf(timeout).until(ExpectedConditions.elementToBeClickable(locator));
            try {
                element.click();
            } catch (ElementClickInterceptedException e) {
                ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
            }
        } catch (TimeoutException e) {
            throw new AssertionError("Could not click " + description + " " + locator
                    + " (not found or not clickable) on page " + driver.getCurrentUrl(), e);
        }
    }

    protected void click(By locator, String description) {
        click(locator, description, TestConfig.timeout());
    }

    protected void clickPresent(By locator, String description) {
        try {
            WebElement element = waitOf(TestConfig.timeout())
                    .until(ExpectedConditions.presenceOfElementLocated(locator));
                ((JavascriptExecutor) driver).executeScript("window.location.assign(arguments[0].href);", element);
        } catch (TimeoutException e) {
            throw new AssertionError("Could not find " + description + " " + locator
                    + " on page " + driver.getCurrentUrl(), e);
        }
    }

    /** Waits until the browser path (ignoring host and query) equals the expected path. */
    public void waitForPath(String expectedPath) {
        try {
            waitOf(TestConfig.timeout()).until(d -> expectedPath.equals(currentPath()));
        } catch (TimeoutException e) {
            throw new AssertionError("Navigation timeout: expected path '" + expectedPath
                    + "' but browser is at '" + driver.getCurrentUrl() + "'", e);
        }
    }

    public String currentPath() {
        String path = java.net.URI.create(driver.getCurrentUrl()).getPath();
        if (path == null || path.isEmpty()) {
            return "/";
        }
        return (path.length() > 1 && path.endsWith("/")) ? path.substring(0, path.length() - 1) : path;
    }

    public String currentUrl() {
        return driver.getCurrentUrl();
    }
}
