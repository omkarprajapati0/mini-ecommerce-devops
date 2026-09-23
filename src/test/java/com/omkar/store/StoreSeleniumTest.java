package com.omkar.store;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class StoreSeleniumTest {

    @LocalServerPort
    private int port;

    @Test
    void customerCanPlaceOrder() {
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless=new");

        WebDriver driver = new ChromeDriver(options);

        try {
            driver.get("http://localhost:" + port);

            driver.findElement(By.id("add-headphones")).click();

            assertEquals(
                "₹1,499",
                driver.findElement(By.id("cart-total")).getText()
            );

            driver.findElement(By.id("customer-name")).sendKeys("Omkar");
            driver.findElement(By.id("place-order")).click();

            WebDriverWait wait =
                new WebDriverWait(driver, Duration.ofSeconds(10));

            String message = wait.until(d ->
                d.findElement(By.id("message")).getText()
            );

            assertTrue(message.contains(
                "Order placed successfully for Omkar!"
            ));
        } finally {
            driver.quit();
        }
    }
}