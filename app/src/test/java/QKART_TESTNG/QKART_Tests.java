package QKART_TESTNG;

import QKART_TESTNG.pages.Checkout;
import QKART_TESTNG.pages.Home;
import QKART_TESTNG.pages.Login;
import QKART_TESTNG.pages.Register;
import QKART_TESTNG.pages.SearchResult;

import static org.testng.Assert.*;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.BrowserType;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import org.testng.annotations.Test;

public class QKART_Tests {

    static RemoteWebDriver driver;
    public static String lastGeneratedUserName;

     @BeforeSuite
    public static void createDriver() throws MalformedURLException {
        // Launch Browser using Zalenium
        final DesiredCapabilities capabilities = new DesiredCapabilities();
        capabilities.setBrowserName(BrowserType.CHROME);
        driver = new RemoteWebDriver(new URL("http://localhost:8082/wd/hub"), capabilities);
        System.out.println("createDriver()");
    }

    /*
     * Testcase01: Verify a new user can successfully register
     */
         @Test
        public void TestCase01() throws InterruptedException {
        Boolean status;
        //  logStatus("Start TestCase", "Test Case 1: Verify User Registration", "DONE");
        //  takeScreenshot(driver, "StartTestCase", "TestCase1");

        // Visit the Registration page and register a new user
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
         status = registration.registerUser("testUser", "abc@123", true);
        assertTrue(status, "Failed to register new user");

        // Save the last generated username
        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Visit the login page and login with the previuosly registered user
        Login login = new Login(driver);
        login.navigateToLoginPage();
         status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        //  logStatus("Test Step", "User Perform Login: ", status ? "PASS" : "FAIL");
        assertTrue(status, "Failed to login with registered user");

        // Visit the home page and log out the logged in user
        Home home = new Home(driver);
        status = home.PerformLogout();

        //  logStatus("End TestCase", "Test Case 1: Verify user Registration : ", status
        //  ? "PASS" : "FAIL");
        //  takeScreenshot(driver, "EndTestCase", "TestCase1");
    }
    @Test
    public void TestCase02() throws InterruptedException {
        Boolean status;
        // logStatus("Start TestCase", "Test Case 2: Verify User Registration", "DONE");
       //  takeScreenshot(driver, "StartTestCase", "TestCase2");

       // Visit the Registration page and register a new user
       Register registration = new Register(driver);
       registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
       assertTrue(status, "Failed to register new user");

       // Save the last generated username
       lastGeneratedUserName = registration.lastGeneratedUsername;

       // Visit the login page and login with the previuosly registered user
       Login login = new Login(driver);
       login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        // logStatus("Test Step", "User Perform Login: ", status ? "PASS" : "FAIL");
       assertTrue(status, "Failed to login with registered user");

       // Visit again Registration page and register with same user
    //    Register registration = new Register(driver);
       registration.navigateToRegisterPage();
        status = registration.registerUser(lastGeneratedUserName, "abc@123", false);
       assertFalse(status, "Successfully register with same ");

       // Visit the home page and log out the logged in user
       Home home = new Home(driver);
       status = home.PerformLogout();


        // logStatus("End TestCase", "Test Case 2: Verify user Registration : ", status
        // ? "PASS" : "FAIL");
        // takeScreenshot(driver, "EndTestCase", "TestCase2");
    }

    /*
     * Testcase03: Verify a new user can successfully register
     */
    @Test
    public void TestCase03() throws InterruptedException {
        // logStatus("TestCase 3", "Start test case : Verify functionality of search box ", "DONE");
        boolean status;
         // Visit the home page
         Home homePage = new Home(driver);
         homePage.navigateToHome();
         status=homePage.searchForProduct("YONEX");
         assertTrue(status, "Unable to search for given product");
         
         // Fetch the search results
        List<WebElement> searchResults = homePage.getSearchResults();
        for (WebElement webElement : searchResults) {
            // Create a SearchResult object from the parent element
            SearchResult resultelement = new SearchResult(webElement);

            // Verify that all results contain the searched text
            String elementText = resultelement.getTitleofResult();
            assertEquals(elementText, "YONEX Smash Badminton Racquet");
        }

        status = homePage.searchForProduct("Gesundheit");
        assertFalse(status, "Unable to search for given product");

    }

