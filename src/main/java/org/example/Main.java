package org.example;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class Main {
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver", "src\\test\\resources\\chromedriver.exe");
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");

        WebDriver chromeDriver = new ChromeDriver(options);
        chromeDriver.manage().window().minimize();
//        chromeDriver.get("https://test-stand.gb.ru/login");
//        chromeDriver.get("https://www.google.com");

        System.out.println("title = " + chromeDriver.getTitle());

        chromeDriver.quit();

//        System.setProperty("webdriver.firefox.driver", "src\\test\\resources\\geckodriver.exe");
//        FirefoxOptions firefoxOptions =
//        WebDriver ffDriver = new FirefoxDriver();
//        ffDriver.get("https://test-stand.gb.ru/login");




    }
}