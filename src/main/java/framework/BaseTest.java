package framework;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;

import io.github.bonigarcia.wdm.WebDriverManager;


public class BaseTest {
    protected WebDriver driver;

    @BeforeMethod
    public void setUp() {
        driver = DriverFactory.initDriver();
        String baseUrl = System.getProperty("baseUrl",
            "https://demo.nopcommerce.com/");
        driver.get(baseUrl);
    }

    @AfterMethod()
    public void tearDown() {
        //DriverFactory.quitDriver();
    }
}

