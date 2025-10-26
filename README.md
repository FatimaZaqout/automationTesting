# 🧪 NopCommerce Automation Testing Project

This project represents a complete **UI Automation Testing Framework** built for the e-commerce demo website  
🔗 https://demo.nopcommerce.com  

The project applies **Selenium WebDriver + TestNG + Page Object Model (POM)** design pattern to ensure:
- Readability
- Reusability
- Maintainability
- Scalability for future test scenarios

---

## 🚀 Key Features
| Feature | Description |
|--------|-------------|
| **Page Object Model** | Every web page has a dedicated class containing its locators & actions |
| **TestNG** | Handles test execution flow, assertions, and reporting |
| **Explicit Waits** | Ensures reliable synchronization, reducing flaky test behavior |
| **Reusable Utilities** | `DriverFactory`, `BaseTest`, and `Waits` provide core testing functionality |
| **Allure Reporting (Optional)** | Attaches screenshots & logs for failed test analysis |
| **Clean Architecture** | Clear separation between test logic and UI interaction logic |

---

## 📂 Project Structure

automationTesting/
├── src/test/java/
│ ├── framework/ # Driver setup, BaseTest, Wait helpers
│ ├── pages/ # POM classes (HomePage, CategoryPage, ComparePage ...)
│ ├── tests/ # Test classes grouped by functionality
│
└── resources/
└── testng.xml # Test execution configuration


## 📝 Covered Test Scenarios (Samples)

| Test Case | Description | Status |
|----------|-------------|--------|
| Change Currency | Verify switching to Euro updates prices | ✅ |
| Search Functionality | Search returns relevant products | ✅ |
| Add to Wishlist | Add a product to wishlist and verify it appears | ✅ |
| Compare Products | Add multiple products, open compare page, assert UI grid | ✅ |
| Remove from Compare | Ensure removing a product updates the grid correctly | ✅ |

---

## 🧱 Technologies Used
- **Java +**
- **Selenium WebDriver**
- **TestNG**
- **Maven**
- **Allure Reports** **

---

## ▶️ How to Run Tests

Using Maven:
```bash
mvn clean test

With Allure Report:

mvn clean test
allure serve allure-results



