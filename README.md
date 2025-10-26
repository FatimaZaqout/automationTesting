# 🧪 NopCommerce UI Automation — Selenium + TestNG (POM)

Automated UI testing framework for the nopCommerce demo store:  
https://demo.nopcommerce.com

This project applies **Page Object Model (POM)** with **Selenium WebDriver** and **TestNG**, plus optional **Allure** reporting and local **screenshots** on every test phase.

---

## ✅ Why this project

- **Stable & Maintainable**: Clean POM layers (`pages/`, `framework/`, `tests/`) to keep test logic separate from UI locators/actions.
- **Deterministic waits**: Explicit waits + utilities to reduce flaky behavior (scroll into view, toast handling).
- **Actionable reporting**: Allure attachments + saved screenshots to diagnose failures quickly.

> Repo highlights visible in GitHub: `src/`, `pom.xml`, `testng.xml`, `allure-results/`, `allure-report/`, `screenshots/`.  
> (See repo tree on the `main` branch.)  
> _Refs_: folders listed on the repository landing page.  
> 
> - `allure-report/`, `allure-results/` present in the tree.  
> - `screenshots/` is also present.  
> - `pom.xml`, `testng.xml` are at the repo root. :contentReference[oaicite:1]{index=1}

---

## 🧱 Tech Stack

- **Language**: Java (Test code)  
- **Automation**: Selenium WebDriver  
- **Runner**: TestNG  
- **Build**: Maven  
- **Reports**: Allure (optional)  
- **Artifacts**: Local screenshots per test (before/after)

---

## 📂 Project Structure

automationTesting/
├─ src/
│ └─ test/
│ ├─ java/
│ │ ├─ framework/ # DriverFactory, BaseTest, Waits...
│ │ ├─ pages/ # Page Objects (HomePage, CategoryPage, ComparePage, ...)
│ │ └─ tests/ # Test classes
│ └─ resources/
├─ screenshots/ # Saved PNGs captured before/after tests
├─ allure-results/ # Raw Allure results
├─ allure-report/ # Generated Allure report (static)
├─ pom.xml # Maven config
└─ testng.xml # Test suite config


> Notes: `allure-results/`, `allure-report/`, `screenshots/`, `pom.xml`, `testng.xml` are visible in the repository tree. :contentReference[oaicite:2]{index=2}

---

## ⚙️ Configuration

You can override defaults at runtime using JVM system properties:

| Property            | Purpose                               | Default                                   |
|---------------------|----------------------------------------|-------------------------------------------|
| `baseUrl`           | AUT base URL                           | `https://demo.nopcommerce.com/`           |
| `browser`           | `chrome` / `firefox` / `edge` / `safari` | `chrome`                                  |
| `headless`          | Run headless                           | `false`                                   |
| `implicitSeconds`   | Optional implicit wait (seconds)       | `0`                                       |
| `pageLoadSeconds`   | Page load timeout (seconds)            | `30`                                      |


## ⚙️ Configuration

You can override defaults at runtime using JVM system properties:

| Property            | Purpose                               | Default                                   |
|---------------------|----------------------------------------|-------------------------------------------|
| `baseUrl`           | AUT base URL                           | `https://demo.nopcommerce.com/`           |
| `browser`           | `chrome` / `firefox` / `edge` / `safari` | `chrome`                                  |
| `headless`          | Run headless                           | `false`                                   |
| `implicitSeconds`   | Optional implicit wait (seconds)       | `0`                                       |
| `pageLoadSeconds`   | Page load timeout (seconds)            | `30`                                      |


## 🧩 Design Notes 

POM-first: Each page exposes clear, reusable actions (e.g., goToCategory, addFirstNToCompare, openComparePageFromFooter).
Stability: scrollIntoView + explicit waits before interactions; handle success notifications (appear/close/disappear) to avoid overlays.
Diagnostics: Before/After screenshots + Allure text/image attachments on failures.
Parallel-friendly: WebDriver managed via DriverFactory with per-test lifecycle in BaseTest.

Author
Eman Naji - QA / Test Automation
Fatima Zaqout — QA / Test Automation
Sohaib Al Shawwa - QA / Test Automation