    /*
     * Testcase04: Verify a new user can successfully register
     */
    @Test
    public void TestCase04() throws InterruptedException {
        // logStatus("TestCase 4", "Start test case : Verify the presence of size Chart", "DONE");
        boolean status = false;

        // Visit home page
        Home homePage = new Home(driver);
        homePage.navigateToHome();
        
        // Search for product and get card content element of search results
        status = homePage.searchForProduct("Running Shoes");
        List<WebElement> searchResults = homePage.getSearchResults();
        // Create expected values
        List<String> expectedTHeaders = Arrays.asList("Size", "UK/INDIA", "EU", "HEEL TO TOE");
        List<List<String>> expectedTBody = Arrays.asList(Arrays.asList("6", "6", "40", "9.8"),
                Arrays.asList("7", "7", "41", "10.2"), Arrays.asList("8", "8", "42", "10.6"),
                Arrays.asList("9", "9", "43", "11"), Arrays.asList("10", "10", "44", "11.5"),
                Arrays.asList("11", "11", "45", "12.2"), Arrays.asList("12", "12", "46", "12.6"));
        // Verify size chart presence and content matching for each search result
        for (WebElement webElement : searchResults) {
            SearchResult result = new SearchResult(webElement);

            // Verify if the size chart exists for the search result
            if (result.verifySizeChartExists()) {
                // Verify if size dropdown exists
                status = result.verifyExistenceofSizeDropdown(driver);
                assertTrue(status,"Failed to validate presence of dropdown !");

                // Open the size chart
                if (result.openSizechart()) {
                    // Verify if the size chart contents matches the expected values
                    if (result.validateSizeChartContents(expectedTHeaders, expectedTBody, driver)) {
                    } else {
                        status = false;
                    }
                    assertTrue(status,"Failure while validating contents of size chart link");

                    // Close the size chart modal
                    status = result.closeSizeChart(driver);

                } else {
                    assertTrue(status,"Test Case Fail. Failure to open Size Chart");
                }

            } else {
                assertTrue(status,"Test Case Fail. Size Chart Link does not exist");
            }
        }
        assertTrue(status,"End Test Case: Validation of Size Chart Details failed !");
    }

    /*
     * Testcase05 :Verify the complete flow of checking out and placing order for products is
     * working correctly
     */
    @Test
    public void TestCase05() throws InterruptedException {
        Boolean status;
        logStatus("Start TestCase", "Test Case 5: Verify Happy Flow of buying products", "DONE");
        // Go to the Register page
        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
         status = registration.registerUser("testUser", "abc@123", true);
        assertTrue(status, "Failed to register new user");

        // Save the last generated username
        lastGeneratedUserName = registration.lastGeneratedUsername;

        // Visit the login page and login with the previuosly registered user
        Login login = new Login(driver);
        login.navigateToLoginPage();
         status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        assertTrue(status, "Failed to login with registered user");

        // Go to the home page
        Home homePage = new Home(driver);
        homePage.navigateToHome();

        // Find required products by searching and add them to the user's cart
        status = homePage.searchForProduct("YONEX");
        homePage.addProductToCart("YONEX Smash Badminton Racquet");
        status = homePage.searchForProduct("Tan");
        homePage.addProductToCart("Tan Leatherette Weekender Duffle");

        // Click on the checkout button
        homePage.clickCheckout();

        // Add a new address on the Checkout page and select it
        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.addNewAddress("Addr line 1 addr Line 2 addr line 3");
        checkoutPage.selectAddress("Addr line 1 addr Line 2 addr line 3");

        // Place the order
        checkoutPage.placeOrder();

        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.urlToBe("https://crio-qkart-frontend-qa.vercel.app/thanks"));

        // Check if placing order redirected to the Thansk page
        status = driver.getCurrentUrl().endsWith("/thanks");

