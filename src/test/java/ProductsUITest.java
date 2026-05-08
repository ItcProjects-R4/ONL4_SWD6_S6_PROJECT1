import com.aventstack.extentreports.*;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.*;
import org.testng.Assert;
import org.testng.annotations.*;

import java.time.Duration;
import java.util.List;

// ─────────────────────────────────────────────────────────────
// Products Page — Page Object Model
// ─────────────────────────────────────────────────────────────
class ProductsPage {
    private WebDriver driver;
    private WebDriverWait wait;

    // Locators
    private By pageTitle     = By.cssSelector(".title");
    private By productNames  = By.cssSelector(".inventory_item_name");
    private By productPrices = By.cssSelector(".inventory_item_price");
    private By addToCartBtns = By.cssSelector("button[data-test^='add-to-cart']");
    private By removeBtns    = By.cssSelector("button[data-test^='remove']");
    private By cartBadge     = By.cssSelector(".shopping_cart_badge");
    private By sortDropdown  = By.cssSelector("select.product_sort_container");

    public ProductsPage(WebDriver driver) {
        this.driver = driver;
        this.wait   = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    // Wait until element is visible and return it
    private WebElement find(By by) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    // Wait until all elements are present and return the list
    private List<WebElement> findAll(By by) {
        return wait.until(ExpectedConditions.presenceOfAllElementsLocatedBy(by));
    }

    // ── Page Actions ──────────────────────────────────────────

    // Check if the current page title is "Products"
    public boolean isOnProductsPage() {
        return find(pageTitle).getText().equalsIgnoreCase("Products");
    }

    // Click the "Add to Cart" button for the first product
    public void addFirstProductToCart() {
        findAll(addToCartBtns).get(0).click();
    }

    // Check if the "Remove" button is visible (i.e., item was added to cart)
    public boolean isRemoveBtnVisible() {
        return !findAll(removeBtns).isEmpty();
    }

    // Return the number displayed on the cart badge icon
    public int getCartBadgeCount() {
        try { return Integer.parseInt(find(cartBadge).getText()); }
        catch (Exception e) { return 0; }
    }

    // Select a sort option from the dropdown by visible text
    public void sortBy(String text) {
        new Select(find(sortDropdown)).selectByVisibleText(text);
    }

    // Return all product prices as a list of doubles
    public List<Double> getProductPricesList() {
        return findAll(productPrices).stream()
                .map(e -> Double.parseDouble(e.getText().replace("$", "")))
                .toList();
    }

    // Return all product names as a list of strings
    public List<String> getProductNamesList() {
        return findAll(productNames).stream().map(WebElement::getText).toList();
    }
}

// ─────────────────────────────────────────────────────────────
// Products UI Test Class — 3 Test Cases + ExtentReports
// ─────────────────────────────────────────────────────────────
public class ProductsUITest {

    WebDriver driver;
    ProductsPage productsPage;

    // ExtentReports objects for report generation
    static ExtentReports extent;
    static ExtentTest test;

    // ══════════════════════════════════════════════════════════
    // @BeforeTest — Configure the report, open browser, and login
    // ══════════════════════════════════════════════════════════
    @BeforeTest
    public void setup() {

        // Configure the HTML report using ExtentSparkReporter
        ExtentSparkReporter spark = new ExtentSparkReporter("reports/ProductsUI_Report.html");
        spark.config().setTheme(Theme.DARK);
        spark.config().setDocumentTitle("Sauce Demo — Products & UI Test Report");
        spark.config().setReportName("Products Page Automation Report");
        spark.config().setTimeStampFormat("dd-MM-yyyy HH:mm:ss");

        // Attach the reporter and set system information
        extent = new ExtentReports();
        extent.attachReporter(spark);
        extent.setSystemInfo("Tester",      "QA Engineer");
        extent.setSystemInfo("Application", "Sauce Demo");
        extent.setSystemInfo("URL",         "https://www.saucedemo.com");
        extent.setSystemInfo("Browser",     "Chrome");
        extent.setSystemInfo("Environment", "Staging");

        // Launch browser and navigate to the application
        driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        driver.get("https://www.saucedemo.com/");

        // Perform login with valid credentials
        driver.findElement(By.id("user-name")).sendKeys("standard_user");
        driver.findElement(By.id("password")).sendKeys("secret_sauce");
        driver.findElement(By.id("login-button")).click();

        // Initialize the Products page object
        productsPage = new ProductsPage(driver);
    }

