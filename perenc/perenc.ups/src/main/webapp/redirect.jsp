<%@ page language="java" import="java.util.*" pageEncoding="utf-8" %>

<!DOCTYPE HTML>
<html>
<head>
    <meta name="viewport" content="width=device-width,initial-scale=1,user-scalable=0">
    <script type="text/javascript">
        var url = "${toUrl}";
        if (url) {
            location = url;
        } else {
            console.log("跳转地址不存在！");
        }
    </script>
</head>

<body>
<div style="margin: 3em; text-align:center; font-size:1.5em;">
    请稍候...
</div>
</body>
</html>
