package utils;

import config.TestConfig;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/** Saves browser screenshots into test-output/screenshots (folder is created automatically). */
public final class ScreenshotUtil {

    private ScreenshotUtil() {
    }

    public static Path capture(WebDriver driver, String testName) {
        try {
            Path dir = TestConfig.screenshotDir();
            Files.createDirectories(dir);
            String stamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String safeName = testName.replaceAll("[^a-zA-Z0-9._-]", "_");
            Path target = dir.resolve(safeName + "_" + stamp + ".png");
            Path temp = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE).toPath();
            Files.copy(temp, target, StandardCopyOption.REPLACE_EXISTING);
            System.out.println("[Screenshot] saved: " + target.toAbsolutePath());
            return target;
        } catch (IOException | RuntimeException e) {
            System.err.println("[Screenshot] could not be saved: " + e.getMessage());
            return null;
        }
    }
}
