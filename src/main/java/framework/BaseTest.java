package framework;

import org.openqa.selenium.*;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.testng.annotations.Listeners;
import io.qameta.allure.testng.AllureTestNg;

@Listeners({ AllureTestNg.class })
public class BaseTest {
    protected WebDriver driver;
    
    @BeforeMethod(alwaysRun = true)
    public void BeforeMethod(Object[] params) {
    	  driver = DriverFactory.initDriver();
          String baseUrl = System.getProperty("baseUrl", "https://demo.nopcommerce.com/");
          driver.get(baseUrl);
        takeScreenshot("BEFORE", "setup", null);
    }
    
    
    @AfterMethod(alwaysRun = true)
    public void AfterMethod(ITestResult result) {
        String status;
        switch (result.getStatus()) {
            case ITestResult.SUCCESS: status = "AFTER_PASS"; break;
            case ITestResult.FAILURE: status = "AFTER_FAIL"; break;
            default:                  status = "AFTER_SKIP";
        }

        String testName  = result.getMethod().getMethodName();
        String className = result.getTestClass().getRealClass().getSimpleName();

        takeScreenshot(status, testName, className);

        if (result.getStatus() == ITestResult.FAILURE) {
            attachAllureText("Failed Test Name", testName);
            try {
                attachAllureText("Current URL", safeGetCurrentUrl());
            } catch (Exception ignored) {}
        }
   	 DriverFactory.quitDriver();
       
    }

 
    private void takeScreenshot(String phase, String name, String className) {
        try {
            if (driver == null) return;
            if (!(driver instanceof TakesScreenshot)) return;

            String cls = (className == null || className.isBlank()) ? "_common" : className;
            Path folder = Path.of("screenshots", cls, (name == null ? "_misc" : name));
            Files.createDirectories(folder);

            String time = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS").format(new Date());
            String fileName = (name == null ? "shot" : name) + "_" + phase + "_" + time + ".png";
            Path dest = folder.resolve(fileName);

            File src = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(src.toPath(), dest);

            System.out.println("Saved screenshot: " + dest.toAbsolutePath());

            try {
                byte[] bytes = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
                attachAllureImage("Screenshot - " + phase, bytes);
            } catch (Throwable ignore) {
            	
            }

        } catch (Exception e) {
            System.out.println("Screenshot failed: " + e.getMessage());
        }
    }

    private String safeGetCurrentUrl() {
        try { return driver.getCurrentUrl(); }
        catch (Exception e) { return "N/A"; }
    }

     private void attachAllureImage(String name, byte[] bytes) {
        try {
            io.qameta.allure.Allure.addAttachment(name, "image/png", new ByteArrayInputStream(bytes), ".png");
        } catch (Throwable ignored) { 
        	
        }
    }

    private void attachAllureText(String name, String content) {
        try {
            io.qameta.allure.Allure.addAttachment(name, "text/plain", content);
        } catch (Throwable ignored) {
        	
        }
    }
}