        // Go to the home page
        homePage.navigateToHome();

        // Log out the user
        homePage.PerformLogout();

        assertTrue(status,"Test Case 5: Happy Flow Test Failed !");
    }

    // Testcase06:  Validation check to read the presence of watch in the cart is not detected
    @Test
    public void TestCase06() throws InterruptedException {
        Boolean status;
        Home homePage = new Home(driver);
        Register registration = new Register(driver);
        Login login = new Login(driver);

        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        
        assertFalse(!status,"User Perform Register Failed!");
        lastGeneratedUserName = registration.lastGeneratedUsername;

        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        
        assertFalse(!status, "User Perform Login Failed");

        homePage.navigateToHome();
        status = homePage.searchForProduct("Xtend Smart Watch");
        homePage.addProductToCart("Xtend Smart Watch");

        status = homePage.searchForProduct("Yarine Floor Lamp");
        homePage.addProductToCart("Yarine Floor Lamp");

        // update watch quantity to 2
        homePage.changeProductQuantityinCart("Xtend Smart Watch", 2);

        // update table lamp quantity to 0
        homePage.changeProductQuantityinCart("Yarine Floor Lamp", 0);

        // update watch quantity again to 1
        homePage.changeProductQuantityinCart("Xtend Smart Watch", 1);

        homePage.clickCheckout();

        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.addNewAddress("Addr line 1 addr Line 2 addr line 3");
        checkoutPage.selectAddress("Addr line 1 addr Line 2 addr line 3");

        checkoutPage.placeOrder();

        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.urlToBe("https://crio-qkart-frontend-qa.vercel.app/thanks"));

        status = driver.getCurrentUrl().endsWith("/thanks");

        homePage.navigateToHome();
        homePage.PerformLogout();

        
        assertTrue(status, "Test Case 6: Verify that cart can be edited Failed!");
    }

    @Test
    public void TestCase07() throws InterruptedException {
        Boolean status = false;
        String products="Stylecon 9 Seater RHS Sofa Set,Xtend Smart Watch";
        String[] productsArr = products.split(",");

        List<String> expectedResult = Arrays.asList(productsArr[0],productsArr[1]);

        Register registration = new Register(driver);
        Login login = new Login(driver);
        Home homePage = new Home(driver);

        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
      
        assertFalse(!status, "User Perform Login Failed!");
        lastGeneratedUserName = registration.lastGeneratedUsername;

        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        
        assertFalse(!status, "User Perform Login Failed !");

        homePage.navigateToHome();
        status = homePage.searchForProduct(productsArr[0]);
        homePage.addProductToCart(productsArr[0]);

        status = homePage.searchForProduct(productsArr[1]);
        homePage.addProductToCart(productsArr[1]);

        homePage.PerformLogout();

        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");

        status = homePage.verifyCartContents(expectedResult);

        assertTrue(status, "Test Case 7: Verify that cart contents are persisted after logout Failed !");

        homePage.PerformLogout();
    }

    @Test
    public void TestCase08() throws InterruptedException {
        Boolean status;

        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        
        assertFalse(!status, "User Perform Registration Failed");
        lastGeneratedUserName = registration.lastGeneratedUsername;

        Login login = new Login(driver);
        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        
        assertFalse(!status, "User Perform Login Failed !");

        Home homePage = new Home(driver);
        homePage.navigateToHome();
        status = homePage.searchForProduct("Stylecon 9 Seater RHS Sofa Set");
        homePage.addProductToCart("Stylecon 9 Seater RHS Sofa Set");

        homePage.changeProductQuantityinCart("Stylecon 9 Seater RHS Sofa Set", 10);

        homePage.clickCheckout();

        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.addNewAddress("Addr line 1 addr Line 2 addr line 3");
        checkoutPage.selectAddress("Addr line 1 addr Line 2 addr line 3");

        checkoutPage.placeOrder();
        Thread.sleep(3000);

        status = checkoutPage.verifyInsufficientBalanceMessage();

        assertTrue(status, "Test Case 8: Verify that insufficient balance error is thrown when the wallet balance is not enough Failed !");
    }

    @Test
    public void TestCase09() throws InterruptedException {
        Boolean status = false;

        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        
        assertFalse(!status, "est Case Failure. Verify that product added to cart is available when a new tab is opened !");
        lastGeneratedUserName = registration.lastGeneratedUsername;

        Login login = new Login(driver);
        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        
        assertFalse(!status,"User Perform Login Failed !");

        Home homePage = new Home(driver);
        homePage.navigateToHome();

        status = homePage.searchForProduct("YONEX");
        homePage.addProductToCart("YONEX Smash Badminton Racquet");

        String currentURL = driver.getCurrentUrl();

        driver.findElement(By.linkText("Privacy policy")).click();
        Set<String> handles = driver.getWindowHandles();
        driver.switchTo().window(handles.toArray(new String[handles.size()])[1]);

        driver.get(currentURL);
        Thread.sleep(2000);

        List<String> expectedResult = Arrays.asList("YONEX Smash Badminton Racquet");
        status = homePage.verifyCartContents(expectedResult);

        driver.close();

        driver.switchTo().window(handles.toArray(new String[handles.size()])[0]);

        assertTrue(status, "Test Case 9: Verify that product added to cart is available when a new tab is opened failed !");
    }

    @Test
    public void TestCase10() throws InterruptedException {
        Boolean status = false;

        // takeScreenshot(driver, "StartTestCase", "TestCase10");

        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        
        assertFalse(!status, "Test Case Failure.  Verify that the Privacy Policy, About Us are displayed correctly ");
        lastGeneratedUserName = registration.lastGeneratedUsername;

        Login login = new Login(driver);
        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
       
        assertFalse(!status, "User Perform Login Failed !");

        Home homePage = new Home(driver);
        homePage.navigateToHome();

        String basePageURL = driver.getCurrentUrl();

        driver.findElement(By.linkText("Privacy policy")).click();
        status = driver.getCurrentUrl().equals(basePageURL);

        assertFalse(!status, "Verifying parent page url didn't change on privacy policy link click failed !");

        Set<String> handles = driver.getWindowHandles();
        driver.switchTo().window(handles.toArray(new String[handles.size()])[1]);
        WebElement PrivacyPolicyHeading = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/h2"));
        status = PrivacyPolicyHeading.getText().equals("Privacy Policy");
       
        assertFalse(!status, "Verifying new tab opened has Privacy Policy page heading failed !");

        driver.switchTo().window(handles.toArray(new String[handles.size()])[0]);
        driver.findElement(By.linkText("Terms of Service")).click();

        handles = driver.getWindowHandles();
        driver.switchTo().window(handles.toArray(new String[handles.size()])[2]);
        WebElement TOSHeading = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/h2"));
        status = TOSHeading.getText().equals("Terms of Service");
        
        assertFalse(!status, "Verifying new tab opened has Terms Of Service page heading failed !");

        driver.close();
        driver.switchTo().window(handles.toArray(new String[handles.size()])[1]).close();
        driver.switchTo().window(handles.toArray(new String[handles.size()])[0]);

        // takeScreenshot(driver, "EndTestCase", "TestCase10");
        assertTrue(status, "Test Case 10: Verify that the Privacy Policy, About Us are displayed correctly failed !"); 
    }

    @Test
    public void TestCase11() throws InterruptedException {
        
        // takeScreenshot(driver, "StartTestCase", "TestCase11");

        Home homePage = new Home(driver);
        homePage.navigateToHome();

        driver.findElement(By.xpath("//*[text()='Contact us']")).click();

        WebElement name = driver.findElement(By.xpath("//input[@placeholder='Name']"));
        name.sendKeys("crio user");
        WebElement email = driver.findElement(By.xpath("//input[@placeholder='Email']"));
        email.sendKeys("criouser@gmail.com");
        WebElement message = driver.findElement(By.xpath("//input[@placeholder='Message']"));
        message.sendKeys("Testing the contact us page");

        WebElement contactUs = driver.findElement(
                By.xpath("/html/body/div[2]/div[3]/div/section/div/div/div/form/div/div/div[4]/div/button"));

        contactUs.click();

        WebDriverWait wait = new WebDriverWait(driver, 30);
        wait.until(ExpectedConditions.invisibilityOf(contactUs));

        // takeScreenshot(driver, "EndTestCase", "TestCase11");

        assertTrue(true, "Test Case 11: Verify that contact us option is working correctly failed !");
    }

    @Test
    public void TestCase12() throws InterruptedException {
        Boolean status = false;
        // takeScreenshot(driver, "StartTestCase", "TestCase12");

        Register registration = new Register(driver);
        registration.navigateToRegisterPage();
        status = registration.registerUser("testUser", "abc@123", true);
        assertFalse(!status, "Test Case Failure. Ensure that the links on the QKART advertisement are clickable failed !");
        lastGeneratedUserName = registration.lastGeneratedUsername;

        Login login = new Login(driver);
        login.navigateToLoginPage();
        status = login.PerformLogin(lastGeneratedUserName, "abc@123");
        assertFalse(!status,"User Perform Login Failed !");

        Home homePage = new Home(driver);
        homePage.navigateToHome();

        status = homePage.searchForProduct("YONEX Smash Badminton Racquet");
        homePage.addProductToCart("YONEX Smash Badminton Racquet");
        homePage.changeProductQuantityinCart("YONEX Smash Badminton Racquet", 1);
        homePage.clickCheckout();

        Checkout checkoutPage = new Checkout(driver);
        checkoutPage.addNewAddress("Addr line 1  addr Line 2  addr line 3");
        checkoutPage.selectAddress("Addr line 1  addr Line 2  addr line 3");
        checkoutPage.placeOrder();
        Thread.sleep(3000);

        String currentURL = driver.getCurrentUrl();

        List<WebElement> Advertisements = driver.findElements(By.xpath("//iframe"));

        status = Advertisements.size() == 3;
        assertTrue(status, "Verify that 3 Advertisements are available failed !");

        WebElement Advertisement1 = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div/iframe[1]"));
        driver.switchTo().frame(Advertisement1);
        driver.findElement(By.xpath("//button[text()='Buy Now']")).click();
        driver.switchTo().parentFrame();

        status = !driver.getCurrentUrl().equals(currentURL);
        assertTrue(status, "Verify that Advertisement 1 is clickable failed !");

        driver.get(currentURL);
        Thread.sleep(3000);

        WebElement Advertisement2 = driver.findElement(By.xpath("//*[@id=\"root\"]/div/div[2]/div/iframe[2]"));
        driver.switchTo().frame(Advertisement2);
        driver.findElement(By.xpath("//button[text()='Buy Now']")).click();
        driver.switchTo().parentFrame();

        status = !driver.getCurrentUrl().equals(currentURL);
        
        assertTrue(status, "Verify that Advertisement 2 is clickable, Test Case 12:  Ensure that the links on the QKART advertisement are clickable !");
    }




    @AfterSuite
    public static void quitDriver() {
        System.out.println("quit()");
        driver.quit();
    }

    public static void logStatus(String type, String message, String status) {

        System.out.println(String.format("%s |  %s  |  %s | %s", String.valueOf(java.time.LocalDateTime.now()), type,
                message, status));
    }

    public static void takeScreenshot(WebDriver driver, String screenshotType, String description) {
        try {
            File theDir = new File("/screenshots");
            if (!theDir.exists()) {
                theDir.mkdirs();
            }
            String timestamp = String.valueOf(java.time.LocalDateTime.now());
            String fileName = String.format("screenshot_%s_%s_%s.png", timestamp, screenshotType, description);
            TakesScreenshot scrShot = ((TakesScreenshot) driver);
            File SrcFile = scrShot.getScreenshotAs(OutputType.FILE);
            File DestFile = new File("screenshots/" + fileName);
            FileUtils.copyFile(SrcFile, DestFile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

