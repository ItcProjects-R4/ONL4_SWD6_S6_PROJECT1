import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

public class Cart {
    WebDriver driver;
    WebDriverWait wait;

    @BeforeMethod
    public void setUp() {
        // to stop the annoying useless warnings fel browser
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
        driver.get("https://www.saucedemo.com/");

        WebElement userName = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("user-name")));
        WebElement password = wait.until(ExpectedConditions.presenceOfElementLocated(By.id("password")));

        userName.sendKeys("standard_user");
        password.sendKeys("secret_sauce");
        password.sendKeys(Keys.ENTER);
    }


    public void addBackpack() {
        WebElement backpackElement = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("add-to-cart-sauce-labs-backpack"))
        );
        backpackElement.click();
    }

    public void addBikeLight() {
        WebElement bikeElement = wait.until(
                ExpectedConditions.elementToBeClickable(By.id("add-to-cart-sauce-labs-bike-light"))
        );
        bikeElement.click();
    }

    public void verifyCartBadge(int expectedNumber) {
        WebElement cartBadge = wait.until(
                ExpectedConditions.presenceOfElementLocated(By.className("shopping_cart_badge"))
        );
        String badgeText = cartBadge.getText();
        Assert.assertEquals(badgeText, String.valueOf(expectedNumber), "Frontend badge did not update correctly!");
    }

    @Test
    public void testAddOneBackpack() {
        addBackpack();
        verifyCartBadge(1);
    }

    @Test
    public void testAddOneBikeLight() {
        addBikeLight();
        verifyCartBadge(1);
    }

    @Test
    public void testAddBikeAndBag() {
        addBackpack();
        addBikeLight();
        verifyCartBadge(2);
    }


    @AfterMethod
    public void tearDown() {
        if (driver != null) {
            // reset, log out, close tab
            WebElement topleft = wait.until(
                    ExpectedConditions.elementToBeClickable(By.id("react-burger-menu-btn"))
            );
            topleft.click();

            WebElement resetButton = wait.until(
                    ExpectedConditions.elementToBeClickable(By.id("reset_sidebar_link"))
            );
            resetButton.click();

            WebElement logout = wait.until(
                    ExpectedConditions.elementToBeClickable(By.id("logout_sidebar_link"))
            );
            logout.click();

            driver.quit();
        }
    }
}