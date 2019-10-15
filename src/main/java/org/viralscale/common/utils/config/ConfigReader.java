package org.viralscale.common.utils.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ConfigReader {
    private static final Logger logger = LoggerFactory.getLogger(ConfigReader.class);

    private String filepath;
    private Properties properties;

    private ConfigReader() {}

    public ConfigReader(String filepath) {
        this.filepath = filepath;
        this.properties = new Properties();
    }

    public void readFile() throws IOException {
        // Check if the path contains an initial slash
        if (this.filepath.charAt(0) == '/') {
            // Start by checking if the original file exists
            this.filepath = Files.exists(Paths.get(this.filepath)) ? this.filepath : this.filepath.substring(1);
        }

        logger.info("Loading config at path " + this.filepath);

        InputStream inputStream = Files.newInputStream(Paths.get(this.filepath));

        properties.load(inputStream);
    }

    public String getValue(String key) {
        return this.getValue(key, null);
    }

    public Integer getValueAsInteger(String key) {
        String val = this.getValue(key, null);
        if (val == null) {
            return null;
        }

        return Integer.parseInt(val);
    }

    public Integer getValueAsInteger(String key, Integer defaultValue) {
        String val = this.getValue(key, null);
        if (val == null) {
            return defaultValue;
        }

        return Integer.parseInt(val);
    }

    public String getValue(String key, String defaultValue) {
        // Do all the resolution for the env vars here
        String value = this.properties.getProperty(key, defaultValue);
        if (value == null) {
            return null;
        }

        return resolveEnvVars(value);
    }

    private String resolveEnvVars(String input)
    {
        if (null == input)
        {
            return null;
        }
        // match ${ENV_VAR_NAME} or $ENV_VAR_NAME
        Pattern p = Pattern.compile("\\$\\{(\\w+)\\}|\\$(\\w+)");
        Matcher m = p.matcher(input); // get a matcher object
        StringBuffer sb = new StringBuffer();
        while(m.find()){
            String envVarName = null == m.group(1) ? m.group(2) : m.group(1);
            String envVarValue = System.getenv(envVarName);
            m.appendReplacement(sb, null == envVarValue ? "" : envVarValue);
        }
        m.appendTail(sb);
        return sb.toString();
    }




}
