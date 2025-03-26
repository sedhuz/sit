package com.infinull.sit;

import com.infinull.sit.message.MessageUtil;
import java.lang.reflect.Method;

public class App {
    public static void main(String[] args) {
        if (args.length == 0) {
            MessageUtil.printMsg("usage.message");
            System.exit(1);
        }

        String command = args[0];
        int statusCode = executeCommand(command);

        System.exit(statusCode);
    }

    private static int executeCommand(String command) {
        String className = "com.infinull.sit.cmd" + command.toLowerCase() + ".Sit" + capitalize(command);

        try {
            // Load the class dynamically
            Class<?> commandClass = Class.forName(className);
            Method runMethod = commandClass.getMethod("run");

            // Invoke the run() method
            return (int) runMethod.invoke(null);
        } catch (ClassNotFoundException e) {
            MessageUtil.printMsg("unknown.command", command);
        } catch (Exception e) {
            e.printStackTrace();
            MessageUtil.printMsg("command.error", command);
        }
        return 1;
    }

    private static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