    // ══════════════════════════════════════════════════════════
    // TC_01 — Verify Products Page loads after login
    // ══════════════════════════════════════════════════════════
    @Test(priority = 1, description = "TC_01: Verify user is redirected to Products page after login")
    public void TC01_VerifyProductsPageLoaded() {

        // Create a new test entry in the report
        test = extent.createTest(
                "TC_01 — Products Page Loads After Login",
                "After successful login, user should be redirected to the Products page"
        );

        try {
            boolean onPage = productsPage.isOnProductsPage();

            // Assert that the page title equals "Products"
            Assert.assertTrue(onPage, "User should be on the Products page");
            test.pass("✅ Products page loaded successfully — title is 'Products'");

        } catch (AssertionError e) {
            test.fail("❌ Products page did NOT load — " + e.getMessage());
            throw e;
        }
    }

    // ══════════════════════════════════════════════════════════
    // TC_02 — Verify Add to Cart updates button + cart badge
    // ══════════════════════════════════════════════════════════
    @Test(priority = 2, description = "TC_02: Verify Add to Cart changes button to Remove and updates badge")
    public void TC02_VerifyAddToCartFunctionality() {

        // Create a new test entry in the report
        test = extent.createTest(
                "TC_02 — Add to Cart Updates Button & Badge",
                "Clicking 'Add to Cart' should change button to 'Remove' and show badge count = 1"
        );

        try {
            // Click Add to Cart for the first product
            productsPage.addFirstProductToCart();
            test.info("🛒 Clicked 'Add to Cart' on the first product");

            // Check 1: "Add to Cart" button should now display "Remove"
            boolean removeVisible = productsPage.isRemoveBtnVisible();
            Assert.assertTrue(removeVisible, "'Add to Cart' should change to 'Remove'");
            test.pass("✅ Button changed to 'Remove' successfully");

            // Check 2: Cart badge should display the number 1
            int badgeCount = productsPage.getCartBadgeCount();
            Assert.assertEquals(badgeCount, 1, "Cart badge should show 1");
            test.pass("✅ Cart badge updated correctly — shows: " + badgeCount);

        } catch (AssertionError e) {
            test.fail("❌ Add to Cart test failed — " + e.getMessage());
            throw e;
        }
    }

    // ══════════════════════════════════════════════════════════
    // TC_03 — Verify Sorting by Price (Low to High)
    // ══════════════════════════════════════════════════════════
    @Test(priority = 3, description = "TC_03: Verify sorting products by Price Low to High works correctly")
    public void TC03_VerifySortByPriceLowToHigh() {

        // Create a new test entry in the report
        test = extent.createTest(
                "TC_03 — Sort Products by Price (Low to High)",
                "Selecting 'Price (low to high)' from the sort dropdown should reorder products ascending"
        );

        try {
            // Select "Price (low to high)" from the sort dropdown
            productsPage.sortBy("Price (low to high)");
            test.info("🔽 Selected sort option: Price (low to high)");

            // Retrieve the current list of prices after sorting
            List<Double> prices = productsPage.getProductPricesList();
            test.info("📋 Prices after sorting: " + prices);

            // Verify each price is less than or equal to the next one
            boolean isSorted = true;
            for (int i = 0; i < prices.size() - 1; i++) {
                if (prices.get(i) > prices.get(i + 1)) {
                    isSorted = false;
                    test.fail("❌ Price order broken at index " + i +
                            ": " + prices.get(i) + " > " + prices.get(i + 1));
                    break;
                }
            }

            Assert.assertTrue(isSorted, "Products should be sorted by price ascending");
            test.pass("✅ Products are correctly sorted from lowest to highest price");

        } catch (AssertionError e) {
            test.fail("❌ Sorting test failed — " + e.getMessage());
            throw e;
        }
    }

    // ══════════════════════════════════════════════════════════
    // @AfterTest — Close the browser and generate the HTML report
    // ══════════════════════════════════════════════════════════
    @AfterTest
    public void teardown() {
        driver.quit();

        // Flush all test data and write the final HTML report to disk
        extent.flush();
        System.out.println("✅ Report generated at: reports/ProductsUI_Report.html");
    }
}
