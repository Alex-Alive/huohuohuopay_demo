<?php
error_reporting(0);
//载入签名算法库
include ('sign.php');
//当前界面是进行网关参数获取以及发起POST请求
//下面参数均为商户自定义，可自行修改

//请求支付地址
$api = 'http://api.huohuohuo.com/gateway/index/unifiedorder';
//商户appid->到平台首页自行复制粘贴
$appid = '1068619';

//商户密钥，到平台首页自行复制粘贴，该参数无需上传，用来做签名验证和回调验证，请勿泄露
$app_key = 'oiKmriOFTOGVfIJxD5YGyKrruHDhcc0w';

//订单号码->这个是四方网站发起订单时带的订单信息，一般为用户名，交易号，等字段信息
$out_trade_no = date("YmdHis") . mt_rand(10000, 99999);
//支付类型alipay、wechat
$pay_type = 'alipay';
//支付金额
$amount = sprintf("%.2f",1.00);
//异步通知接口url->用作于接收成功支付后回调请求
$callback_url = 'http://aa.huohuohuo.com/callback_demo.php';
//支付成功后自动跳转url
$success_url = 'http://aa.huohuohuo.com';
//支付失败或者超时后跳转url
$error_url = 'http://aa.huohuohuo.com';
//版本号
$version = 'v1.1';
//用户网站的请求支付用户信息，可以是帐号也可以是数据库的ID:858887906
$out_uid = '';

$data = [
    'appid'        => $appid,
    'pay_type'     => $pay_type,
    'out_trade_no' => $out_trade_no,
    'amount'       => $amount,
    'callback_url' => $callback_url,
    'success_url'  => $success_url,
    'error_url'    => $error_url,
    'version'      => $version,
    'out_uid'      => $out_uid,
];

//拿APPKEY与请求参数进行签名
$sign = getSign($app_key, $data);

?>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
	<title>接口调用</title>
</head>
<body>
<form action="<?php echo $api;?>" method="post" id="frmSubmit">
    <input type="hidden" name="appid" value="<?php echo $appid;?>" />
    <input type="hidden" name="pay_type" value="<?php echo $pay_type;?>"/>
    <input type="hidden" name="out_trade_no" value="<?php echo $out_trade_no;?>"/>
    <input type="hidden" name="sign" value="<?php echo $sign;?>"/>
    <input type="hidden" name="callback_url" value="<?php echo $callback_url;?>" />
    <input type="hidden" name="success_url" value="<?php echo $success_url;?>" />
    <input type="hidden" name="error_url" value="<?php echo $error_url;?>" />
    <input type="hidden" name="amount" value="<?php echo $amount;?>" />
    <input type="hidden" name="version" value="<?php echo $version;?>" />
    <input type="hidden" name="out_uid" value="<?php echo $out_uid;?>" />
</form>
<script type="text/javascript">
document.getElementById("frmSubmit").submit();
</script>
</body>
</html>