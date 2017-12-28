# SearchEngine
A simple search engine for news of Sohu

## 工作原理:  

1：爬行

搜索引擎是通过一种特定规律的软件，遵从一些命令或文件的内容来跟踪网页的链接，从一个链接爬到另外一个链接，像蜘蛛在蜘蛛网上爬行一样，所以被称为“蜘蛛”也被称为“机器人”。

2：抓取存储

通过蜘蛛跟踪链接爬行到网页，并将爬行的数据存入原始页面数据库。在抓取页面时，也做一定的重复内容检测，一旦遇到权重很低的网站上有大量抄袭、采集或者复制的内容，很可能就不再爬行。

3：预处理

搜索引擎将蜘蛛抓取回来的页面，进行各种步骤的预处理。
除了HTML 文件外，搜索引擎通常还能抓取和索引以文字为基础的多种文件类型。我们在搜索结果中也经常会看到这些文件类型。 但搜索引擎还不能处理图片、视频、Flash 这类非文字内容，也不能执行脚本和程序。

### 首页index.html:  
主要用于接收用户输入的关键词，并链接到相关servlet进行后台控制处理
![首页index.html](https://raw.githubusercontent.com/qzq2514/ImageForGithubMakdown/master/SearchEngine/Index.png)  



### 分页展示页面dividePage.jsp:
获取后台传来的集合对象，并对集合中包含的新闻元素进行遍历，展示新闻的相关信息，同时采取了关键词的高亮展示，提高了交互性
![dividePage.jsp](https://raw.githubusercontent.com/qzq2514/ImageForGithubMakdown/master/SearchEngine/Divided.png)  
 


### 单条信息详细展示页面display.jsp：
根据DisplayServlet传来的单条新闻的详细数据，详细展示新闻的标题，正文，作者等信息
![display.jsp](https://raw.githubusercontent.com/qzq2514/ImageForGithubMakdown/master/SearchEngine/Display.png)  
