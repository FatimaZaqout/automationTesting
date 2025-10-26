package pages;

import org.openqa.selenium.*;

public class ComparePage extends BasePage {
    private final By compareLink = By.linkText("Compare products list");
    public ComparePage(WebDriver driver) {super(driver);}
    public ComparePage scrollToBottomCompletely() {
        JavascriptExecutor js = (JavascriptExecutor) driver;

        long lastHeight = ((Number) js.executeScript(
                "return Math.max(document.body.scrollHeight, document.documentElement.scrollHeight)"))
                .longValue();
        int attempts = 0;
        while (attempts++ < 20) { 
            js.executeScript("window.scrollTo(0, document.body.scrollHeight);");
            try { Thread.sleep(400); } catch (InterruptedException ignored) {}

            long newHeight = ((Number) js.executeScript(
                    "return Math.max(document.body.scrollHeight, document.documentElement.scrollHeight)"))
                    .longValue();
            if (newHeight == lastHeight) break;
            lastHeight = newHeight;
        }
        waits.visible(compareLink);
        return this;
    }

    public ComparePage clickCompareProductsList() {
        waits.scrollIntoView(driver.findElement(compareLink));
        waits.click(compareLink);
        return this;
    }
}
