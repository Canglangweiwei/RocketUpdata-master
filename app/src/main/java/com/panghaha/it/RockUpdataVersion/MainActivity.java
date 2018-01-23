package com.panghaha.it.RockUpdataVersion;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.lzy.okhttputils.OkHttpUtils;
import com.lzy.okhttputils.callback.StringCallback;

import okhttp3.Call;
import okhttp3.Response;

@SuppressWarnings("ALL")
public class MainActivity extends AppCompatActivity {

    private UpdataDialog updataDialog;
    private String versionName = "";
    private int versioncode;
    private String oldVersion, newVersion, versionmsg, url, channelid;
    // 这里是测试用 登录检测版本号的服务器地址
    private String URL_UpdataVersion = "http://123.56.97.229:6080/Server/user/version.do";
    private TextView tvmsg, tvcode;
    private Data_updataVserion updataVserion;

    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);

        //初始化弹窗 布局 点击事件的id
        updataDialog = new UpdataDialog(this, R.layout.dialog_updataversion,
                new int[]{R.id.dialog_sure});

        oldVersion = getAppVersionName(this);

        initData();

        Button btn_show = (Button) findViewById(R.id.btn_show);
        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isFinishing()) {
                    updataDialog.show();
                }
            }
        });

        Button btn_test = (Button) findViewById(R.id.btn_test);
        btn_test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                test();
            }
        });
    }

    private void initData() {
        OkHttpUtils.get(URL_UpdataVersion)
                .params("ostype", "1")
                .execute(new StringCallback() {

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LogUtil.d("TAG", s);
                        updataVserion = JsonUtil.parseJsonToBean(s, Data_updataVserion.class);
                        newVersion = updataVserion.getVersion();
                        versionmsg = updataVserion.getMess();
                        url = updataVserion.getAddress();
                        String[] n = versionmsg.split("-");
                        versionmsg = n[0] + "\n" + n[1] + "\n" + n[2];
                        if (!newVersion.equals(oldVersion)) {/**新旧版本号对比*/
                            updataDialog.show();
                            tvmsg = (TextView) updataDialog.findViewById(R.id.updataversion_msg);
                            tvcode = (TextView) updataDialog.findViewById(R.id.updataversioncode);
                            tvcode.setText(newVersion);
                            tvmsg.setText(versionmsg);
                            updataDialog.setOnCenterItemClickListener(new UpdataDialog.OnCenterItemClickListener() {
                                @Override
                                public void OnCenterItemClick(UpdataDialog dialog, View view) {
                                    switch (view.getId()) {
                                        case R.id.dialog_sure:
                                            /** 调用系统自带的浏览器去下载最新apk */
                                            Intent intent = new Intent();
                                            intent.setAction("android.intent.action.VIEW");
                                            Uri content_url = Uri.parse(url);
                                            intent.setData(content_url);
                                            startActivity(intent);
                                            break;
                                    }
                                    updataDialog.dismiss();
                                }
                            });
                        }
                    }
                });
    }

    /**
     * 返回当前程序版本名  build.gradle里的
     */
    private String getAppVersionName(Context context) {
        try {
            // ---get the package info---
            PackageManager pm = context.getPackageManager();
            PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);
            versionName = pi.versionName;
            versioncode = pi.versionCode;
            LogUtil.d("versionName:---" + versionName, "versioncode:---" + versioncode);
            if (versionName == null || versionName.length() <= 0) {
                return "";
            }
        } catch (Exception e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

    private void test() {
        String url = "http://0.89892528.cn:8700/test/jsonData.php";
        OkHttpUtils.post(url)
                .params("type", 1)
                .execute(new StringCallback() {

                    @Override
                    public void onSuccess(String s, Call call, Response response) {
                        LogUtil.d("onSuccess", s);
                        textView.setText(s);
                    }
                });
    }
}
