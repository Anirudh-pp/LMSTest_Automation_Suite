# LMSTest_Automation_Suite

Selenium + JUnit 5 test suite for the Moodle demo LMS (`https://school.moodledemo.net`), built with the Page Object Model (POM).

## Project structure

```
src/test/java/com/lmstest/
├── base/   BaseTest.java              # shared WebDriver setup/teardown
├── pages/  LoginPage.java, ...        # one class per screen, exposes user actions
└── tests/  CourseEnrollmentTests.java # test cases, one @Test per scenario
```

### `base/BaseTest.java`

Abstract class every test class extends. It owns the `WebDriver`/`WebDriverWait` lifecycle so individual tests never create or close a browser themselves:

- `@BeforeEach setUp()` — launches a fresh Chrome session before every test (`--headless=true` system property runs it headless, e.g. `mvn test -Dheadless=true`).
- `@AfterEach tearDown()` — quits the browser after every test, pass or fail.
- Shared constants (`BASE_URL`, `STUDENT_USER`, `STUDENT_PASS`) used by any page/test that needs them.

Any new test class should `extends BaseTest` to get `driver` and `wait` for free instead of managing its own browser instance.

### `pages/` — Page Objects

Each class models one page/screen of the app and hides its selectors behind named methods, so tests describe *what* to do, not *how* the DOM is structured:

- Private `By` locators at the top of the class.
- Constructor takes the `driver`/`wait` created by `BaseTest`.
- An `open()`/`waitUntilLoaded()` method that navigates to the page and waits for it to be ready.
- Public methods for every user action or piece of data the tests need (e.g. `loginAs(...)`, `getCourseNames()`, `openFirstResource()`), each named after the behavior rather than the underlying HTML.

**To add a page object for a new screen:**
1. Create `pages/<Screen>Page.java` with a constructor `(WebDriver driver, WebDriverWait wait)`.
2. Define `By` locators for the elements that screen's tests will touch.
3. Add an `open()` (if tests navigate to it directly) or `waitUntilLoaded()` method using `wait.until(ExpectedConditions...)`.
4. Add one method per user action/assertion data point — keep raw `driver.findElement` calls inside the page object, never in a test.

### `tests/` — Test cases

Each test class `extends BaseTest`, groups related scenarios for one feature, and logs in or navigates via page objects in a `@BeforeEach` when every test in the class needs it. Every scenario is a `@Test` method with a `@DisplayName` describing the test case (e.g. `TC04 - My courses lists enrolled courses`), which only calls page object methods and asserts on their results.

**To add new test cases:**
1. Create `tests/<Feature>Tests.java extends BaseTest` (or add methods to an existing class if they share the same feature/setup).
2. Add a `@BeforeEach` for setup shared by all tests in the class (e.g. login), using the relevant page object(s).
3. Add a `@Test` method per scenario, named `tcNN_camelCaseDescription`, with `@DisplayName("TCNN - human description")`.
4. Drive the test only through page object methods (create any missing page object first), then assert with JUnit's `assertTrue`/`assertEquals`/etc.
5. If a step needs a selector that doesn't exist yet, add it to the relevant page object rather than reaching into the DOM from the test.

## Running the tests

```
mvn test                 # headed Chrome
mvn test -Dheadless=true # headless Chrome
```
