package com.infinull.sit.enums;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public enum SITOBJECTTYPE {
    BLOB("blob"),
    COMMIT("commit"),
    TREE("tree");

    private final String name;
    private static final Set<String> VALID_TYPES = new HashSet<>();

    static {
        for (SITOBJECTTYPE type : SITOBJECTTYPE.values()) {
            VALID_TYPES.add(type.name);
        }
    }

    SITOBJECTTYPE(String name) {
        this.name = name;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public static boolean isValid(String name) {
        return VALID_TYPES.contains(name);
    }

    public static SITOBJECTTYPE fromName(String name) {
        return Arrays.stream(values())
                .filter(sitObjType -> sitObjType.name.equals(name))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("Invalid Sit Object Type: " + name));
    }
}
