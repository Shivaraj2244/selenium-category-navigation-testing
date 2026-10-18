package tests;

import config.TestConfig;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import utils.ScreenshotOnFailureExtension;
import utils.LocalDemoServer;

/** Starts Chrome before each test and closes it afterwards. Selenium Manager finds the driver. */
@ExtendWith(ScreenshotOnFailureExtension.class)
public abstract class BaseTest {

    protected WebDriver driver;

    @BeforeAll
    static void startLocalDemo() {
        System.setProperty("base.url", LocalDemoServer.start());
    }

    @AfterAll
    static void stopLocalDemo() {
        LocalDemoServer.stop();
        System.clearProperty("base.url");
    }

    @BeforeEach
    void setUpBrowser() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--window-size=1920,1080", "--disable-notifications", "--remote-allow-origins=*");
        if (TestConfig.headless()) {
            options.addArguments("--headless=new");
        }
        driver = new ChromeDriver(options);
        driver.manage().timeouts().pageLoadTimeout(TestConfig.pageLoadTimeout());
        if (!TestConfig.headless()) {
            driver.manage().window().maximize();
        }
    }

    @AfterEach
    void tearDownBrowser() {
        if (driver != null) {
            driver.quit();
        }
    }

    public WebDriver getDriver() {
        return driver;
    }
}
