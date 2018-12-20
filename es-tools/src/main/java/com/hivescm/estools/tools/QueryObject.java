package com.hivescm.estools.tools;

import com.hivescm.common.serialize.api.json.GsonSerialize;

import java.io.Serializable;

public class QueryObject implements Serializable {

    private static final long serialVersionUID = -5777792620149644491L;

    private String flag;

    private String key;

    private Object value;

    private MultiQueryObject multiQueryObject;

    public QueryObject() {
    }

    public QueryObject(String flag, String key, Object value) {
        this.flag = flag;
        this.key = key;
        this.value = value;
    }

    public QueryObject(MultiQueryObject multiQueryObject) {
        this.multiQueryObject = multiQueryObject;
    }

    public String getFlag() {
        return flag;
    }

    public String getKey() {
        return key;
    }

    public Object getValue() {
        return value;
    }

    public MultiQueryObject getMultiQueryObject() {
        return multiQueryObject;
    }

    public String toString() {
        return GsonSerialize.INSTANCE.encode(this);
    }
}