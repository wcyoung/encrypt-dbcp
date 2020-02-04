package wcyoung.encrypt.dbcp.config;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationProperties {

    private Properties properties;

    public ConfigurationProperties(String defaultFilePath) {
        this("dbcp.configurationFile", defaultFilePath);
    }

    public ConfigurationProperties(String argumentKey, String defaultFilePath) {
        try {
            InputStream inputStream = null;

            String argumentFilePath = System.getProperty(argumentKey);
            if (argumentFilePath != null && argumentFilePath.trim().length() != 0) {
                inputStream = new FileInputStream(argumentFilePath);
            } else {
                inputStream = getClass().getClassLoader().getResourceAsStream(defaultFilePath);
            }

            properties = new Properties();
            properties.load(inputStream);
            inputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) {
        return properties.getProperty(key, defaultValue);
    }

}
