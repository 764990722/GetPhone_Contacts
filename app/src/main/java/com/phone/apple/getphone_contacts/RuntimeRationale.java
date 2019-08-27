/*
 * Copyright © Yan Zhenjie
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.phone.apple.getphone_contacts;

import android.app.Dialog;
import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

import com.yanzhenjie.permission.Permission;
import com.yanzhenjie.permission.Rationale;
import com.yanzhenjie.permission.RequestExecutor;

import java.util.List;


/**
 * 创 建 人 PeaceJay
 * 创建时间 2019/3/19
 * 类 描 述：启动时动态权限弹窗
 */
public final class RuntimeRationale implements Rationale<List<String>> {

    private Dialog mDialog;

    @Override
    public void showRationale(Context context, List<String> permissions, final RequestExecutor executor) {
        List<String> permissionNames = Permission.transformText(context, permissions);
//        String message = context.getString(R.string.message_permission_rationale, TextUtils.join("\n", permissionNames));
        String message = "请开启"+TextUtils.join("", permissionNames)+"权限，以正常使用血源派功能";

        mDialog = new Dialog(context, R.style.dialog1);
        LayoutInflater in = LayoutInflater.from(context);
        View viewDialog = in.inflate(R.layout.prompt_dialog, null);
        TextView tv_name = viewDialog.findViewById(R.id.tv_name);
        tv_name.setText(message);

        viewDialog.findViewById(R.id.tv_out).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
//                executor.cancel();
            }
        });
        viewDialog.findViewById(R.id.tv_suer).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                executor.cancel();
                mDialog.dismiss();
                executor.execute();
            }
        });

//        viewDialog.setBackgroundColor(0x5f000000);
        mDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        //禁止物理返回
        mDialog.setCancelable(false);
        mDialog.setContentView(viewDialog);
        /*触摸屏幕其它区域，就会让这个progressDialog消失  消失true反之false*/
        mDialog.setCanceledOnTouchOutside(false);
        mDialog.show();
    }


}