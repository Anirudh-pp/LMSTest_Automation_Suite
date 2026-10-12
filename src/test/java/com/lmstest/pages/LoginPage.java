package com.lmstest.pages;

import com.lmstest.base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class LoginPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By usernameField = By.id("username");
    private final By passwordField = By.id("password");
    private final By loginButton   = By.id("loginbtn");

    public LoginPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public LoginPage open() {
        driver.get(BaseTest.BASE_URL + "/login/index.php");
        wait.until(ExpectedConditions.visibilityOfElementLocated(usernameField));
        return this;
    }

    /** Logs in and waits until Moodle leaves the login page. */
    public void loginAs(String username, String password) {
        driver.findElement(usernameField).sendKeys(username);
        driver.findElement(passwordField).sendKeys(password);
        driver.findElement(loginButton).click();
        wait.until(ExpectedConditions.not(ExpectedConditions.urlContains("/login/")));
    }
}
