package com.phone.apple.getphone_contacts;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;


import com.phone.apple.getphone_contacts.a_z.BeanPhoneDto;

import java.util.ArrayList;
import java.util.List;

/**
 * 创 建 人 PeaceJay
 * 创建时间 2019/8/26
 * 类 描 述：获取手机通讯录
 */
public class PhoneUtil {

    // 号码
    public final static String NUM = ContactsContract.CommonDataKinds.Phone.NUMBER;
    // 联系人姓名
    public final static String NAME = ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME;

    //上下文对象
    private Context context;
    //联系人提供者的uri
    private Uri phoneUri = ContactsContract.CommonDataKinds.Phone.CONTENT_URI;

    public PhoneUtil(Context context){
        this.context = context;
    }

    //获取所有联系人
    public List<BeanPhoneDto> getPhone(){
        List<BeanPhoneDto> phoneDtos = new ArrayList<>();
        ContentResolver cr = context.getContentResolver();
        Cursor cursor = cr.query(phoneUri,new String[]{NUM,NAME},null,null,null);
        while (cursor.moveToNext()){
            String name = cursor.getString(cursor.getColumnIndex(NAME));
            String phone = cursor.getString(cursor.getColumnIndex(NUM));
//            phone = phone.replace("-", "");
            phone = phone.replace(" ", "");
            BeanPhoneDto phoneDto = new BeanPhoneDto(name,phone);
            phoneDtos.add(phoneDto);
        }
        return phoneDtos;
    }

    /**
     * 判断是否为汉字
     *
     * @param string
     * @return
     */

    public static boolean isChinese(String string) {
        int n = 0;
        for (int i = 0; i < string.length(); i++) {
            n = (int) string.charAt(i);
            if (!(19968 <= n && n < 40869)) {
                return false;
            }
        }
        return true;
    }

}
