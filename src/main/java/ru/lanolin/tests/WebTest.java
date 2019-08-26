package ru.lanolin.tests;

import org.junit.*;
import org.junit.runner.RunWith;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriverService;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import ru.lanolin.Config;
import ru.lanolin.Main;

import java.io.File;
import java.io.IOException;

@RunWith(BlockJUnit4ClassRunner.class)
public class WebTest extends Assert{

	private static ChromeDriverService service;
	private static Config configChrome;

	private WebDriver driver;
	private Config loginAndPassword;
	private String site;
	private String site_true;

	@BeforeClass
	public static void createAndStartService() {
		configChrome = new Config(Main.class, "chrome.properties");

		service = new ChromeDriverService.Builder()
				.usingDriverExecutable(new File(configChrome.getStringProperty("chromedriver")))
				.usingAnyFreePort()
				.build();

		try {
			service.start();
		} catch (IOException e) { e.printStackTrace(); }
	}

	@Before
	public void before(){
		driver = new RemoteWebDriver(service.getUrl(), DesiredCapabilities.chrome());
		loginAndPassword = new Config(Main.class, "application.properties");
		site = configChrome.getStringProperty("site");
		site_true = configChrome.getStringProperty("true_site");
	}

	@Test
	public void test(){
		WebDriverWait wait = new WebDriverWait(driver, 30);
		driver.get(site);

		WebElement login = driver.findElement(By.id("username"));
		WebElement passwd = driver.findElement(By.id("password"));
		WebElement singIn = driver.findElement(By.id("sign-in-btn"));

		login.sendKeys(loginAndPassword.getStringProperty("login"));
		passwd.sendKeys(loginAndPassword.getStringProperty("password"));

		singIn.click();

		try {Thread.sleep(500); } catch (InterruptedException e) { e.printStackTrace(); }

		wait.until(ExpectedConditions.or(
				ExpectedConditions.presenceOfElementLocated(By.id("notification-div")),
				ExpectedConditions.and(
					ExpectedConditions.presenceOfElementLocated(By.className("ReactModalPortal")),
					ExpectedConditions.presenceOfElementLocated(By.cssSelector("#app-mount-point > header"))
				)
		));

		String test_site = driver.getCurrentUrl();

		assertTrue("Не был выполнена авторизация. Проверте логин и парроль",
				!test_site.equals(site) && test_site.equals(site_true));
	}

	@After
	public void after(){
		driver.quit();
		loginAndPassword.clear();
	}

	@AfterClass
	public static void createAndStopService() {
		service.stop();
		configChrome.clear();
	}
}
