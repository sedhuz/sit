package com.infinull.sit.object;

import com.infinull.sit.enums.SITOBJECTTYPE;
import com.infinull.sit.util.Sha;

public abstract class SitObject {

    protected SITOBJECTTYPE type;

    SitObject() {
    }

    SitObject(SITOBJECTTYPE type) {
        this.type = type;
    }

    public SITOBJECTTYPE getType() {
        return type;
    }

    public void setType(SITOBJECTTYPE type) {
        this.type = type;
    }

    public Sha getSha() {
        return Sha.computeSha(serialize());
    }

    public abstract String getFullContent();

    public abstract int getSize();

    public abstract String getContent();

    public abstract byte[] serialize();

    @Override
    public String toString() {
        return "SitObject{" +
                "type=" + type +
                ", sha='" + getSha() + '\'' +
                '}';
    }
}
