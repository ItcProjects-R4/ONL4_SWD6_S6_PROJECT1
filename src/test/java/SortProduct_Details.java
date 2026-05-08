import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import org.openqa.selenium.support.ui.Select;
import io.github.bonigarcia.wdm.WebDriverManager;

import java.util.HashMap;
import java.util.Map;

public class SortProduct_Details {

    WebDriver d;

    @BeforeTest
    public void preRequisites() {
        WebDriverManager.chromedriver().setup();
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--incognito");
        options.addArguments("--disable-features=PasswordLeakDetection");

        Map<String, Object> prefs = new HashMap<String, Object>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);

        options.setExperimentalOption("prefs", prefs);
        options.setExperimentalOption("excludeSwitches", new String[]{"enable-automation"});

        d = new ChromeDriver(options);
        d.manage().window().maximize();
        d.get("https://www.saucedemo.com/");
    }

    @Test(priority = 1)
    public void valid_login() throws InterruptedException {
        d.findElement(By.id("user-name")).sendKeys("standard_user");
        d.findElement(By.id("password")).sendKeys("secret_sauce");
        d.findElement(By.id("login-button")).click();
        Thread.sleep(2000);
    }

    @Test(priority = 2)
    public void VerifyDefaultSorting() {
        WebElement dropdown = d.findElement(By.className("product_sort_container"));
        Select s = new Select(dropdown);
        String selectedOption = s.getFirstSelectedOption().getText();
        Assert.assertEquals(selectedOption, "Name (A to Z)");
    }

    @Test(priority = 3)
    public void VerifyProductDetailsAndUI() throws InterruptedException {
        String expectedName = d.findElement(By.id("item_4_title_link")).getText();
        String expectedPrice = d.findElements(By.className("inventory_item_price")).get(0).getText();

        d.findElement(By.id("item_4_title_link")).click();
        Thread.sleep(2000);

        String actualName = d.findElement(By.className("inventory_details_name")).getText();
        String actualPrice = d.findElement(By.className("inventory_details_price")).getText();
        WebElement desc = d.findElement(By.className("inventory_details_desc"));
        WebElement img = d.findElement(By.className("inventory_details_img"));
        WebElement addBtn = d.findElement(By.id("add-to-cart"));

        Assert.assertEquals(actualName, expectedName);
        Assert.assertEquals(actualPrice, expectedPrice);
        Assert.assertTrue(desc.isDisplayed());
        Assert.assertTrue(img.isDisplayed());
        Assert.assertTrue(addBtn.isDisplayed());
    }

    @Test(priority = 4)
    public void VerifyCartAndRemoveButton() throws InterruptedException {
        d.findElement(By.id("add-to-cart")).click();
        Thread.sleep(1000);

        String cartCount = d.findElement(By.className("shopping_cart_badge")).getText();
        Assert.assertEquals(cartCount, "1");

        WebElement removeBtn = d.findElement(By.id("remove"));
        Assert.assertTrue(removeBtn.isDisplayed());
    }

    @Test(priority = 5)
    public void VerifyBackToProductsNavigation() throws InterruptedException {
        d.findElement(By.id("back-to-products")).click();
        Thread.sleep(2000);
        Assert.assertTrue(d.getCurrentUrl().contains("inventory.html"));
    }

    @Test(priority = 6)
    public void VerifySortNameZA() throws InterruptedException {
        Select s = new Select(d.findElement(By.className("product_sort_container")));
        s.selectByVisibleText("Name (Z to A)");
        Thread.sleep(2000);
        String firstItem = d.findElement(By.className("inventory_item_name")).getText();
        Assert.assertEquals(firstItem, "Test.allTheThings() T-Shirt (Red)");
    }

    @Test(priority = 7)
    public void VerifySortPriceLowHigh() throws InterruptedException {
        Select s = new Select(d.findElement(By.className("product_sort_container")));
        s.selectByVisibleText("Price (low to high)");
        Thread.sleep(2000);
        String lowPrice = d.findElement(By.className("inventory_item_price")).getText();
        Assert.assertEquals(lowPrice, "$7.99");
    }

    @Test(priority = 8)
    public void ChooseMultipleItems() throws InterruptedException {
        d.findElement(By.id("add-to-cart-sauce-labs-bike-light")).click();
        Thread.sleep(1000);
        d.findElement(By.className("shopping_cart_link")).click();
        Thread.sleep(2000);
    }

    @Test(priority = 9)
    public void CheckoutProcess() throws InterruptedException {
        d.findElement(By.id("checkout")).click();
        Thread.sleep(1000);
        d.findElement(By.id("first-name")).sendKeys("joudy");
        d.findElement(By.id("last-name")).sendKeys("ahmed");
        d.findElement(By.id("postal-code")).sendKeys("2286");
        d.findElement(By.id("continue")).click();
        Thread.sleep(2000);
        d.findElement(By.id("finish")).click();
        Thread.sleep(2000);
    }

    @Test(priority = 10)
    public void ReturnHome() throws InterruptedException {
        d.findElement(By.id("back-to-products")).click();
        Thread.sleep(2000);
    }


    @AfterTest
    public void postRequisites() {
        if (d != null) {
            d.quit();
        }
    }
}