package com.infinull.sit.cmd.catfile;

import com.infinull.sit.cmd.SitCommand;
import com.infinull.sit.message.MessageUtil;

public class SitCatfile implements SitCommand {
	public static void run(String[] args) {
		if (args.length < 2) { // Ensure 2 args are give flag & <obj_sha>
			MessageUtil.printMsgAndExit(1,"catfile.usage");
		}

		String flag = args[0];
		String sha = args[1];

		switch (flag) {
			case "-p":
				CatUtil.printObject(sha);
			case "-s":
				CatUtil.printSize(sha);
			case "-t":
				CatUtil.printType(sha);
			case "-e":
				CatUtil.checkExistence(sha);
			default:
				MessageUtil.printMsgAndExit(1,"catfile.usage");
		}
	}
}
