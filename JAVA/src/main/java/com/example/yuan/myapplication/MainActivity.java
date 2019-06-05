package com.example.yuan.myapplication;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.alipay.sdk.app.PayTask;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

import static com.example.yuan.myapplication.Sign.signForInspiry;


public class MainActivity extends AppCompatActivity {
    private Dialog mDialog;
    TextView content;
    Button test;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        test = (Button) findViewById(R.id.test);
        content = (TextView) findViewById(R.id.content);


        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //进度条
                mDialog = DialogThridUtils.showWaitDialog(MainActivity.this, "请求中...", false, true);
                getPayUrl();
            }
        });
    }


    public void getPayUrl() {
        HttpUtils httpUtils = new HttpUtils(15000);

        final RequestParams params = new RequestParams();

        //以下参数以老牛平台为例

        params.addBodyParameter("appid", "88888888");//必填。您的商户唯一标识
        params.addBodyParameter("pay_type", "alipay_red");//必填。wechat:微信、alipay:支付宝，支付宝红包。alipay_red
        params.addBodyParameter("amount", "0.01");//必填。交易金额,必填。单位：元。精确小数点后2位,例子：10.00
//        params.addBodyParameter("return_type", "app");//非必填。请求支付标识,app、PC、mobile，
        params.addBodyParameter("callback_url", "https://www.baidu.com/");//必填。回调地址
//        params.addBodyParameter("success_url", "");//非必填。支付成功后网页自动跳转地址
//        params.addBodyParameter("error_url", "");//非必填。支付失败时，或支付超时后网页自动跳转地址
//        params.addBodyParameter("out_uid", "");//非必填。用户网站的请求支付用户信息，可以是帐号也可以是数据库的ID

        String orderNo="" + (int) (Math.random() * 1000000);
        params.addBodyParameter("out_trade_no",orderNo );//必填。商户订单号
        params.addBodyParameter("version", "v1.0");//必填。接口版本号


        SortedMap<Object,Object> params1 = new TreeMap<Object,Object>();
        params1.put("appid","88888888");
        params1.put("pay_type","alipay_red");
        params1.put("amount","0.01");
        params1.put("callback_url","https://www.baidu.com/");
        params1.put("out_trade_no",orderNo);
        params1.put("version","v1.0");



        params.addBodyParameter("sign", signForInspiry(params1,"sF3SUymkCzQnUGnqRugUFgE2aqFBaYXq"));//必填。





        httpUtils.send(HttpRequest.HttpMethod.POST, "http://api.huohuohuo.com/gateway/index/unifiedorder?format=json", params, new RequestCallBack<String>() {

            @Override
            public void onFailure(HttpException arg0, String arg1) {
                System.out.println("接口返回错误" + arg1);
                DialogThridUtils.closeDialog(mDialog);
                Toast.makeText(MainActivity.this, arg1, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onSuccess(ResponseInfo<String> arg0) {
                DialogThridUtils.closeDialog(mDialog);

                final String result = arg0.result;
                System.out.println("接口返回信息" + result);


                (MainActivity.this).runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //已在主线程中，可以更新UI
                        content.setText("请求结果>   \n" + result);
                    }
                });

            }
        });
    }




    public void test(){
        SortedMap<Object,Object> params1 = new TreeMap<Object,Object>();
        params1.put("callbacks","CODE_SUCCESS");
        params1.put("appid","10222021");
        params1.put("pay_type","wechat");
        params1.put("success_url","http://xsjj.com/success/url");
        params1.put("error_url","http://xsjj.com/error/url");
        params1.put("out_trade_no","C0000000000123");
        params1.put("amount","100.00");

        String NowSign=signForInspiry(params1,"sF3SUymkCzQnUGnqRugUFgE2aqFBaYXq");
        //模拟请求到了后台，后台验签
        notifyServer("CODE_SUCCESS","10222021","wechat","http://xsjj.com/success/url",
                "http://xsjj.com/error/url","C0000000000123","100.00",NowSign );
    }


    //验签，服务端付款成功回调检查签名
    /**
     * 参数示例
     *{ 
         "callbacks":'CODE_SUCCESS', 
         "appid": '10222021', 
         "pay_type": 'wechat', 
         "success_url" : 'http://xsjj.com/success/url', 
         "error_url" : 'http://xsjj.com/error/url', 
         "out_trade_no" : 'C0000000000123', 
         "amount" : '100.00', 
         "sign" : 'lahdhadwuworwenkhhsofsernfknfsufddrlfdlogo9drtrl', 
     }

     */
    public  String notifyServer(String callbacks, //
                                String appid, //
                                String pay_type, //
                                String success_url, //
                                String error_url, //
                                String out_trade_no, //
                                String amount,
                          String sign
                               ) {

        SortedMap<Object,Object> params1 = new TreeMap<Object,Object>();
        params1.put("callbacks",callbacks);
        params1.put("appid",appid);
        params1.put("pay_type",success_url);
        params1.put("success_url",error_url);
        params1.put("error_url",out_trade_no);
        params1.put("out_trade_no",out_trade_no);
        params1.put("amount",amount);

         String serverNowSign=signForInspiry(params1,"sF3SUymkCzQnUGnqRugUFgE2aqFBaYXq");

        System.out.println("当前模拟服务器接受参数验签结果："+serverNowSign.equals(sign));

        Toast.makeText(MainActivity.this, "当前模拟服务器接受参数验签结果："+serverNowSign.equals(sign), Toast.LENGTH_SHORT).show();
         if(serverNowSign.equals(sign)){
             return "success";
         }else{
             return "error";
         }


    }

}
