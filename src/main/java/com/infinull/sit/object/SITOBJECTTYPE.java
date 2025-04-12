package com.infinull.sit.object;

public enum SITOBJECTTYPE {
    BLOB("blob"),
    TREE("tree"),
    COMMIT("commit"),
    TAG("tag");

    private final String typeString;

    SITOBJECTTYPE(String typeString) {
        this.typeString = typeString;
    }

    public String getTypeString() {
        return typeString;
    }

    public static SITOBJECTTYPE fromString(String typeString) {
        for (SITOBJECTTYPE objType : SITOBJECTTYPE.values()) {
            if (objType.typeString.equalsIgnoreCase(typeString)) {
                return objType;
            }
        }
        throw new IllegalArgumentException("Unknown object type: " + typeString);
    }

    @Override
    public String toString() {
        return typeString;
    }
}
