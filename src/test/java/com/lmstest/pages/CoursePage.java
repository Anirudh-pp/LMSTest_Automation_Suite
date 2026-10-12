package com.lmstest.pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;

public class CoursePage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    private final By heading    = By.cssSelector("h1");
    private final By sections   = By.cssSelector("li.section, [data-for='section']");
    private final By activities = By.cssSelector("li.activity, [data-for='cmitem']");
    // "Page" activities open as normal HTML, so they are preferred for the open-resource check
    private final By pageLinks        = By.cssSelector("li.activity.modtype_page a.aalink");
    private final By anyActivityLinks = By.cssSelector("li.activity a.aalink");

    public CoursePage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public void waitUntilLoaded() {
        wait.until(ExpectedConditions.urlContains("/course/view.php"));
        wait.until(ExpectedConditions.presenceOfElementLocated(heading));
    }

    public String getTitle() {
        return driver.findElement(heading).getText().trim();
    }

    public int getSectionCount() {
        wait.until(ExpectedConditions.presenceOfElementLocated(sections));
        return driver.findElements(sections).size();
    }

    public int getActivityCount() {
        wait.until(ExpectedConditions.presenceOfElementLocated(activities));
        return driver.findElements(activities).size();
    }

    /**
     * Opens one page/resource of the course. Navigates by the link's href so it also works
     * when the section is collapsed. Returns the URL that was opened.
     */
    public String openFirstResource() {
        List<WebElement> links = driver.findElements(pageLinks);
        if (links.isEmpty()) {
            links = driver.findElements(anyActivityLinks);
        }
        if (links.isEmpty()) {
            throw new AssertionError("No activity links found on the course page");
        }
        String href = links.get(0).getAttribute("href");
        driver.get(href);
        return href;
    }

    public boolean isResourceOpened() {
        wait.until(ExpectedConditions.urlContains("/mod/"));
        boolean hasHeading = !driver.findElements(heading).isEmpty()
                && !driver.findElement(heading).getText().trim().isEmpty();
        boolean hasError = !driver.findElements(By.cssSelector(".errorbox, .alert-danger")).isEmpty();
        return hasHeading && !hasError;
    }
}
