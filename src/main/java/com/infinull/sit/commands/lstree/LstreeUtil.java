package com.infinull.sit.commands.lstree;

import com.infinull.sit.object.SitObject;
import com.infinull.sit.object.Tree;
import com.infinull.sit.store.ObjectStore;
import com.infinull.sit.util.Sha;

public class LstreeUtil {
    public static void printTreeContent(Sha sha) {
        SitObject sitObject = ObjectStore.get(sha);
        if (sitObject instanceof Tree) {
            Tree tree = (Tree) sitObject;
            System.out.println(tree.getContent());
        } else {
            System.out.println("Not a tree object");
        }
    }
}
