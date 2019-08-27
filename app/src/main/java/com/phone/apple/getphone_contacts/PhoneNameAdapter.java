package com.phone.apple.getphone_contacts;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.SectionIndexer;
import android.widget.TextView;
import android.widget.Toast;
import com.phone.apple.getphone_contacts.a_z.BeanPhoneDto;
import java.util.List;

import butterknife.internal.Utils;

public class PhoneNameAdapter extends BaseAdapter implements SectionIndexer {
    private List<BeanPhoneDto> list;
    private Context mContext;
    private int checked = -1;//初始选择为-1，position没有-1嘛，那就是谁都不选咯
//    private IWXAPI wxapi;
    String DownPage = "https://share.vspfire.com/share/view/download";

    public PhoneNameAdapter(Context mContext, List<BeanPhoneDto> list) {
        this.mContext = mContext;
        this.list = list;
    }

    /**
     * 当ListView数据发生变化时,调用此方法来更新ListView
     *
     * @param list
     */
    public void updateListView(List<BeanPhoneDto> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void setChecked(int checked) {//设定一个选中的标志位，在activity中传入值。
        this.checked = checked;
    }


    public int getCount() {
        return this.list.size();
    }

    public Object getItem(int position) {
        return list.get(position);
    }

    public long getItemId(int position) {
        return position;
    }

    public View getView(final int position, View view, ViewGroup arg2) {
        ViewHolder viewHolder = null;
        final BeanPhoneDto content = list.get(position);
        if (view == null) {
            viewHolder = new ViewHolder();
            view = LayoutInflater.from(mContext).inflate(R.layout.item_phonename_content, null);
            viewHolder.tvLetter = view.findViewById(R.id.tv_catagory);
            viewHolder.tv_name = view.findViewById(R.id.tv_name);
            viewHolder.tv_phone = view.findViewById(R.id.tv_phone);
            viewHolder.tv_name_sx = view.findViewById(R.id.tv_name_sx);
            viewHolder.linear_item = view.findViewById(R.id.linear_item);
            viewHolder.tv_wxin_invite = view.findViewById(R.id.tv_wxin_invite);
            viewHolder.tv_SMS_invite = view.findViewById(R.id.tv_SMS_invite);
            view.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) view.getTag();
        }

        int section = getSectionForPosition(position);
        if (position == getPositionForSection(section)) {
            viewHolder.tvLetter.setVisibility(View.VISIBLE);
            viewHolder.tvLetter.setText(content.getSortLetters());
        } else {
            viewHolder.tvLetter.setVisibility(View.GONE);
        }
        viewHolder.tv_name.setText(content.getName());
        viewHolder.tv_phone.setText(content.getTelPhone());
        String str = content.getName();
        if (!str.equals("")){
            str = str.substring(0, 1);
            viewHolder.tv_name_sx.setText(str);
        }else {
            viewHolder.tv_name_sx.setText("Z");
        }

        viewHolder.tv_wxin_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                isWeiXinAppInstall(AppValue.DownPage,"【血源派】",DownPage + "邀请你加入血源派，融入宗亲社区，建立家庭圈子，与至爱亲友常相伴！" + DownPage,SendMessageToWX.Req.WXSceneSession);
            }
        });

        viewHolder.tv_SMS_invite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //"smsto:xxx" xxx是可以指定联系人的
                Uri smsToUri = Uri.parse("smsto:" + content.getTelPhone());
                Intent intent = new Intent(Intent.ACTION_SENDTO, smsToUri);
                //"sms_body"必须一样，smsbody是发送短信内容content
                intent.putExtra("sms_body", "【血源派】" + "XXX邀请你加入血源派，融入宗亲社区，建立家庭圈子，与至爱亲友常相伴！" + DownPage);
                mContext.startActivity(intent);
            }
        });
        return view;
    }



    final static class ViewHolder {
        TextView tv_name,tv_phone,tv_name_sx;
        TextView tv_wxin_invite,tv_SMS_invite;
        TextView tvLetter;
        LinearLayout linear_item;
    }

    public int getSectionForPosition(int position) {
        return list.get(position).getSortLetters().charAt(0);
    }

    public int getPositionForSection(int section) {
        for (int i = 0; i < getCount(); i++) {
            String sortStr = list.get(i).getSortLetters();
            char firstChar = sortStr.toUpperCase().charAt(0);
            if (firstChar == section) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public Object[] getSections() {
        return null;
    }


//    /**
//     * 判断是否安装微信
//     * 地址   标题   内容   类型
//     */
//    private void isWeiXinAppInstall(String url, String title, String desc,int type) {
//        if ( wxapi == null)
//            wxapi = WXAPIFactory.createWXAPI(App.getContext(), AppValue.WxAppId);
//        if (wxapi.isWXAppInstalled()) {
//            shareUrlToWx(url,title,desc,type);
//        } else {
//            Toast.makeText(App.getContext(), "未安装微信无法分享", Toast.LENGTH_SHORT).show();
//        }
//    }
//
//    /**
//     * 分享url地址
//     * @param url            地址
//     * @param title          标题
//     * @param desc           描述
//     * @param wxSceneSession 类型
//     */
//    private static final int THUMB_SIZE = 150;
//    public void shareUrlToWx(String url, String title, String desc, final int wxSceneSession) {
//        WXWebpageObject webpage = new WXWebpageObject();
//        webpage.webpageUrl = url;
//        WXMediaMessage msg = new WXMediaMessage(webpage);
//        msg.title = title;
//        msg.description = desc;
//        Bitmap bmp = BitmapFactory.decodeResource(App.getContext().getResources(), R.drawable.app_logo);
//        Bitmap thumbBmp = Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true);
//        bmp.recycle();
//        msg.thumbData = Utils.bmpToByteArray(thumbBmp, true);
//        SendMessageToWX.Req req = new SendMessageToWX.Req();
//        req.transaction = buildTransaction("webpage");
//        req.message = msg;
//        req.scene = wxSceneSession;
//        wxapi.sendReq(req);
//    }
//    private String buildTransaction(final String type) {
//        return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
//    }



}