package com.lmstest.tests;

import com.lmstest.base.BaseTest;
import com.lmstest.pages.CoursePage;
import com.lmstest.pages.LoginPage;
import com.lmstest.pages.MyCoursesPage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * M2 - Course Enrollment (TC04 - TC06)
 * Site: https://school.moodledemo.net  (resets every hour, login: student / moodle26)
 */
@TestMethodOrder(MethodOrderer.DisplayName.class)
class CourseEnrollmentTests extends BaseTest {

    @BeforeEach
    void login() {
        new LoginPage(driver, wait).open().loginAs(STUDENT_USER, STUDENT_PASS);
    }

    @Test
    @DisplayName("TC04 - My courses lists enrolled courses")
    void tc04_myCoursesListsEnrolledCourses() {
        MyCoursesPage myCourses = new MyCoursesPage(driver, wait).open();

        List<String> courses = myCourses.getCourseNames();

        assertFalse(courses.isEmpty(), "At least one enrolled course should be listed");
        System.out.println("TC04 enrolled courses: " + courses);
    }

    @Test
    @DisplayName("TC05 - Open a course")
    void tc05_openCourse() {
        MyCoursesPage myCourses = new MyCoursesPage(driver, wait).open();

        String expectedName = myCourses.openCourse(0);

        CoursePage course = new CoursePage(driver, wait);
        course.waitUntilLoaded();
        assertTrue(driver.getCurrentUrl().contains("/course/view.php"),
                "Should be on a course page but was: " + driver.getCurrentUrl());
        assertTrue(expectedName.toLowerCase().contains(course.getTitle().toLowerCase()),
                "Expected course title '" + expectedName + "' but page shows '" + course.getTitle() + "'");
    }

    @Test
    @DisplayName("TC06 - Course content is visible")
    void tc06_courseContentIsVisible() {
        new MyCoursesPage(driver, wait).open().openCourse(0);

        CoursePage course = new CoursePage(driver, wait);
        course.waitUntilLoaded();

        assertTrue(course.getSectionCount() > 0, "Course should list at least one section");
        assertTrue(course.getActivityCount() > 0, "Course should list at least one activity");

        String opened = course.openFirstResource();
        assertTrue(course.isResourceOpened(), "Resource/page should open without error: " + opened);
    }
}
