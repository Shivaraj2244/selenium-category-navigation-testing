package config;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Properties;

/**
 * Central place for test settings. Values come from config.properties,
 * and any value can be overridden with -Dkey=value on the Maven command line.
 */
public final class TestConfig {

    private static final Properties PROPS = new Properties();

    static {
        try (InputStream in = TestConfig.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in == null) {
                throw new IllegalStateException("config.properties not found on the classpath "
                        + "(expected in src/main/resources).");
            }
            PROPS.load(in);
        } catch (IOException e) {
            throw new IllegalStateException("Could not read config.properties", e);
        }
    }

    private TestConfig() {
    }

    private static String get(String key) {
        String override = System.getProperty(key);
        String value = (override != null && !override.isBlank()) ? override : PROPS.getProperty(key);
        if (value == null) {
            throw new IllegalStateException("Missing configuration key: " + key);
        }
        return value.trim();
    }

    public static String baseUrl() {
        String url = get("base.url");
        return url.endsWith("/") ? url.substring(0, url.length() - 1) : url;
    }

    public static boolean headless() {
        return Boolean.parseBoolean(get("headless"));
    }

    public static Duration timeout() {
        return Duration.ofSeconds(Long.parseLong(get("timeout.seconds")));
    }

    public static Duration pageLoadTimeout() {
        return Duration.ofSeconds(Long.parseLong(get("page.load.timeout.seconds")));
    }

    public static Path screenshotDir() {
        return Paths.get(get("screenshot.dir"));
    }

    public static String parentCategoryName() {
        return get("parent.category.name");
    }

    public static String parentCategoryPath() {
        return get("parent.category.path");
    }

    public static String subCategoryName() {
        return get("sub.category.name");
    }

    public static String subCategoryPath() {
        return get("sub.category.path");
    }

    public static String homeBreadcrumbLabel() {
        return get("home.breadcrumb.label");
    }
}
