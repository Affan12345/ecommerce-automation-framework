# E-Commerce Test Automation Framework

![Java](https://img.shields.io/badge/Java-17-orange)
![Selenium](https://img.shields.io/badge/Selenium-4.21-43B02A)
![TestNG](https://img.shields.io/badge/TestNG-7.10-red)
![Maven](https://img.shields.io/badge/Build-Maven-blue)
![Pattern](https://img.shields.io/badge/Design-Page%20Object%20Model-lightgrey)

A web UI test automation framework written in **Java** with **Selenium WebDriver**, **TestNG** and **Maven**. It follows the **Page Object Model (POM)** design pattern and automates the main customer journey of the [Sauce Demo](https://www.saucedemo.com) e-commerce practice site: login, browsing products, managing the cart and completing a checkout.

---

## Table of Contents

- [About the Project](#about-the-project)
- [Tech Stack](#tech-stack)
- [Key Features](#key-features)
- [Framework Design](#framework-design)
- [Project Structure](#project-structure)
- [Test Scenarios](#test-scenarios)
- [Getting Started](#getting-started)
- [Running the Tests](#running-the-tests)
- [Reports and Screenshots](#reports-and-screenshots)
- [What I Learned](#what-i-learned)
- [Future Improvements](#future-improvements)
- [Author](#author)

---

## About the Project

I built this project to practise writing clean, maintainable UI automation from scratch, instead of putting every locator and action inside the test methods. The framework separates **what is tested** (test classes) from **how the page is operated** (page classes), so a change in the website needs a fix in one page class only, not in every test.

The site under test is [saucedemo.com](https://www.saucedemo.com), a public demo store made for automation practice. The test data used here (`standard_user` / `secret_sauce`) is the public demo login published on the site itself.

---

## Tech Stack

| Tool | Version | Purpose |
|---|---|---|
| Java | 17 | Programming language |
| Selenium WebDriver | 4.21.0 | Browser automation |
| TestNG | 7.10.2 | Test framework, assertions, suite execution |
| Maven | 3.x | Dependency and build management |
| Google Chrome | Latest | Browser under test |
| Eclipse IDE | 2026-12 | Development environment |

---

## Key Features

- **Page Object Model:** every page has its own class with locators and actions (`LoginPage`, `ProductsPage`, `CartPage`, `CheckoutPage`)
- **Reusable base class:** `BaseTest` opens and closes the browser for every test, so test classes stay short
- **Assertions with clear messages:** TestNG `Assert` checks with readable failure messages
- **Explicit waits:** `WebDriverWait` with `ExpectedConditions` in the checkout flow to handle page transitions
- **Screenshot on failure:** when a test fails, a timestamped screenshot is saved automatically
- **Suite-based execution:** all test classes run together from `testng.xml`
- **HTML reports:** TestNG reports from a passing run are included in `sample-reports`
- **Clean repository:** build output, screenshots and IDE files are excluded with `.gitignore`

---

## Framework Design

```
 Test class  ──uses──►  Page class  ──uses──►  WebDriver
 (what to verify)       (how to operate        (browser control)
                         the page)
        ▲
        └── extends BaseTest (browser setup and teardown)
```

### Layers

| Layer | Package | Responsibility |
|---|---|---|
| Base | `base` | `BaseTest` creates the `ChromeDriver`, maximizes the window, sets the wait, opens the site and quits the browser after each test |
| Pages | `pages` | Locators and user actions for each page. No assertions here |
| Tests | `tests` | Test scenarios and assertions. No locators here |
| Utilities | `utils` | `ScreenshotUtil` captures and saves screenshots |

### Example: a test reads like a user story

```java
@Test
public void completeOrderEndToEndTest() {
    new LoginPage(driver).login("standard_user", "secret_sauce");

    ProductsPage productsPage = new ProductsPage(driver);
    productsPage.addProductToCart("sauce-labs-backpack");
    productsPage.openCart();

    new CartPage(driver).clickCheckout();

    CheckoutPage checkoutPage = new CheckoutPage(driver);
    checkoutPage.fillInformation("Test", "User", "1207");
    checkoutPage.clickFinish();

    Assert.assertEquals(checkoutPage.getConfirmationMessage(),
            "Thank you for your order!");
}
```

### Screenshot on failure

`BaseTest` checks the result after each test. If it failed, it calls `ScreenshotUtil` before closing the browser:

```java
@AfterMethod
public void tearDown(ITestResult result) {
    if (result.getStatus() == ITestResult.FAILURE) {
        String path = ScreenshotUtil.capture(driver, result.getName());
        System.out.println("Screenshot saved: " + path);
    }
    if (driver != null) {
        driver.quit();
    }
}
```

---

## Project Structure

```
ecommerce-automation-framework
├── src/test/java
│   ├── base
│   │   └── BaseTest.java
│   ├── pages
│   │   ├── LoginPage.java
│   │   ├── ProductsPage.java
│   │   ├── CartPage.java
│   │   └── CheckoutPage.java
│   ├── tests
│   │   ├── LoginTest.java
│   │   ├── ProductsTest.java
│   │   └── CheckoutTest.java
│   └── utils
│       └── ScreenshotUtil.java
├── sample-reports
│   ├── TestNG Report.html
│   ├── Index Report.html
│   └── Index Report_files
├── pom.xml
├── testng.xml
├── .gitignore
└── README.md
```

---

## Test Scenarios

| # | Class | Test | What it verifies |
|---|---|---|---|
| 1 | `LoginTest` | `validLoginTest` | A valid user reaches the products page |
| 2 | `LoginTest` | `invalidLoginTest` | Wrong credentials show an error message |
| 3 | `ProductsTest` | `productsPageShowsSixProductsTest` | Page title is "Products" and six products are listed |
| 4 | `ProductsTest` | `addSingleProductTest` | Adding one product sets the cart badge to 1 |
| 5 | `ProductsTest` | `addMultipleProductsTest` | Adding two products sets the cart badge to 2 |
| 6 | `CheckoutTest` | `removeProductFromCartTest` | Removing one of two products leaves one in the cart |
| 7 | `CheckoutTest` | `completeOrderEndToEndTest` | Full flow from login to the order confirmation message |

**Total: 7 tests**

---

## Getting Started

### Prerequisites

- JDK 17 or above
- Maven 3.x
- Google Chrome (Selenium Manager downloads the matching driver automatically)
- Internet connection
- Git

Check your setup:

```
java -version
mvn -version
git --version
```

### Installation

```
git clone https://github.com/Affan12345/ecommerce-automation-framework.git
cd ecommerce-automation-framework
```

Then open the folder as a **Maven project** in Eclipse or IntelliJ IDEA and let it download the dependencies.

---

## Running the Tests

### From the IDE (Eclipse)

1. Right-click `testng.xml`
2. Choose **Run As → TestNG Suite**

To run a single class, right-click the class (for example `LoginTest.java`) and choose **Run As → TestNG Test**.

### From the command line

```
mvn test
```

> The suite in `testng.xml` lists the three test classes in the order `LoginTest`, `ProductsTest`, `CheckoutTest`.

### Expected result

```
Total tests run: 7, Passes: 7, Failures: 0, Skips: 0
```

---

## Reports and Screenshots

| Output | Location | Notes |
|---|---|---|
| TestNG HTML reports | `test-output/` | Created after every run, not committed to Git |
| Sample reports | `sample-reports/` | Reports from a passing run, kept for reference. Download the files and open them in a browser |
| Failure screenshots | `screenshots/` | Created only when a test fails, not committed to Git |

---

## What I Learned

- Structuring a framework with the **Page Object Model** and inheritance
- Writing locators with `By.id`, `By.className` and `By.cssSelector`
- Using **explicit waits** instead of relying only on fixed or implicit waits
- Investigating a test that sometimes fails by reading the **failure trace** and the **failure screenshot**
- Managing dependencies with **Maven** and running suites with **TestNG**
- Publishing a project professionally with **Git**, `.gitignore` and a README

---

## Future Improvements

- [ ] Data-driven login tests with TestNG `@DataProvider`
- [ ] Richer reporting with ExtentReports, with screenshots inside the report
- [ ] Cross-browser execution (Chrome and Firefox)
- [ ] Reading test data and the base URL from a config file
- [ ] Continuous integration with GitHub Actions

---

## Author

**Rafid Affan**

GitHub: [@Affan12345](https://github.com/Affan12345)

