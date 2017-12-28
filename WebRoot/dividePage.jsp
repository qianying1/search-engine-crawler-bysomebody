<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"
	import="java.sql.*"%>
<%@ page import="com.microsoft.sqlserver.jdbc.SQLServerDriver"%>
<%@taglib uri="http://java.sun.com/jsp/jstl/core" prefix="QZQ"%>
<%
	String path = request.getContextPath();
	String basePath = request.getScheme() + "://"
	+ request.getServerName() + ":" + request.getServerPort()
	+ path + "/";
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<html>
<head>
<base href="<%=basePath%>">
    <link rel="stylesheet" href="http://cdn.bootcss.com/bootstrap/3.3.0/css/bootstrap.min.css">
    <link rel="stylesheet" href="css/search-result-css.css">
    <title>搜索结果</title>
</head>

<body>
	<header>
    <nav class="navbar navbar-default navbar-fixed-top" role="navigation">
        <div class="container">
            <div class="navbar-header">
                <button type="button" class="navbar-toggle collapsed" data-toggle="collapse" data-target="#navbar"
                        aria-expanded="false" aria-controls="navbar">
                    <span class="sr-only">Toggle navigation</span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                    <span class="icon-bar"></span>
                </button>
                <a class="navbar-brand" href="index.html"><img width="135px" src="imgs/bg.png"></a>
            </div>
            <div id="navbar" class="navbar-collapse collapse">
                <ul class="nav navbar-nav">
                  <div style="width: 500px;float: left;margin-top: 7px" id="search-group">
                    <div class="input-group">
					   <input id="searchName" type="text" class="form-control"> 
					     <span class="input-group-btn">
						   <button id="submitBtn" class="btn btn-info" type="button">Go!</button>
					     </span> 
				     </div>
                   </div>
                </ul>
                <!-- <ul class="nav navbar-nav navbar-right">
                    <li><a href="index.html">返回首页</a></li>
                </ul> -->
            </div>
        </div>
    </nav>
</header>
<div class="container mainWrap">
    <div class="mainWrap-left">
        <div class="main-top">
            <div class="search-sort">
                <a href="SortNewsServlet?searchName=${searchName}&sorted_type=1">最近新闻优先显示</a>
            </div>
            JUST GO为您检索出 <b>${totalSortedNum } </b>条记录。
            <QZQ:if test="${totalSortedNum !=0}">
		                        以下是第 <b>${bottom}</b>-<b>${checkedTop}</b> 条记录"
		    </QZQ:if>
        </div>
        
     <QZQ:forEach var="newBean" items="${newsBeans}">
        <div class="result-container">
        <h3><a href="DisplayServlet?newsid=${newBean.newsId}&searchName=${searchName}"> ${newBean.newsTitle} </a></h3>
        <p class="result-article">${newBean.newsContent}</p>
        <p class="result-info"><cite>${newBean.newsURL}</cite>发表时间：${newBean.newsDate}</p>
        </div>
	   <%-- ${newBean.newsAuthor}${space}
	     发表时间： ${newBean.newsDate} <br> url: <font color="green"><b> </b>${newBean.newsURL}</font><br><br> --%>
	 </QZQ:forEach>
	 
	 <div align=center>
	  <nav>
	    <ul class="pagination pagination-lg">
	        <QZQ:if test="${currentPage ==0}">
		      <li class="disabled"><a href="#">上一页</a></li>
		    </QZQ:if>   
		    <QZQ:if test="${currentPage !=0}">
		      <li><a href="DividePageServlet?pages=${currentPage-1}">上一页</a></li>
		    </QZQ:if>
		    
		 <QZQ:forEach var="i" begin="0" end="${total_page}">
		    
		    <QZQ:if test="${currentPage ==i}">
		      <li class="active"><a href="DividePageServlet?pages=${i}">${i+1} <span class="sr-only">(current)</span></a></li>
		    </QZQ:if>
		    <QZQ:if test="${currentPage !=i}">
		      <li><a href="DividePageServlet?pages=${i}">${i+1}</a></li>
		    </QZQ:if>
		 </QZQ:forEach>
		 
		 <QZQ:if test="${currentPage ==total_page}">
		      <li class="disabled"><a href="#">下一页</a></li>
		    </QZQ:if>   
		    <QZQ:if test="${currentPage !=total_page}">
		      <li><a href="DividePageServlet?pages=${currentPage+1}">下一页</a></li>
		 </QZQ:if>
		</ul>
	   </nav>
	</div>
    </div>
</div>

<script src="http://cdn.bootcss.com/jquery/1.11.1/jquery.min.js"></script>
<script src="http://cdn.bootcss.com/jquery/1.11.1/jquery.min.js"></script>
<script src="http://cdn.bootcss.com/bootstrap/3.3.0/js/bootstrap.min.js"></script>
<script src="js/divided.js"></script>
<script src="js/jquery-1.10.2.js"></script>
</body>
</html>
