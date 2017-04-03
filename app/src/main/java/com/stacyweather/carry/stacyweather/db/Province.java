package com.stacyweather.carry.stacyweather.db;

import org.litepal.crud.DataSupport;

/**
 *
 */
public class Province extends DataSupport {
    private int id;
    private String provinceName;
    private int provinceInt;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getprovinceName() {
        return provinceName;
    }

    public void setprovinceName(String provinceName) {
        this.provinceName = provinceName;
    }

    public int getprovinceInt() {
        return provinceInt;
    }

    public void setprovinceInt(int provinceInt) {
        this.provinceInt = provinceInt;
    }




}
