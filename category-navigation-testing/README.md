# Project 47 - Selenium Tests for Category Navigation and Breadcrumbs

Java 17, Selenium WebDriver 4.25, JUnit 5.10, Maven, Chrome, and the Page Object Model.

Inspected reference site: <https://automationexercise.com/>
Execution target: deterministic local demo server started by the JUnit tests.

## Verified site behavior

AutomationExercise has a Products page at `/products`. Its Category menu contains Women, Men, and Kids. The Men section expands to real category links, including Tshirts at `/category_products/3`. The local fixture reproduces this inspected flow and DOM so Selenium execution is deterministic.

The Men's Tshirts page has this breadcrumb markup and hierarchy:

`Products` (clickable) > `Men > Tshirts` (current page)

There is no separate clickable Men breadcrumb. The suite therefore tests the supported Products breadcrumb navigation and does not invent a parent-category breadcrumb.

## Test cases

`CategoryNavigationTest` contains 11 tests covering:

- Home page loading and the AutomationExercise logo.
- Products navigation and category-menu visibility.
- Men -> Tshirts category navigation.
- Men's Tshirts page URL and heading.
- Breadcrumb visibility, labels, and order.
- The clickable Products breadcrumb returning to `/products`.
- Availability of multiple top-level categories.
- A missing-link diagnostic assertion.

Every test starts an independent Chrome session. Explicit Selenium waits are used; the project does not use fixed sleeps. Shared page helpers detect Cloudflare/human-verification pages, access-denied pages, and navigation timeouts and report the current URL in the failure message. No security challenge is bypassed.

## Project structure

```text
src/main/java/config/TestConfig.java
src/main/resources/config.properties
src/test/java/pages/{BasePage,HomePage,CategoryPage}.java
src/test/java/tests/{BaseTest,CategoryNavigationTest}.java
src/test/java/utils/{LocalDemoServer,ScreenshotUtil,ScreenshotOnFailureExtension}.java
src/test/resources/junit-platform.properties
test-output/screenshots/
```

The JUnit extension captures a PNG after a failed test under `test-output/screenshots/`. Existing screenshots are preserved.

## Run the tests in PowerShell

```powershell
cd C:\Users\Admin\Downloads\category-navigation-testing
mvn.cmd clean test
```

Useful options:

```powershell
mvn.cmd test -Dheadless=true
mvn.cmd test -Dtest=CategoryNavigationTest
mvn.cmd test -Dtimeout.seconds=40
```

Chrome must be installed. Selenium Manager obtains the compatible ChromeDriver. The local server binds to an available loopback port and stops after the test class. Test reports are written to `target/surefire-reports/`.

## Current execution status

The project uses the local fixture for execution because nopCommerce displayed Cloudflare verification and AutomationExercise's public ads/renderer caused intermittent timeouts. The public site is not bypassed or used as a test dependency.

Latest local execution: `Tests run: 11, Failures: 0, Errors: 0, Skipped: 0`.
