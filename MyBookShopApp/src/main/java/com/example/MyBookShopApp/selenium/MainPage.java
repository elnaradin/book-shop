package com.example.MyBookShopApp.selenium;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.Select;

public class MainPage {
    private String url = "http://localhost:8085/";
    private final ChromeDriver chromeDriver;

    public MainPage(ChromeDriver chromeDriver) {
        this.chromeDriver = chromeDriver;
    }

    public MainPage callPage() {
        chromeDriver.get(url);
        return this;
    }

    public MainPage pause() throws InterruptedException {
        Thread.sleep(1000);
        return this;
    }

    public MainPage setUpSearchToken(String token) {
        WebElement element = chromeDriver.findElement(By.id("query"));
        element.sendKeys(token);
        return this;
    }

    public MainPage submit() {
        WebElement element = chromeDriver.findElement(By.id("search"));
        element.submit();
        return this;
    }

    public MainPage openSearchField() {
        WebElement element = chromeDriver.findElementByClassName("Header-searchLink");
        element.click();
        return this;
    }

    public MainPage openFirstBookPage() {
        WebElement element = chromeDriver.findElementByClassName("Card-picture");
        element.click();
        return this;
    }

    public MainPage openFirstAuthorPage() {
        WebElement element = chromeDriver.findElementByClassName("author_link");
        element.click();
        return this;
    }

    public MainPage returnToMainPage() {
        WebElement element = chromeDriver.findElementByLinkText("Main");
        element.click();
        return this;
    }

    public MainPage openBooksByTag(String tag) {
        WebElement element = chromeDriver.findElementByLinkText(tag);
        element.click();
        return this;
    }

    public MainPage openGenrePage() {
        WebElement element = chromeDriver.findElementByLinkText("Genres");
        element.click();
        return this;
    }

    public MainPage openBooksByGenre(String genre) {
        WebElement element = chromeDriver.findElementByPartialLinkText(genre);
        element.click();
        return this;
    }

    public MainPage openRecentBooksPage() {
        WebElement element = chromeDriver.findElementByLinkText("Recent");
        element.click();
        return this;
    }

    public MainPage changePeriod(String from, String to) {
        WebElement elementFrom = chromeDriver.findElementById("fromdaterecent");
        elementFrom.clear();
        elementFrom.sendKeys(from);
        WebElement elementTo = chromeDriver.findElementById("enddaterecent");
        elementTo.clear();
        elementTo.sendKeys(to);
        return this;
    }

    public MainPage openPopularBooksPage() {
        WebElement element = chromeDriver.findElementByLinkText("Popular");
        element.click();
        return this;
    }

    public MainPage changeLanguageToEng() {
        Select dropdown = new Select(chromeDriver.findElementById("locales"));
        dropdown.selectByValue("en");
        return this;
    }

    public MainPage openAuthorsListPage() {
        WebElement element = chromeDriver.findElementByLinkText("Authors");
        element.click();
        return this;
    }

    public MainPage openAuthorPage(String name) {
        WebElement element = chromeDriver.findElementByLinkText(name);
        element.click();
        return this;
    }

    public MainPage openAuthorBooks() {
        WebElement element = chromeDriver.findElementByPartialLinkText("All books by the author");
        element.click();
        return this;
    }
}
