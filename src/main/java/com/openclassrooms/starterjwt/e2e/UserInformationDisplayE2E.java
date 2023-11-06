package com.openclassrooms.starterjwt.e2e;
import org.junit.Test;
import org.openqa.selenium.*;

import org.openqa.selenium.firefox.FirefoxDriver;


public class UserInformationDisplayE2E {

    @Test
    public void testGoogleSearch() throws InterruptedException {
        /* Optional. If not specified, WebDriver searches the PATH for chromedriver.*/
        System.setProperty("webdriver.gecko.driver", "C:\\users\\hamateratsu\\documents\\geckodriver.exe");
        WebDriver driver = new FirefoxDriver();

        driver.get("http://www.google.com/");

        Thread.sleep(5000);  // Let the user actually see something!

        WebElement searchBox = driver.findElement(By.name("q"));

        searchBox.sendKeys("FirefoxDriver");

        searchBox.submit();

        Thread.sleep(5000);  // Let the user actually see something!

        driver.quit();
    }
}