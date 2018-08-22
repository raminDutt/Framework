package framework;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;
import java.util.logging.Logger;
import java.util.stream.Stream;

public class Factory {

	private static Logger logger = Logger.getGlobal();

	public static List<? extends Object> getPlugin(Class<?> plugin) {
		List<Object> iplugins = new ArrayList<>();

		// Reading the jar location of plugins from a config file (config.txt)
		URL[] urls = null;
		String fileName = "config.txt";
		File file = new File(fileName);
		try (InputStream inputStream = new FileInputStream(file);
				InputStreamReader reader = new InputStreamReader(inputStream);
				BufferedReader bufferedReader = new BufferedReader(reader);
				Stream<String> lines = bufferedReader.lines();) {
			urls = lines.map(line -> {
				String path = "file://" + line;
				URL url = null;
				try {
					url = new URL(path);
				} catch (Exception e) {
					e.printStackTrace();
				}
				return url;
			}).toArray(URL[]::new);

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}

		// Load plugins that are specified in the config file.
		// The loader will always first delegate to parent loader to find class
		// before attempting to load it itself
		
		ServiceLoader<?> loader = null;
		if (urls != null) {
			URLClassLoader urlClassLoader = URLClassLoader.newInstance(urls);
			loader = ServiceLoader.load(plugin, urlClassLoader);			
		} else {
			loader = ServiceLoader.load(plugin);
		}

		Iterator<?> iterator = loader.iterator();

		while (iterator.hasNext()) {
			try {
				iplugins.add(iterator.next());
			} catch (ServiceConfigurationError configurationError) {
				logger.warning(configurationError.getMessage());
			}
		}

		return iplugins;
	}
}
