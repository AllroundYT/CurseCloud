package dev.allround.cloud.util;

import java.io.IOException;
import java.net.ServerSocket;

public class PortChecker {
    public static int getFreePort(int lowest, int highest, int ideal) {
        if (isPortFree(ideal)) return ideal;
        int port = lowest;
        while (port <= highest && !isPortFree(port)) {
            port++;
        }
        if (port > highest) throw new RuntimeException("No free port found.");
        return port;
    }

    public static boolean isPortFree(int port) {
        try {
            new ServerSocket(port).close();
            return true;
        } catch (IOException e) {
            return false;
        }
    }
}
