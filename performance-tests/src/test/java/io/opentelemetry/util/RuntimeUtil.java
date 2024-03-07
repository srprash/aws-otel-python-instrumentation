package io.opentelemetry.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.io.BufferedReader;
import java.io.InputStreamReader;

public class RuntimeUtil {
    private static final Logger logger = LoggerFactory.getLogger(RuntimeUtil.class);
    private static final int CORE_COUNT = getCpuCoreCount();

    // E.g. if 6 core system, "0-2"
    public static String getNonApplicationCores() {
        String cpus = String.format("0-%s", CORE_COUNT/2 - 1);
        logger.info(String.format("Non-App Cores: %s.", cpus));
        return cpus;
    }

    // E.g. if 6 core system, "3-5"
    public static String getApplicationCores() {
        String cpus = String.format("%s-%s", CORE_COUNT/2, CORE_COUNT - 1);
        logger.info(String.format("App Cores: %s.", cpus));
        return cpus;
    }

    public static String getDuration() {
        String env_value = System.getenv("DURATION");
        String duration = (env_value == null) ? "10s" : env_value;
        logger.info(String.format("Duration %s.", duration));
        return duration;
    }

    // Adapted from https://stackoverflow.com/questions/4759570/finding-number-of-cores-in-java
    private static int getCpuCoreCount() {
        String os = System.getProperty("os.name").toLowerCase();
        try {
            if (os.contains("mac")) {
                logger.info("Detect mac.");
                return getCoreCountMac();
            } else {
                logger.info("Assume linux.");
                return getCoreCountLinux();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private static int getCoreCountMac() throws Exception {
        String command = "sysctl -n machdep.cpu.core_count";
        String[] cmd = {"/bin/sh", "-c", command};
        Process process = Runtime.getRuntime().exec(cmd);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = reader.readLine();
        return line.length() > 0 ? Integer.parseInt(line) : 0;
    }

    private static int getCoreCountLinux() throws Exception {
        String command = "lscpu";
        Process process = Runtime.getRuntime().exec(command);
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = reader.readLine();
        int threadsPerCore = 0;
        int coresPerSocket = 0;
        int sockets = 0;
        while (line != null) {
            if (line.contains("Thread(s) per core:")) {
                threadsPerCore = Integer.parseInt(line.split("\\s+")[line.split("\\s+").length - 1]);
            }
            if (line.contains("Core(s) per socket:")) {
                coresPerSocket = Integer.parseInt(line.split("\\s+")[line.split("\\s+").length - 1]);
            }
            if (line.contains("Socket(s):")) {
                sockets = Integer.parseInt(line.split("\\s+")[line.split("\\s+").length - 1]);
            }
            line = reader.readLine();
        }
        return threadsPerCore * coresPerSocket * sockets;
    }
}

