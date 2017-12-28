<%@ page language="java" import="java.util.*" pageEncoding="UTF-8" import="java.sql.*"%>
<%@ page import="com.microsoft.sqlserver.jdbc.SQLServerDriver" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="utf-8">
    <title>${displayTitle}</title>
    <link rel="stylesheet" href="css/base.css" media="screen">
    <link rel="stylesheet" href="css/style.css" media="screen">
    <link rel="stylesheet" href="css/jqzoom.css" media="screen">
    <script type="text/javascript" src="http://www.just.edu.cn/statics/js/jquery.min.js"></script>
    <script type="text/javascript" src="http://www.just.edu.cn/statics/js/jquery-migrate-1.2.1.js"></script>
    <script type="text/javascript" src="http://www.just.edu.cn/statics/js/jqzoom.js"></script>
    <script type="text/javascript">
        var img_url="http://www.just.edu.cn/statics/img/";
    </script>
    <script type="text/javascript" src="http://www.just.edu.cn/statics/js/script.js"></script>
</head>
<body>
<div class="wrap" id="wrap">
    <div id="header">
        <div class="layout fn-clear">
            <h1><a class="logo fn-left" title="江苏科技大学">江苏科技大学</a></h1>
            <div class="moto2 fn-left">江苏科技大学</div>
            <div class="fn-right header-right">
                <ul>
                    <li><a href="http://hbd.just.edu.cn" target="_blank">航标灯</a></li>
                </ul>
            </div>
        </div>
    </div>
    <div class="listbody">
        <div class="layout fn-clear" >
            <!-- <div class="mainleft">
                <div class="catheader">详细内容</div>
                <div class="catlist">
                    <ul>
                    </ul>
                </div>
            </div>左边结束 -->
            <div class="mainright">
                <div class="position">您的位置：<a href="DividePageServlet">搜索结果</a> &gt; <a href="###">详细内容</a></div>
                <div class="cont_title" id="tit">${displayTitle}</div>
                <div class="cont_desc">点击数：${clickCount}&nbsp;&nbsp;&nbsp;&nbsp;              发布日期：${displayDate}            </div>
                <div class="cont_content" id="showText">
                    <p class="MsoNormal">
                        <b>作者：${displayAuthor}</b><b></b>
                    </p>
                    <p class="MsoNormal" style="text-indent:20.65pt;" align="left">
                          ${displayContent}
                    </p>

                </div>
            </div><!-- 右边结束 -->
        </div>
    </div>
    <footer class="myfooter">
        联系电话：xxxxxxxxxx 邮箱：JustGo@outlook.com 联系地址：江苏省镇江市江苏科技大学
        <br>
        版权所有：Copyright <em>©</em> 2016-2017, JustGo.com All Rights Reserved <a
            href="http://www.miibeian.gov.cn/" target="_blank">苏ICP备16009794号</a>
        <br>注：本站内容仅供学习使用，不承担任何法律责任。
    </footer>
</div>
<script type="text/javascript" src="http://v3.jiathis.com/code/jiathis_r.js?move=0" charset="utf-8"></script>
</body>
</html>