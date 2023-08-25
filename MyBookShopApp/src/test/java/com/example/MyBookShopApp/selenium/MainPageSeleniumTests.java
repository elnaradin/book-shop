package com.example.MyBookShopApp.selenium;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.chrome.ChromeDriver;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class MainPageSeleniumTests {
    private static ChromeDriver driver;

    @BeforeAll
    static void setup() {
        System.setProperty("webdriver.chrome.driver", "C:\\Users\\user\\Downloads\\chromedriver-win64\\chromedriver.exe");
        driver = new ChromeDriver();

        driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
    }

    @AfterAll
    static void tearDown() {
        driver.quit();
    }

        @Test
    void testMainPageAccess() throws InterruptedException {
        MainPage mainPage = new MainPage(driver);
        mainPage
                .callPage()
                .pause();

        assertTrue(driver.getPageSource().contains("BOOKSHOP"));
    }
    @Test
    void testMainPageSearchQuery() throws InterruptedException {
        MainPage mainPage = new MainPage(driver);
        mainPage
                .callPage()
                .pause()
                .openSearchField()
                .pause()
                .setUpSearchToken("night")
                .pause()
                .submit()
                .pause();
        assertTrue(driver.getPageSource().contains("Wings of Fire"));
    }
    @Test
    @DisplayName("Сценарий навигации по разделам")
    void testShopDepartments() throws InterruptedException {
        MainPage mainPage = new MainPage(driver);
        mainPage
                .callPage()
                .pause()
                .changeLanguageToEng()
                .pause();
        visitBookAndAuthorPages(mainPage)
                .returnToMainPage()
                .pause()
                .openBooksByTag("open system")
                .pause();
        assertTrue(driver.getPageSource().contains("open system"));
        visitBookAndAuthorPages(mainPage)
                .openGenrePage()
                .pause()
                .openBooksByGenre("service-desk")
                .pause();
        assertTrue(driver.getPageSource().contains("service-desk"));
        visitBookAndAuthorPages(mainPage)
                .openRecentBooksPage()
                .pause();
        assertTrue(driver.getPageSource().contains("from"));
        mainPage
                .changePeriod("01.01.2022", "01.02.2023")
                .pause();
        visitBookAndAuthorPages(mainPage)
                .openPopularBooksPage()
                .pause();
        visitBookAndAuthorPages(mainPage)
                .openAuthorsListPage()
                .pause()
                .openAuthorPage("Aleda Rawlison")
                .pause();
        assertTrue(driver.getPageSource().contains("<h1 class=\"Middle-title\">Aleda Rawlison</h1>"));
        mainPage
                .openAuthorBooks()
                .pause()
                .openFirstBookPage()
                .pause();
        assertTrue(driver.getPageSource().contains("Aleda Rawlison"));
    }

    private static MainPage visitBookAndAuthorPages(MainPage mainPage) throws InterruptedException {
        return mainPage
                .openFirstBookPage()
                .pause()
                .openFirstAuthorPage()
                .pause();
    }

}