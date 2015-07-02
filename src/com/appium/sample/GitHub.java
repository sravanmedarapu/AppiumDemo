package com.appium.sample;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.android.AndroidDeviceActionShortcuts;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.AndroidKeyCode;
import io.appium.java_client.remote.MobileCapabilityType;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;


public class GitHub {
	public static AppiumDriver<?> driver;
	public static AndroidDriver<?> androidDriver;
	@FindBy(id="com.github.mobile:id/et_login") private static WebElement userName;
	@FindBy(id="com.github.mobile:id/et_password") private static WebElement pasword;
	@FindBy(id="com.github.mobile:id/cb_show_password") private static WebElement showPassword;
	@FindBy(id="com.github.mobile:id/m_login") private static WebElement logIn;
	@FindBy(id="com.github.mobile:id/navigation_drawer_item_name") private static WebElement home;

	public static void main(String[] args) throws MalformedURLException, InterruptedException {
		try{
			appiumDriver();//driver creation 
			logIn();//login to the application
			homePage();//navigate between the pages
			Thread.sleep(2000);
			search();//search for repository
			Thread.sleep(2000);
			System.out.println("Quitting the driver");
			driver.quit();//quit driver
		}catch(Exception e){
			e.getMessage();
			e.printStackTrace();
			driver.quit();
		}
	}

	//login to the application
	public static void logIn() throws InterruptedException{
		GitHub page = PageFactory.initElements(driver, GitHub.class);
		if(driver.findElements(By.id("com.github.mobile:id/et_login")).size()>0){
			System.out.println("Entering username");
			userName.sendKeys("github username");

			//hide keyboard
			driver.hideKeyboard();
			showPassword.click();
			showPassword.click();
			Thread.sleep(1000);
			//enter password
			System.out.println("Entering password");
			pasword.sendKeys("github password");

			//hide keyboard
			driver.hideKeyboard();
			//click on login button
			System.out.println("Click on login");
			logIn.click();

			WebDriverWait wait =new WebDriverWait(driver, 80);
			System.out.println("Waiting 50 sec for \"Loading...\" to be invisible");
			wait.until(ExpectedConditions.invisibilityOfElementLocated(By.name("Logging in…")));
			home.click();
			if(((AndroidDriver) driver).findElementsByAndroidUIAutomator("new UiSelector().description(\"Open navigation drawer\")").size()>0){
				((AndroidDriver) driver).findElementByAndroidUIAutomator("new UiSelector().description(\"Open navigation drawer\")").click();
			}
			Thread.sleep(2000);
		}
		else{
			System.out.println("Application already logged in");
		}
	}

	/**
	 * navigate across the pages 
	 * @throws InterruptedException
	 */
	public static void homePage() throws InterruptedException{
		WebDriverWait wait =new WebDriverWait(driver, 50);
		wait.until(ExpectedConditions.visibilityOfElementLocated(By.name("Home")));
		System.out.println("Click on Home");
		driver.findElement(By.name("Home")).click();
		waitForProcess();
		System.out.println("Click on news");
		driver.findElement(By.name("news")).click();
		waitForProcess();
		System.out.println("Click on repositories");
		driver.findElement(By.name("repositories")).click();
		waitForProcess();
		System.out.println("Swipe Left");
		driver.swipe(260, 260, 39, 260, 1000);
		System.out.println("Click on followers");
		driver.findElement(By.name("followers")).click();
		waitForProcess();
		System.out.println("Click on following");
		driver.findElement(By.name("following")).click();
		waitForProcess();

		((AndroidDriver) driver).findElementByAndroidUIAutomator("new UiSelector().description(\"Open navigation drawer\")").click();;
		driver.findElement(By.name("Gist")).click();
		System.out.println("Waiting 50 sec for Loading Gist page ");
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("com.github.mobile:id/pb_loading")));

		((AndroidDriver) driver).findElementByAndroidUIAutomator("new UiSelector().description(\"Open navigation drawer\")").click();;

		driver.findElement(By.name("Issue Dashboard")).click();
		System.out.println("Waiting 50 sec for Loading Issue Dashboard");
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("com.github.mobile:id/pb_loading")));

		((AndroidDriver) driver).findElementByAndroidUIAutomator("new UiSelector().description(\"Open navigation drawer\")").click();;

		driver.findElement(By.name("Bookmarks")).click();
		System.out.println("Waiting 50 sec for Loading Bookmarks");
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("com.github.mobile:id/pb_loading")));
		Thread.sleep(2000);

	}

	/**
	 * searching for repository
	 */
	public static void search(){
		try{
			driver.findElement(By.id("com.github.mobile:id/m_search")).click();;
			driver.findElement(By.id("com.github.mobile:id/search_src_text")).sendKeys("java-client");
			System.out.println("Searching for java-client");
			((AndroidDeviceActionShortcuts) driver).sendKeyEvent(AndroidKeyCode.ENTER);
			waitForProcess();
			System.out.println("Scroll down");
			driver.swipe(150, 400, 150, 180, 1000);
			Thread.sleep(2000);
			System.out.println("Scroll down");
			driver.swipe(150, 400, 150, 180, 1000);
		}catch(Exception e){
			System.out.println(e.getMessage());
		}
	}

	/**
	 * wait for page to load
	 */
	public static void waitForProcess(){
		WebDriverWait wait =new WebDriverWait(driver, 50);
		System.out.println("Waiting 50 sec for Loading Home page");
		wait.until(ExpectedConditions.invisibilityOfElementLocated(By.id("com.github.mobile:id/pb_loading")));
	}

	/**
	 * For invoking AppiumDriver 
	 * @throws MalformedURLException 
	 */
	public static void appiumDriver() throws MalformedURLException{
		File appDir = new File("src/");
		File app = new File(appDir, "app-debug.apk");
		DesiredCapabilities capabilities = new DesiredCapabilities();
		//capabilities.setCapability(MobileCapabilityType.BROWSER_NAME, "");
		capabilities.setCapability(MobileCapabilityType.DEVICE_NAME, "Android Emulator");
		capabilities.setCapability(MobileCapabilityType.APP, app.getAbsolutePath());
		capabilities.setCapability(MobileCapabilityType.NEW_COMMAND_TIMEOUT, 120);
		androidDriver = new AndroidDriver(new URL("http://127.0.0.1:4723/wd/hub"), capabilities);
		driver=androidDriver;
		System.out.println("AppiumDriver started");

	}
}
