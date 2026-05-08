import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.Sleeper;
import org.openqa.selenium.support.ui.Wait;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.Map;

public class EndtoEnd {
    WebDriver d;

    @BeforeTest
    public void pre() throws InterruptedException {

        //*-----------------------------------------------AI work place---------------------------------------------*//
        ChromeOptions options = new ChromeOptions();
        Map<String, Object> prefs = new HashMap<>();
        prefs.put("credentials_enable_service", false);
        prefs.put("profile.password_manager_enabled", false);
        prefs.put("profile.password_manager_leak_detection", false);
        options.setExperimentalOption("prefs", prefs);
        options.addArguments("--disable-features=PasswordLeakDetection");
        d = new ChromeDriver(options);
        //*---------------------------------------------------------------------------------------------------------*//
        d.navigate().to("https://www.saucedemo.com/");
        WebElement e1 = d.findElement(By.id("user-name"));
        WebElement e2 = d.findElement(By.id("password"));
        WebElement e3 = d.findElement(By.id("login-button"));
        e1.sendKeys("standard_user");
        Thread.sleep(2000);
        e2.sendKeys("secret_sauce");
        Thread.sleep(2000);
        e3.click();
        Thread.sleep(2000);
    }
    @Test()
    public void End2End() throws InterruptedException {
        WebElement e1 = d.findElement(By.id("add-to-cart-sauce-labs-backpack"));
        e1.click();
        Thread.sleep(2000);
        WebElement e2 = d.findElement(By.className("shopping_cart_link"));
        e2.click();
        Thread.sleep(2000);
        WebElement e3 = d.findElement(By.id("checkout"));
        e3.click();
        Thread.sleep(2000);
        WebElement fname_filed = d.findElement(By.id("first-name"));
        WebElement lname_filed = d.findElement(By.id("last-name"));
        WebElement post_code = d.findElement(By.id("postal-code"));
        WebElement buttom = d.findElement(By.id("continue"));
        fname_filed.sendKeys("Hosam");
        Thread.sleep(2000);
        lname_filed.sendKeys("diab");
        Thread.sleep(2000);
        post_code.sendKeys("30230");
        Thread.sleep(2000);
        buttom.click();
        Thread.sleep(2000);
        WebElement finsh =d.findElement(By.id("finish"));
        finsh.click();
        Thread.sleep(2000);
        WebElement back=d.findElement(By.id("back-to-products"));
        back.click();
        Thread.sleep(2000);
        d.close();
    }

}