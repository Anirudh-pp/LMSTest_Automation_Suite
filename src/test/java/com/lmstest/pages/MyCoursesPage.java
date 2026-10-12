package com.lmstest.pages;

import com.lmstest.base.BaseTest;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.ArrayList;
import java.util.List;

public class MyCoursesPage {

    private final WebDriver driver;
    private final WebDriverWait wait;

    // Course name links inside the "My courses" overview block
    private final By courseLinks = By.cssSelector("[data-region='courses-view'] a.coursename");

    public MyCoursesPage(WebDriver driver, WebDriverWait wait) {
        this.driver = driver;
        this.wait = wait;
    }

    public MyCoursesPage open() {
        driver.get(BaseTest.BASE_URL + "/my/courses.php");
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(courseLinks, 0));
        return this;
    }

    /** Names of all enrolled courses currently listed. */
    public List<String> getCourseNames() {
        List<String> names = new ArrayList<>();
        for (WebElement link : driver.findElements(courseLinks)) {
            names.add(nameOf(link));
        }
        return names;
    }

    /** Clicks the course at the given position and returns its name. */
    public String openCourse(int index) {
        WebElement link = driver.findElements(courseLinks).get(index);
        String name = nameOf(link);
        link.click();
        return name;
    }

    // The link can contain a hidden "Course name" label; read the visible text only.
    private String nameOf(WebElement link) {
        List<WebElement> visible = link.findElements(By.cssSelector(".multiline"));
        String text = visible.isEmpty() ? link.getText() : visible.get(0).getText();
        // Moodle repeats the name for screen readers, so keep only the first non-empty line
        for (String line : text.replace("Course name", "").split("\\R")) {
            if (!line.trim().isEmpty()) {
                return line.trim();
            }
        }
        return "";
    }
}