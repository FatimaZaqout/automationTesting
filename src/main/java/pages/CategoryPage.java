package pages;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

public class CategoryPage extends BasePage {
    private  By sortDropdown       = By.id("products-orderby");
    private  By productCards       = By.cssSelector(".product-item");
    private  By productTitleInCard = By.cssSelector(".product-title a");
    private  By priceInCard        = By.cssSelector(".prices .actual-price, span.price");

    // Compare
    private  By compareBtn         = By.cssSelector("a.add-to-compare-list-button, .add-to-compare-list-button");
    private  By successBar         = By.cssSelector("div.bar-notification.success");
    private  By successBarClose    = By.cssSelector("div.bar-notification.success span.close");
    private  By footerCompareLink  = By.cssSelector("a[href='/compareproducts']");

    private WebDriverWait w10() { return new WebDriverWait(driver, Duration.ofSeconds(10)); }
    public CategoryPage(WebDriver driver) { super(driver); }

    /* ---------- Sorting ---------- */
    public CategoryPage sortBy(String visibleText) {
        WebElement first = driver.findElements(productCards).stream().findFirst().orElse(null);

        new Select(waits.visible(sortDropdown)).selectByVisibleText(visibleText);
        w10().until(d -> new Select(d.findElement(sortDropdown))
                .getFirstSelectedOption().getText().trim().equals(visibleText.trim()));

        if (first != null) {
            try { w10().until(ExpectedConditions.stalenessOf(first)); } catch (Exception ignored) {}
        }
        waits.visible(productCards);
        return this;
    }

    /* ---------- Read names & prices ---------- */
    public List<String> names() {
        return driver.findElements(productCards).stream()
                .map(card -> card.findElements(productTitleInCard))
                .filter(list -> !list.isEmpty())
                .map(list -> list.get(0).getText().trim())
                .filter(s -> !s.isEmpty())
                .collect(Collectors.toList());
    }

    public List<BigDecimal> prices() {
        return driver.findElements(productCards).stream()
                .map(card -> card.findElements(priceInCard))
                .filter(list -> !list.isEmpty())
                .map(list -> parsePrice(list.get(0).getText()))
                .filter(v -> v != null)
                .collect(Collectors.toList());
    }

    public boolean namesAscending() {
        List<String> n = names();
        for (int i = 0; i + 1 < n.size(); i++)
            if (n.get(i).compareToIgnoreCase(n.get(i + 1)) > 0) return false;
        return true;
    }

    public boolean pricesAscending() {
        List<BigDecimal> v = prices();
        for (int i = 0; i + 1 < v.size(); i++)
            if (v.get(i).compareTo(v.get(i + 1)) > 0) return false;
        return true;
    }

    public boolean hasAtLeast(int n) { return driver.findElements(productCards).size() >= n; }

    /* ---------- Compare actions ---------- */
    public CategoryPage addToCompareByIndex(int index) {
        List<WebElement> items = driver.findElements(productCards);
        if (index < 0 || index >= items.size()) throw new IllegalArgumentException("Bad index: " + index);

        WebElement tile = items.get(index);
        waits.scrollIntoView(tile);
        tile.findElement(compareBtn).click();
        waitSuccessBarCycle();
        return this;
    }

    public CategoryPage addFirstNToCompare(int n) {
        if (!hasAtLeast(n)) throw new IllegalStateException("Not enough products");
        for (int i = 0; i < n; i++) addToCompareByIndex(i);
        return this;
    }

    public pages.ComparePage openComparePageFromFooter() {
        try { waits.invisible(successBar); } catch (Exception ignored) {}
        waits.scrollIntoView(waits.visible(footerCompareLink));
        waits.click(footerCompareLink);
        return new pages.ComparePage(driver);
    }

    /* ---------- Helpers ---------- */
    private void waitSuccessBarCycle() {
        waits.visible(successBar);
        try { driver.findElement(successBarClose).click(); } catch (Exception ignored) {}
        waits.invisible(successBar);
    }

    private BigDecimal parsePrice(String raw) {
        if (raw == null) return null;
        String s = raw.replaceAll("[^\\d,\\.]", "").trim();
        if (s.contains(",") && s.contains(".")) {
            if (s.lastIndexOf(',') > s.lastIndexOf('.')) { s = s.replace(".", "").replace(",", "."); }
            else { s = s.replace(",", ""); }
        } else {
            s = s.replace(",", "");
        }
        if (s.isEmpty()) return null;
        try { return new BigDecimal(s); } catch (Exception e) { return null; }
    }
}
