package org.spike.model;

public class PersonDao {
    private String nm;
    private String fn;
    private String ci;
    private int a;

    public PersonDao() {
    }

    public PersonDao(String nm, String fn, String ci, int a) {
        this.nm = nm;
        this.fn = fn;
        this.ci = ci;
        this.a = a;
    }

    public String getCi() {
        return ci;
    }

    public void setCi(String ci) {
        this.ci = ci;
    }

    public String getNm() {
        return nm;
    }

    public void setNm(String nm) {
        this.nm = nm;
    }

    public String getFn() {
        return fn;
    }

    public void setFn(String fn) {
        this.fn = fn;
    }

    public int getA() {
        return a;
    }

    public void setA(int a) {
        this.a = a;
    }
}
