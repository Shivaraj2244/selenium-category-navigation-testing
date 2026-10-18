package utils;

import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import tests.BaseTest;

/** JUnit 5 extension: takes a screenshot whenever a test fails (runs before driver.quit()). */
public class ScreenshotOnFailureExtension implements AfterTestExecutionCallback {

    @Override
    public void afterTestExecution(ExtensionContext context) {
        if (context.getExecutionException().isPresent()) {
            Object instance = context.getRequiredTestInstance();
            if (instance instanceof BaseTest base && base.getDriver() != null) {
                ScreenshotUtil.capture(base.getDriver(),
                        context.getRequiredTestClass().getSimpleName() + "_" + context.getRequiredTestMethod().getName());
            }
        }
    }
}
