package wcyoung.encrypt.dbcp.config;

import java.io.FileInputStream;
import java.io.InputStream;
import java.util.Properties;

public class ConfigurationProperties {

    private Properties properties;

    public ConfigurationProperties() {
        try {
            InputStream inputStream = null;

            String argumentFilePath = System.getProperty("dbcp.configurationFile");
            if (argumentFilePath != null && argumentFilePath.trim().length() != 0) {
                inputStream = new FileInputStream(argumentFilePath);
            }

            properties = new Properties();

            if (inputStream != null) {
                properties.load(inputStream);
                inputStream.close();
            }
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
