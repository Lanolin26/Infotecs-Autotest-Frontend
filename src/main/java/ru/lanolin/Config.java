package ru.lanolin;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;
import java.util.Properties;

public class Config {

	private final Properties properties;

	private static Config instance;

	public static Config getInstance() {
		return instance == null ? instance = new Config() : instance;
	}

	private Config(){
		properties = new Properties();
	}

	public void load(Class classStart){
		InputStream propertyFile = classStart.getClassLoader().getResourceAsStream("application.properties");
		try {
			properties.load(propertyFile);
		} catch (IOException e) {
			System.err.println("Ошибка при чтении конфигурационных файлов");
			e.printStackTrace();
			System.exit(1);
		}
	}

	public int getIntegerProperty(String key){
		return getIntegerProperty(key, 0);
	}

	public int getIntegerProperty(String key, int defaultValue){
		String value = properties.getProperty(key);
		return Objects.isNull(value) ? defaultValue : Integer.parseInt(value);
	}

	public String getStringProperty(String key){
		return getStringProperty(key, "");
	}

	public String getStringProperty(String key, String defaultValue){
		String value = properties.getProperty(key);
		return Objects.isNull(value) ? defaultValue : value;
	}

	public boolean getBooleanProperty(String key){
		return getBooleanProperty(key, false);
	}

	public boolean getBooleanProperty(String key, boolean defaultValue){
		String value = properties.getProperty(key);
		return Objects.isNull(value) ? defaultValue : Boolean.getBoolean(value);
	}

}
