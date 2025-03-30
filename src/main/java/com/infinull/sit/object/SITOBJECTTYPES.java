package com.infinull.sit.object;

public enum SITOBJECTTYPES {
    BLOB("blob"),
    TREE("tree"),
    COMMIT("commit"),
    TAG("tag");

    private final String typeString;

    SITOBJECTTYPES(String typeString) {
        this.typeString = typeString;
    }

    public String getTypeString() {
        return typeString;
    }

    public static SITOBJECTTYPES fromString(String typeString) {
        for (SITOBJECTTYPES objType : SITOBJECTTYPES.values()) {
            if (objType.typeString.equalsIgnoreCase(typeString)) {
                return objType;
            }
        }
        throw new IllegalArgumentException("Unknown object type: " + typeString);
    }
}
