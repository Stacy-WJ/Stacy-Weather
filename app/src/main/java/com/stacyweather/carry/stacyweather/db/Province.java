package com.stacyweather.carry.stacyweather.db;

import org.litepal.crud.DataSupport;

/**
 * @author Administrator
 * @time 2017/3/29 22:33
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 *
 */
public class Province extends DataSupport {
    private int id;
    private String provinceNaeme;
    private int rovinceCode;
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getProvinceNaeme() {
        return provinceNaeme;
    }

    public void setProvinceNaeme(String provinceNaeme) {
        this.provinceNaeme = provinceNaeme;
    }

    public int getRovinceCode() {
        return rovinceCode;
    }

    public void setRovinceCode(int rovinceCode) {
        this.rovinceCode = rovinceCode;
    }




}
