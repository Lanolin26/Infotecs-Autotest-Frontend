package ru.lanolin;

public class MainTest {

	private static final Config config;

	static{
		config = Config.getInstance();
		config.load(MainTest.class);
	}

	public static void main(String[] args) {

		System.out.println(config.getStringProperty("site"));

	}

}
