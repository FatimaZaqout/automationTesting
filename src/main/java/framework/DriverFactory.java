package framework;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.time.Duration;
import java.util.Objects;
import java.util.Properties;

public class DriverFactory {

    private static final ThreadLocal<WebDriver> tlDriver = new ThreadLocal<>();
    private static Properties config;

    static {
        try {
            config = new Properties();
            config.load(Objects.requireNonNull(
                DriverFactory.class.getClassLoader().getResourceAsStream("config.properties")
            ));
        } catch (Exception e) {
            config = new Properties();
        }
    }

    public static WebDriver initDriver() {
        if (tlDriver.get() == null) {
            String browser = get("browser", "chrome");      // chrome | firefox | edge | safari
            boolean headless = Boolean.parseBoolean(get("headless", "false"));
            int implicit = Integer.parseInt(get("implicitSeconds", "0"));
            int pageLoad = Integer.parseInt(get("pageLoadSeconds", "30"));

            switch (browser.toLowerCase()) {
                case "firefox": {
                    WebDriverManager.firefoxdriver().setup();
                    FirefoxOptions fx = new FirefoxOptions();
                    if (headless) fx.addArguments("--headless");
                    tlDriver.set(new FirefoxDriver(fx));
                    break;
                }
                case "edge": {
                    WebDriverManager.edgedriver().setup();
                    EdgeOptions ed = new EdgeOptions();
                    if (headless) ed.addArguments("--headless=new");
                    tlDriver.set(new EdgeDriver(ed));
                    break;
                }
                case "safari": {
                    SafariOptions sf = new SafariOptions();
                    tlDriver.set(new SafariDriver(sf));
                    break;
                }
                case "chrome":
                default: {
                    // Enhanced Chrome options to reduce automation signals
                    WebDriverManager.chromedriver().setup();
                    ChromeOptions options = new ChromeOptions();
                    // If user explicitly requested headless, enable it; otherwise prefer visible browser.
                    if (headless) {
                        options.addArguments("--headless=new");
                    }
                    options.addArguments("--disable-blink-features=AutomationControlled");
                    options.addArguments("--disable-infobars");
                    options.addArguments("--start-maximized");
                    // Random-ish user-agent (rotate if running many sessions)
                    options.addArguments("user-agent=" + randomUserAgent());
                    // Remove enable-automation flag and extension
                    options.setExperimentalOption("excludeSwitches", java.util.Arrays.asList("enable-automation"));
                    options.setExperimentalOption("useAutomationExtension", false);
                    // Optional: set proxy via config (commented out by default)
                    // String proxy = get("proxy", "");
                    // if (!proxy.isEmpty()) { org.openqa.selenium.Proxy seleniumProxy = new org.openqa.selenium.Proxy(); seleniumProxy.setHttpProxy(proxy).setSslProxy(proxy); options.setProxy(seleniumProxy); }

                    tlDriver.set(new ChromeDriver(options));
                }
            }

            WebDriver driver = tlDriver.get();
            driver.manage().timeouts().pageLoadTimeout(Duration.ofSeconds(pageLoad));
            if (implicit > 0) {
                driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(implicit));
            }
            try {
                driver.manage().window().maximize();
            } catch (Exception ignored) {}
        }
        return tlDriver.get();
    }

    public static WebDriver getDriver() {
        return tlDriver.get();
    }

    public static void quitDriver() {
        WebDriver driver = tlDriver.get();
        if (driver != null) {
            try { driver.quit(); } finally { tlDriver.remove(); }
        }
    }

    private static String randomUserAgent() {
        String[] agents = new String[]{
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36",
            "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_7) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/15.1 Safari/605.1.15",
            "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:116.0) Gecko/20100101 Firefox/116.0",
            "Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/118.0.0.0 Safari/537.36"
        };
        int idx = (int)(Math.random() * agents.length);
        return agents[idx];
    }

    private static String get(String key, String defVal) {
        String sys = System.getProperty(key);
        if (sys != null && !sys.isEmpty()) return sys;
        String val = config.getProperty(key);
        return (val != null) ? val : defVal;
    }
}
