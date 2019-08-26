package ru.lanolin;

import org.apache.commons.exec.OS;
import org.junit.runner.JUnitCore;
import org.junit.runner.Result;
import org.junit.runner.notification.Failure;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import ru.lanolin.tests.WebTest;

import java.io.File;

public class Main {

	private static final Config config;

	static{
		config = new Config(Main.class, "chrome.properties");
	}


	public static void main(String[] args) {
		String os = System.getProperty("os.name");

		if(!(os.contains("win") || os.contains("nix") || os.contains("nux") || os.contains("aix"))){
			System.err.println("Данный тест проводится на платфорах win/linux");
			return;
		}

		String driver = config.getStringProperty("chromedriver");
		File f = new File(driver);
		if(!f.exists()){
			System.err.println("Не найден chromedriver. Проверте его наличие в папке.");
			return;
		}

		Result result = JUnitCore.runClasses(WebTest.class);

		System.out.println("Всего тестов " + result.getRunCount());
		System.out.println("Проваленный тестов " + result.getFailureCount());
		System.out.println("Успешное прохождение тестов: " + result.wasSuccessful());

		for(Failure failure : result.getFailures()) {
			System.err.println("Ошибка: " + failure.getMessage());
		}
	}

}
