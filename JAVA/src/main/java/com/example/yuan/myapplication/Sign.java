package com.example.yuan.myapplication;

import java.math.BigInteger;
import java.net.URISyntaxException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.TreeSet;

public class Sign {
//    public static void main(String[] args) throws URISyntaxException {
//        SortedMap<Object,Object> params = new TreeMap<Object,Object> ();
//
//        params.put("id","123");
//
//        params.put("name","123");
//
//        params.put("age","123");
//
//
//        System.out.println(">>>>>>>"+signForInspiry(params,"123456"));
//
//    }


    /**
     * 生成签名；
     *
     * @param params
     * @return
     */
    static public String signForInspiry(Map params, String key) {

        StringBuffer sbkey = new StringBuffer();
        Set es = params.entrySet();
        Iterator it = es.iterator();

        while (it.hasNext()) {
            Map.Entry entry = (Map.Entry) it.next();
            String k = (String) entry.getKey();
            Object v = entry.getValue();
            //空值不传递，不参与签名组串
            if (null != v && !"".equals(v)) {
                sbkey.append(k + "=" + v + "&");
            }
        }


        sbkey = sbkey.append("key=" + key);
        System.out.println("当前key:"+sbkey);
        //MD5加密,结果转换为大写字符
        String sign = MD5(sbkey.toString()).toUpperCase();
        return sign;
    }

    /**
     * 对字符串进行MD5加密
     *
     * @param str 需要加密的字符串
     * @return 小写MD5字符串 32位
     */
    static public String MD5(String str) {
        MessageDigest digest;

        try {
            digest = MessageDigest.getInstance("MD5");
            digest.update(str.getBytes());
            return new BigInteger(1, digest.digest()).toString(16);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
            return null;
        }
    }

}
