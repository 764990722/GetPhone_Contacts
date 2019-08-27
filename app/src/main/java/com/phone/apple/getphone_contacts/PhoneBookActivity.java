package com.phone.apple.getphone_contacts;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.phone.apple.getphone_contacts.a_z.BeanPhoneDto;
import com.phone.apple.getphone_contacts.a_z.PinyinPhone;
import com.phone.apple.getphone_contacts.a_z.PinyinUtils;
import com.phone.apple.getphone_contacts.a_z.SideBar;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshHeader;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.SimpleMultiPurposeListener;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;
import com.yanzhenjie.permission.Permission;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 创 建 人 PeaceJay
 * 创建时间 2019/4/10
 * 类 描 述：通讯录
 */
public class PhoneBookActivity extends AppCompatActivity {

    @BindView(R.id.tv_title)
    TextView tv_title;
    @BindView(R.id.refreshLayout)
    SmartRefreshLayout refreshLayout;
    @BindView(R.id.lv_contact)
    ListView sortListView;
    @BindView(R.id.dialog)
    TextView dialog;
    @BindView(R.id.sidrbar)
    SideBar sideBar;
    @BindView(R.id.tv_qh_login)
    EditText tv_qh_login;
    @OnClick(R.id.lly_back)
    public void lly_back() {
        finish();
    }

    private List<BeanPhoneDto> phoneDtos;
    public static final String[] PHONE = new String[]{
            Permission.READ_CONTACTS};
    private PhoneNameAdapter adapter;
    private String [] stringId,stringName;
    private JSONArray jsonArray;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone_book_item);
        ButterKnife.bind(this);
        tv_title.setText("手机联系人");
        requestPermission(PHONE);
        refreshLayout.setOnMultiPurposeListener(new SimpleMultiPurposeListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                requestPermission(PHONE);
                tv_qh_login.setText("");
                refreshLayout.finishRefresh(500);
                refreshLayout.resetNoMoreData();//恢复没有更多数据的原始状态
            }
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

            }
            @Override
            public void onHeaderMoving(RefreshHeader header, boolean isDragging, float percent, int offset, int headerHeight, int maxDragHeight) {

            }
        });


        //根据输入框输入值的改变来过滤搜索
        tv_qh_login.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //当输入框里面的值为空，更新为原来的列表，否则为过滤数据列表
                filterData(s.toString());
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });
    }


    private void setAdapter() {
        Collections.sort(phoneDtos, new PinyinPhone());
        adapter = new PhoneNameAdapter(this,phoneDtos);
        sortListView.setAdapter(adapter);
        //listview点击
        sortListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        //设置右侧触摸监听
        sideBar.setOnTouchingLetterChangedListener(new SideBar.OnTouchingLetterChangedListener() {
            @Override
            public void onTouchingLetterChanged(String s) {
                //该字母首次出现的位置
                int position = adapter.getPositionForSection(s.charAt(0));
                if (position != -1) {
                    sortListView.setSelection(position);
                    //中间显示提示
                    sideBar.setTextView(dialog);
                }
            }
        });
    }



    /**
     * Request permissions.
     */
    private void requestPermission(String [] phone) {
        AndPermission.with(this)
                .runtime()
                .permission(phone)
                .rationale(new RuntimeRationale())
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> permissions) {
                        //成功
                        PhoneUtil phoneUtil = new PhoneUtil(PhoneBookActivity.this);
                        phoneDtos = phoneUtil.getPhone();
                        jsonArray = new JSONArray();
                        /*虚拟数据*/
                        ArrayList<String> qyidArrayList = new ArrayList<String>();
                        ArrayList<String> stringArrayList = new ArrayList<String>();

                        for (int i = 0; i < phoneDtos.size(); i++) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("name",phoneDtos.get(i).getName());
                            jsonObject.put("phone",phoneDtos.get(i).getTelPhone());
                            jsonArray.add(jsonObject);

                            String str = phoneDtos.get(i).getName();
                            if (!str.equals("")){
                                str = str.substring(0, 1);
                                if (PhoneUtil.isChinese(str)){
                                    stringArrayList.add(phoneDtos.get(i).getName());
                                }else {
                                    stringArrayList.add("Z("+phoneDtos.get(i).getName()+")");
                                }
                                qyidArrayList.add(phoneDtos.get(i).getTelPhone());
                            }

                        }
                        stringId = qyidArrayList.toArray(new String[qyidArrayList.size()]);
                        stringName = stringArrayList.toArray(new String[stringArrayList.size()]);
                        Logger.e("1111111", "onCLick: "+ jsonArray.toString());
                        phoneDtos = filledData(stringId,stringName);
                        //更新界面
                        setAdapter();
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(@NonNull List<String> permissions) {
                        //拒绝的权限需要手动开启
                        finish();
                    }
                })
                .start();
    }


    private List<BeanPhoneDto> filledData(String[] stringId,String[] stringName) {
        List<BeanPhoneDto> mSortList = new ArrayList<>();
        ArrayList<String> indexString = new ArrayList<>();
        for (int i = 0; i < stringId.length; i++) {
            BeanPhoneDto sortModel = new BeanPhoneDto();
            sortModel.setTelPhone(stringId[i]);
            sortModel.setName(stringName[i]);
            String pinyin = PinyinUtils.getPingYin(stringName[i]);
            String sortString = pinyin.substring(0, 1).toUpperCase();
            if (sortString.matches("[A-Z]")) {
                sortModel.setSortLetters(sortString.toUpperCase());
                if (!indexString.contains(sortString)) {
                    indexString.add(sortString);
                }
            }
            mSortList.add(sortModel);
        }
        Collections.sort(indexString);
        sideBar.setIndexText(indexString);
        return mSortList;
    }


    /**
     * 根据输入框中的值来过滤数据并更新ListView
     *
     * @param filterStr
     */
    private void filterData(String filterStr) {
        List<BeanPhoneDto> mSortList = new ArrayList<>();
        if (TextUtils.isEmpty(filterStr)) {
            mSortList = phoneDtos;
        } else {
            mSortList.clear();
            for (BeanPhoneDto sortModel : phoneDtos) {
                String name = sortModel.getName();
                if (name.toUpperCase().indexOf(filterStr.toUpperCase()) != -1
                        || PinyinUtils.getPingYin(name).toUpperCase().startsWith(filterStr.toUpperCase())) {
                    mSortList.add(sortModel);
                }
            }
        }
        // 根据a-z进行排序
        Collections.sort(mSortList, new PinyinPhone());
        adapter.updateListView(mSortList);
    }



}