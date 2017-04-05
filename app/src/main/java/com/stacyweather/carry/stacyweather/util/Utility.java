package com.stacyweather.carry.stacyweather.util;

import android.text.TextUtils;

import com.stacyweather.carry.stacyweather.db.City;
import com.stacyweather.carry.stacyweather.db.County;
import com.stacyweather.carry.stacyweather.db.Province;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author Administrator
 * @time 2017/3/30 0:03
 * @des ${TODO}
 * @updateAuthor $Author$
 * @updateDate $Date$
 * @updateDes ${TODO}
 */
public class Utility {
    /*
    解析和处理省级服务器返回的省级数据
     */
    public static boolean handleProvinceResponse(String response) {
        if (!TextUtils.isEmpty(response)) {
            try {
                JSONArray allProvinces = new JSONArray(response);
                for (int i = 0; i < allProvinces.length(); i++) {
                    JSONObject provinceObject = allProvinces.getJSONObject(i);
                    Province province = new Province();
                    province.setprovinceName(provinceObject.getString("name"));
                    province.setprovinceCode(provinceObject.getInt("id"));
                    province.save();
                }return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
return false;
    }
    /*
    解析和处理市级服务器返回的省级数据
     */
    public  static boolean handleCityResponse(String response , int proviceId){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray allCityies = new JSONArray(response);
                for (int i = 0; i < allCityies.length(); i++) {
                    JSONObject cityObject = allCityies.getJSONObject(i);
                    City city = new City();
                    city.setCityName(cityObject.getString("name"));
                    city.setCityCode(cityObject.getInt("id"));
                    city.setProvinceId(proviceId);
                    city.save();
                }return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }return false;
    }
    /*
    解析和处理县级服务器返回的省级数据
     */
    public static boolean handleCountyResponse(String response,int cityId){
        if (!TextUtils.isEmpty(response)){
            try {
                JSONArray allCounties = new JSONArray(response);
                for (int i = 0; i < allCounties.length(); i++) {
                    JSONObject countyObject = allCounties.getJSONObject(i);
                    County county = new County();
                    county.setCountyName(countyObject.getString("name"));
                    county.setId(countyObject.getInt("id"));
                    county.setCityId(cityId);
                    county.save();
                }return true;
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return false;
    }
}