package com.stacyweather.carry.stacyweather;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.stacyweather.carry.stacyweather.db.City;
import com.stacyweather.carry.stacyweather.db.County;
import com.stacyweather.carry.stacyweather.db.Province;
import com.stacyweather.carry.stacyweather.util.HttpUtil;
import com.stacyweather.carry.stacyweather.util.Utility;

import org.litepal.crud.DataSupport;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;


public class ChooseAreaFragment extends android.support.v4.app.Fragment{
    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVIE_COUNTY = 2;

    private ProgressDialog mProgressDialog;
    private TextView titleText;
    private Button backButton;
    private ListView mListView;
    private ArrayAdapter<String> mAdapter;
    private List<String> dataList = new ArrayList<>();
//    省列表
    private List<Province> mProvinceList;
//    市列表
    private  List<City> mCityList;
//    县列表
    private List<County> mCountyList;
//    选中的省
    private  Province selectedProvince;
//    选中的市
    private City selectedCity;
//    当前选中的级别
    private int currentLevel;

    private static final String TAG = "ChooseAreaFragment";
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle
            savedInstanceState) {
        View view = inflater.inflate(R.layout.choose_area,container,false);
        titleText= (TextView) view.findViewById(R.id.title_text);
        backButton = (Button) view.findViewById(R.id.title_bt);
        mListView = (ListView) view.findViewById(R.id.list_view);
        mAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1,dataList);
        mListView.setAdapter(mAdapter);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (currentLevel==LEVEL_PROVINCE){
                    selectedProvince = mProvinceList.get(position);
                    queryCitys();
                }else if (currentLevel == LEVEL_CITY){
                    selectedCity = mCityList.get(position);
                    queryCounties();
                }
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (currentLevel==LEVIE_COUNTY){
                    queryCitys();
                }else if (currentLevel == LEVEL_CITY){
                    queryProvinces();
                }
            }
        });
                 queryProvinces();
    }

    private void queryProvinces() {
        //查询省信息
        titleText.setText("中国");
        backButton.setVisibility(View.GONE);
        mProvinceList = DataSupport.findAll(Province.class);
        if (mProvinceList.size()>0){
            dataList.clear();
            for (Province province :mProvinceList ) {
            	dataList.add(province.getprovinceName());
            }
            mAdapter.notifyDataSetChanged();
            mListView.setSelection(0);
            currentLevel=LEVEL_PROVINCE;
        }else{
            String address = "http://guolin.tech/api/china";
            queryFromServer(address,"province");
        }
    }

    private void queryFromServer(String address, final String type) {
        showProgressDialog();
        HttpUtil.sendOkHttpRequest(address, new Callback() {
            @Override
            public void onResponse(Call call, Response response) throws IOException {
             String responseText =response.body().string();
                boolean result = false;
                if ("province".equals(type)){
                    result = Utility.handleProvinceResponse(responseText);
                    Log.i(TAG, "输出结果"+result);
                }else if("city".equals(type)){
                    result=Utility.handleCityResponse(responseText,selectedProvince.getId());
                }else if ("county".equals(type)){
                    result = Utility.handleCountyResponse(responseText,selectedCity.getId());
                }
                if (result){
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            closeProgressDialog();
                            if ("province".equals(type)){
                                queryProvinces();
                            }else if("city".equals(type)){
                                queryCitys();
                            }else if ("county".equals(type)){
                                queryCounties();
                            }
                        }
                    });
                }
            }
            @Override
            public void onFailure(Call call, IOException e) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        closeProgressDialog();
                        Toast.makeText(getContext(),"加载失败",Toast.LENGTH_SHORT).show();
                    }
                });
            }

        });
    }

    private void closeProgressDialog() {
        if (mProgressDialog!=null){
            mProgressDialog.dismiss();
        }
    }

    private void showProgressDialog() {
        if (mProgressDialog==null){
            mProgressDialog = new ProgressDialog(getActivity());
            mProgressDialog.setMessage("正在加载");
            mProgressDialog.setCanceledOnTouchOutside(false);
        }
        mProgressDialog.show();
    }

    private void queryCounties() {
        titleText.setText(selectedCity.getCityName());
        backButton.setVisibility(View.VISIBLE);
        mCountyList=DataSupport.where("cityid=?",String.valueOf(selectedCity.getId())).find(County.class);
        if (mCountyList.size()>0){
            dataList.clear();
            for (County county :mCountyList ) {
            	dataList.add(county.getCountyName());
            }
            mAdapter.notifyDataSetChanged();
            mListView.setSelection(0);
            currentLevel=LEVIE_COUNTY;
        }else{
            int provicecode=selectedProvince.getprovinceCode();
            int citycode = selectedCity.getCityCode();
            String address = "http://guolin.tech/api/china/"+provicecode+"/"+citycode;
            queryFromServer(address,"county");
        }
    }

    private void queryCitys() {
        titleText.setText(selectedProvince.getprovinceName());
        backButton.setVisibility(View.VISIBLE);
        mCityList=DataSupport.where("provinceid=?",String.valueOf(selectedProvince.getId())).find(City.class);
        if (mCityList.size()>0){
            dataList.clear();
            for (City city  :mCityList ) {
            	dataList.add(city.getCityName());
            }
            mAdapter.notifyDataSetChanged();
            mListView.setSelection(0);
            currentLevel=LEVEL_CITY;
            }else{
            int proviceCode = selectedProvince.getprovinceCode();
            String address = "http://guolin.tech/api/china/"+proviceCode;
            queryFromServer(address,"city");
        }
    }


}
