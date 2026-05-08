import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class ProductCartIntegration {
    WebDriver driver;
    WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.password_manager_leak_detection", false);
        options.setExperimentalOption("prefs", prefs);
        options.setAcceptInsecureCerts(true);

        driver = new ChromeDriver(options);
        driver.manage().window().maximize();
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));

        // Standard Login
        driver.get("https://www.saucedemo.com/");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("user-name"))).sendKeys("standard_user");
        wait.until(ExpectedConditions.presenceOfElementLocated(By.id("password"))).sendKeys("secret_sauce", Keys.ENTER);
    }

    // ==========================================
    // HELPER METHODS (Products + Cart Domains)
    // ==========================================

    public void sortProducts(String visibleText) {
        WebElement sortDropdown = wait.until(ExpectedConditions.elementToBeClickable(By.className("product_sort_container")));
        Select select = new Select(sortDropdown);
        select.selectByVisibleText(visibleText);
    }

    public void verifyCartBadge(String expectedNumber) {
        WebElement cartBadge = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("shopping_cart_badge")));
        Assert.assertEquals(cartBadge.getText(), expectedNumber, "Cart badge number is incorrect!");
    }

    // ==========================================
    // INTEGRATION TESTS
    // ==========================================

    @Test
    public void testSortAndAddToCartIntegration() {
        // 1. Action: Sort the products by Price (Low to High)
        sortProducts("Price (low to high)");

        // 2. Action: Grab the name of whatever the FIRST item is now, and add it to the cart
        WebElement firstItemNameElement = wait.until(ExpectedConditions.presenceOfElementLocated(By.cssSelector(".inventory_item:nth-child(1) .inventory_item_name")));
        String expectedItemName = firstItemNameElement.getText(); // Should be "Sauce Labs Onesie"

        WebElement firstItemAddToCartBtn = wait.until(ExpectedConditions.elementToBeClickable(By.cssSelector(".inventory_item:nth-child(1) button")));
        firstItemAddToCartBtn.click();

        // 3. Integration Check: Go to the actual cart page
        WebElement cartLink = wait.until(ExpectedConditions.elementToBeClickable(By.className("shopping_cart_link")));
        cartLink.click();

        // 4. Assert: Is the item in the cart the exact same one we clicked after sorting?
        WebElement itemInCartName = wait.until(ExpectedConditions.presenceOfElementLocated(By.className("inventory_item_name")));
        Assert.assertEquals(itemInCartName.getText(), expectedItemName, "The sorted item added does not match the item in the cart!");
    }

    @Test
    public void testCartRemovalUpdatesInventoryState() {
        // 1. Action: Add the Bike Light from the Inventory page
        WebElement bikeAddToCartBtn = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("add-to-cart-sauce-labs-bike-light"))
        );
        bikeAddToCartBtn.click();

        // 2. Integration Check: Go to the Cart page
        WebElement cartLink = wait.until(
                ExpectedConditions.elementToBeClickable(By.className("shopping_cart_link"))
        );
        cartLink.click();

        // 3. Action: Remove the item from INSIDE the cart
        WebElement cartRemoveBtn = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("remove-sauce-labs-bike-light"))
        );
        cartRemoveBtn.click();

        // 4. Integration Check: Click 'Continue Shopping' to go BACK to Inventory
        WebElement continueShoppingBtn = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("continue-shopping"))
        );
        continueShoppingBtn.click();

        // 5. The Judge: Is the button on the main page green again?
        // If the ID "add-to-cart-sauce-labs-bike-light" exists, it means the state successfully reset.
        // If the state was broken, it would still have the ID "remove-sauce-labs-bike-light".
        WebElement bikeButtonResetState = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.id("add-to-cart-sauce-labs-bike-light"))
        );

        Assert.assertTrue(bikeButtonResetState.isDisplayed(),
                "Integration Failure: Inventory page button did not reset after item was removed from the cart!");
    }

    @Test
    public void testProductDetailsToCartStateIntegration() {
        // 1. Action: Click into a product's specific details page
        WebElement backpackTitle = wait.until(ExpectedConditions.elementToBeClickable(By.id("item_4_title_link")));
        backpackTitle.click();

        // 2. Action: Add to cart from INSIDE the details page
        WebElement addToCartBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("add-to-cart")));
        addToCartBtn.click();

        // 3. Assert: Badge updates inside the details page
        verifyCartBadge("1");

        // 4. Integration Check: Navigate BACK to the main inventory page
        WebElement backToProductsBtn = wait.until(ExpectedConditions.elementToBeClickable(By.id("back-to-products")));
        backToProductsBtn.click();

        // 5. Assert: The cart badge state successfully carried over to the main page
        verifyCartBadge("1");
    }

    // ==========================================
    // TEARDOWN
    // ==========================================

    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            // Clean app state so the next test starts fresh
            WebElement topleft = wait.until(ExpectedConditions.elementToBeClickable(By.id("react-burger-menu-btn")));
            topleft.click();

            WebElement resetButton = wait.until(ExpectedConditions.elementToBeClickable(By.id("reset_sidebar_link")));
            resetButton.click();

            WebElement logout = wait.until(ExpectedConditions.elementToBeClickable(By.id("logout_sidebar_link")));
            logout.click();

            driver.quit();
        }
    }
}