package com.hivescm.estools.tools;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class MultiQueryObject implements Serializable {

    private static final long serialVersionUID = -5777792620149644493L;

    private String flag;

    private List<QueryObject> baseObjectList = new ArrayList<>();

    public MultiQueryObject() {
    }

    public MultiQueryObject(String flag, List<QueryObject> baseObjectList) {
        this.flag = flag;
        this.baseObjectList = baseObjectList;
    }

    public String getFlag() {
        return flag;
    }

    public List<QueryObject> getQueryObjectList() {
        return baseObjectList;
    }
}
