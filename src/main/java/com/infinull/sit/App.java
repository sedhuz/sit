package com.infinull.sit;

import com.infinull.sit.exception.SitException;
import com.infinull.sit.message.MessageUtil;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

public class App {
    public static void main(String[] args) {
        if (args.length == 0) {
            MessageUtil.printMsg("usage.message");
            System.exit(1);
        }
        String command = args[0];
        args = Arrays.copyOfRange(args,1,args.length);
        try {
            executeCommand(command, args);
        } catch (SitException e) {
            System.out.println(e.getMessage());
            System.exit(e.getStatusCode());
        }
    }

    public static void test(String[] args) {
        if (args.length == 0) {
            MessageUtil.printMsg("usage.message");
            return;
        }
        String command = args[0];
        args = Arrays.copyOfRange(args,1,args.length);
        try {
            executeCommand(command, args);
        } catch (SitException e) {
            System.out.println(e.getMessage());
        }
    }

    private static void executeCommand(String command, String[] args) {
        String className = "com.infinull.sit.cmd." + command.toLowerCase() + ".Sit" + capitalize(command);

        try {
            Class<?> commandClass = Class.forName(className);
            Method runMethod = commandClass.getMethod("run", String[].class);
            runMethod.invoke(null, (Object) args);
        } catch (ClassNotFoundException e) {
            throw new SitException(1,"error.command.unknown", command);
        } catch (NoSuchMethodException | IllegalAccessException e) {
            throw new SitException(1,"error.command.execute", command);
        } catch (InvocationTargetException e) {
            throw (SitException) e.getTargetException();
        }
    }

    private static String capitalize(String str) {
        return str.substring(0, 1).toUpperCase() + str.substring(1);
    }
}
