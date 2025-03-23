package jp.favoriteigo;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyUtil {
    private static final Properties properties = new Properties();

    static {
        try (InputStream inputStream = PropertyUtil.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (inputStream == null) {
                throw new IOException("config.propertiesが見つかりません");
            }
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("config.propertiesの読み込みに失敗しました", e);
        }
    }

    private PropertyUtil() {
        throw new UnsupportedOperationException("PropertyUtilはインスタンス化できません");
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}