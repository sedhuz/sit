package com.infinull.sit.cmd.init;

import com.infinull.sit.message.MessageUtil;
import java.io.*;

public class SitInit {
	public static int run() {
		String cwd = System.getProperty("user.dir");
		File sitDir = new File(cwd, ".git");

		if (sitDir.exists()) {
			if (sitDir.isDirectory()) {
				MessageUtil.printMsg("git.exists");
				return 1;
			} else {
				MessageUtil.printMsg("git.is.file");
				return 1;
			}
		} else {
			if (sitDir.mkdirs()) {
				new File(sitDir, "objects").mkdirs();
				new File(sitDir, "refs").mkdirs();
				new File(sitDir, "refs/heads").mkdirs();
				new File(sitDir, "hooks").mkdirs();
				new File(sitDir, "info").mkdirs();

				File configFile = new File(sitDir, "config");
				try {
					configFile.createNewFile();
				} catch (IOException e) {
					MessageUtil.printMsg("config.error");
					return 1;
				}

				File headFile = new File(sitDir, "HEAD");
				try (FileWriter writer = new FileWriter(headFile)) {
					writer.write("ref: refs/heads/master");
				} catch (IOException e) {
					MessageUtil.printMsg("head.error");
					return 1;
				}

				MessageUtil.printMsg("init.success", sitDir.getPath());
				return 0;
			} else {
				MessageUtil.printMsg("no.git.dir");
				return 1;
			}
		}
	}
}
