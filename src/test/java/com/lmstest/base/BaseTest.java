package com.lmstest.base;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

/**
 * Starts a fresh Chrome before every test and closes it afterwards.
 * Run headless with:  mvn test -Dheadless=true
 */
public abstract class BaseTest {

    public static final String BASE_URL = "https://school.moodledemo.net";
    // Demo student account published on the site's login page
    public static final String STUDENT_USER = "student";
    public static final String STUDENT_PASS = "moodle26";

    protected WebDriver driver;
    protected WebDriverWait wait;

    @BeforeEach
    void setUp() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--start-maximized", "--remote-allow-origins=*");
        if (Boolean.getBoolean("headless")) {
            options.addArguments("--headless=new", "--window-size=1920,1080");
        }
        driver = new ChromeDriver(options);
        wait = new WebDriverWait(driver, Duration.ofSeconds(20));
    }

    @AfterEach
    void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }
}
