package com.lihd.java;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.util.Properties;

/**
 * @program: zookeeper-sample
 * @description:
 * @author: li_hd
 * @create: 2020-03-11 16:26
 **/
public class PropertiesUtils {


    /**
     * 根据key读取value
     * @param key
     * @return
     */
    public static String readValue(String key) {

        final URL resource = PropertiesUtils.class.getClassLoader().getResource("app.properties");

        Properties prop = new Properties();
        InputStream in = null;
        try {
            assert resource != null;
            in = new BufferedInputStream(new FileInputStream(new File(resource.toURI())));
            prop.load(in);
            return prop.getProperty(key);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (in != null) {
                    in.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return "";
    }


}
