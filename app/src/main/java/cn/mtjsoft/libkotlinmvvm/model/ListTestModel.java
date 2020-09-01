package cn.mtjsoft.libkotlinmvvm.model;

import java.io.Serializable;

/**
 * @author mtj
 * @Package cn.mtjsoft.libkotlinmvvm.model
 * @date 2020-09-01 10:10:12
 */

public class ListTestModel implements Serializable {
    private String numSrt;

    public String getNumSrt() {
        return numSrt;
    }

    public void setNumSrt(String numSrt) {
        this.numSrt = numSrt;
    }
}