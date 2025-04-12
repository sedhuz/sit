package com.infinull.sit;

import com.infinull.sit.exception.SitException;
import com.infinull.sit.message.MessageUtil;
import com.infinull.sit.persistence.SitFileUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class App {
    private static final String[] COMMANDS_THAT_CAN_SKIP_INIT = {"init", "clone"};

    public static void main(String[] args) {
        if (args.length == 0) {
            MessageUtil.printMsg("usage.sit");
            System.exit(1);
        }
        String command = args[0];
        args = Arrays.copyOfRange(args, 1, args.length);
        try {
            if (!Arrays.asList(COMMANDS_THAT_CAN_SKIP_INIT).contains(command))
                init();
            executeCommand(command, args);
        } catch (SitException e) {
            System.out.println(e.getMessage());
            System.exit(e.getStatusCode());
        }
    }

    public static void test(String[] args) {
        if (args.length == 0) {
            MessageUtil.printMsg("usage.sit");
            return;
        }
        String command = args[0];
        args = Arrays.copyOfRange(args, 1, args.length);
        try {
            init();
            executeCommand(command, args);
        } catch (SitException e) {
            System.out.println(e.getMessage());
        }
    }

    // static initializers go here!
    private static void init() {
        SitFileUtil.initialize();
    }

    private static void executeCommand(String command, String[] args) {
        final String packageName = "com.infinull.sit.commands"; // Base package for commands
        final String className = packageName + "." + command.toLowerCase() + "." + capitalize(command) + "Command";

        try {
            Class<?> commandClass = Class.forName(className);
            Object commandClassInstance = commandClass.getDeclaredConstructor().newInstance();
            Method runMethod = commandClass.getMethod("run", String[].class);
            runMethod.invoke(commandClassInstance, (Object) args);
        } catch (InvocationTargetException e) {
            Throwable targetException = e.getTargetException();
            if (targetException instanceof SitException) {
                throw (SitException) targetException;
            } else {
                targetException.printStackTrace();
            }
        } catch (ClassNotFoundException e) {
            throw new SitException(1, "error.command.unknown", command);
        } catch (Exception e) {
            throw new SitException(1, "error.command.execute", command);
        }
    }

    private static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
