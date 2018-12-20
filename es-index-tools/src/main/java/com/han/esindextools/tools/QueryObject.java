package com.han.esindextools.tools;

import com.hivescm.common.serialize.api.json.GsonSerialize;

import java.io.Serializable;

/**
 * @Author: hanyf
 * @Description:
 * @Date: 2018/8/13 18:09
 */
public class QueryObject implements Serializable {

    private static final long serialVersionUID = 8503270181634361132L;
    private String flag;
    private String key;
    private Object value;
    private MultiQueryObject multiQueryObject;

    public QueryObject()
    {
    }

    public QueryObject(String flag, String key, Object value)
    {
        this.flag = flag;
        this.key = key;
        this.value = value;
    }

    public QueryObject(MultiQueryObject multiQueryObject) {
        this.multiQueryObject = multiQueryObject;
    }

    public String getFlag() {
        return this.flag;
    }

    public String getKey() {
        return this.key;
    }

    public Object getValue() {
        return this.value;
    }

    public MultiQueryObject getMultiQueryObject() {
        return this.multiQueryObject;
    }

    public String toString() {
        return GsonSerialize.INSTANCE.encode(this);
    }
}
