package az.hibernate.util;

import java.io.InputStream;
import java.util.Properties;
import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;

@UtilityClass
public class PropertiesUtil {

    private static final Properties PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }

    @SneakyThrows
    private static void loadProperties() {
        try (InputStream resourceAsStream = PropertiesUtil.class.getClassLoader().getResourceAsStream("application.properties")) {
            PROPERTIES.load(resourceAsStream);
        }
    }

}
