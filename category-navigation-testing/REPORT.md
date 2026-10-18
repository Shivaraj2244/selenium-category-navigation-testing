# Project 47 – Selenium Tests for Category Navigation and Breadcrumbs (Project Report)

## 1. Abstract
This project builds an automated test suite in Java that checks category navigation and breadcrumb behaviour of the public nopCommerce demo store. It uses Selenium WebDriver to control Chrome, JUnit 5 to organise and assert tests, Maven to manage the build, and the Page Object Model to keep code clean. Failed tests save screenshots, and Maven produces text, XML and HTML reports.

## 2. Problem statement
Online stores depend on menus and breadcrumbs to help users find products. Checking them by hand after every change is slow and error-prone. An automated suite can repeat these checks quickly and consistently.

## 3. Objectives
- Verify the home page and category menu load correctly.
- Verify navigation from a category to a sub-category.
- Verify breadcrumb visibility, labels, order and links.
- Handle missing elements and timeouts with clear messages.
- Capture screenshots on failure and produce reports.

## 4. Scope
In scope: front-end UI tests of one existing website (Computers → Notebooks path), run on desktop Chrome. Out of scope: login, cart, checkout, mobile layout, performance and security testing, other browsers.

## 5. Requirements
Software: Windows 10/11, JDK 17+, Maven 3.9+, Google Chrome (current), VS Code with Extension Pack for Java, internet access.
Hardware: 4 GB RAM minimum (8 GB recommended), 1 GB free disk.

## 6. System architecture
```
JUnit 5 tests (CategoryNavigationTest)  <- BaseTest (start/stop Chrome, screenshot on failure)
        |
Page Objects (HomePage, CategoryPage) -> BasePage (explicit waits, error messages)
        |
Selenium WebDriver -> ChromeDriver -> Chrome -> demo.nopcommerce.com
Support: TestConfig + config.properties, ScreenshotUtil, Surefire reports
```

## 7. Technologies
- **Selenium WebDriver:** library that controls a real browser (open pages, find elements, click, read text).
- **JUnit 5:** test framework providing `@Test`, setup/teardown and assertions.
- **Maven:** downloads dependencies (`pom.xml`) and runs build and test commands.
- **Page Object Model:** each page is a class holding its locators and actions, so tests read like user steps and a changed locator is fixed in one place.

## 8. Test cases
Preconditions for all: Chrome installed, site reachable. Test data: parent = Computers (`/computers`), sub-category = Notebooks (`/notebooks`) – *to be confirmed on the live site*.

| ID | Steps | Expected result | Actual result |
|---|---|---|---|
| TC01 | Open base URL | Home page loads, logo visible | [To be filled after execution] |
| TC02 | Open home page, look at top menu | Menu visible and contains Computers | [To be filled] |
| TC03 | Click Computers | URL path `/computers`, heading "Computers" | [To be filled] |
| TC04 | On Computers page click Notebooks tile | Notebooks page opens, heading "Notebooks" | [To be filled] |
| TC05 | Open Computers, then Notebooks | Breadcrumb visible on both | [To be filled] |
| TC06 | Read breadcrumb on both pages | `Home, Computers` and `Home, Computers, Notebooks` | [To be filled] |
| TC07 | Read breadcrumb on Notebooks | Order: Home, Computers, Notebooks | [To be filled] |
| TC08 | On Notebooks click "Computers" breadcrumb | `/computers` opens | [To be filled] |
| TC09 | On Notebooks click "Home" breadcrumb | Home page opens | [To be filled] |
| TC10 | Open Notebooks | URL `/notebooks` and heading "Notebooks" | [To be filled] |
| TC11 | Click a non-existent menu link | Test gets an `AssertionError` naming the missing element | [To be filled] |

## 9. Testing methodology
Black-box, UI-level functional testing, automated with a data-driven configuration. Each test starts a fresh browser (independent tests), uses explicit waits, and asserts on URL, heading and breadcrumb text. Expected values were taken from the site's observed hierarchy, not invented.

## 10. Results
Execution date: [fill]  Chrome version: [fill]  Tests run / passed / failed: [fill]
Attach: `surefire-report.html` screenshot, list of files in `test-output/screenshots`, and notes on any selector corrections made. *No results are claimed in this document before the suite is actually run.*

## 11. Conclusion
The project shows how a maintainable Selenium framework can verify navigation and breadcrumbs on a live e-commerce site, with clear failure messages and evidence (screenshots, reports).

## 12. Future enhancements
Cross-browser runs (Firefox, Edge), more categories via data-driven tests (`@ParameterizedTest`), product-page breadcrumbs, mobile menu tests, Allure reports, CI with GitHub Actions, parallel execution.

## 13. Viva questions and answers
1. **What is Selenium WebDriver?** A browser-automation API that sends commands to a real browser through a driver (ChromeDriver).
2. **Why Page Object Model?** Separates locators/actions from test logic → less duplication, easier maintenance.
3. **Explicit vs implicit wait?** Explicit waits for a specific condition on a specific element (used here); implicit applies a global polling delay. Mixing them is discouraged.
4. **Why avoid `Thread.sleep()`?** It waits a fixed time: slow when the page is fast, flaky when it is slow.
5. **What does Selenium Manager do?** Automatically finds/downloads the right browser driver.
6. **What does Maven Surefire do?** Runs tests during `mvn test` and writes reports.
7. **How are failures captured?** `ScreenshotOnFailureExtension` runs after each test; if it threw an exception, `ScreenshotUtil` saves a PNG.
8. **Why CSS selector with `a[href$='/computers']`?** Based on the URL (stable) rather than visible text or position (fragile).
9. **Why is `@BeforeEach` used?** Fresh browser per test keeps tests independent.
10. **What are the project's limits?** One website, one browser, depends on the site's current HTML and availability; selectors must be re-verified if the site changes.
