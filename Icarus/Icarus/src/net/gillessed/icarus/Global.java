package net.gillessed.icarus;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

public class Global {

	public static String THREAD_POOL_COUNT = "threadpool-count";
	public static final String BEGINNING_SKIP_COUNT = "beginning-skip-count";
	public static final String SUPERSAMPLE_COUNT = "supersample-count";
	public static final String BLUR_ON = "blur-on";
	public static final String LOG_BASE = "log-base";
	
	private static Properties icarusProperties;
	private static boolean icarusPropertiesLoaded;
	
	private static final File ICARUS_PROPERTIES_FILE = 
			new File("resources" + File.separator + "icarus.properties");
	
	public static String getProperty(String propertyName) {
		if(!icarusPropertiesLoaded) {
			icarusProperties = new Properties();
			try {
				icarusProperties.load(new FileInputStream(ICARUS_PROPERTIES_FILE));
			} catch (FileNotFoundException e) {
				throw new RuntimeException(e);
			} catch (IOException e) {
				throw new RuntimeException(e);
			}
		}
		return (String) icarusProperties.get(propertyName);
	}
}
