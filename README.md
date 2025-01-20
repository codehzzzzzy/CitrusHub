# 基于大模型+RAG的柑橘知识库与专家平台（更新中）

## 模块介绍

### 1、用户管理模块

-   用户注册与登录：允许农民、专家和管理员注册账户并登录系统。
-   用户资料管理：用户可以编辑个人资料，包括基本信息、联系方式等。
-   权限控制：根据不同用户角色（农民、专家、管理员）设置不同的访问权限。

### 2、知识库管理模块

- 知识库导入：管理员可以录入和管理农业知识，包括种植技术、病虫害防治等。
- 知识库检索：用户可以通过关键词搜索相关知识，支持全文检索。
- 知识库分类：按照种类，种植阶段等对知识库内容进行分类

### 3、专家咨询模块

-   专家信息展示：展示专家的详细信息，包括专业领域、工作经验、联系方式等。
-   在线咨询：农民可以向专家提出问题，并通过文字进行交流。

### 4、智能问答模块

-   智能问答系统：基于本地大模型（qwen2.5 7B）+ RAG进行构建
-   问答记录：记录用户回答历史

### 5、市场信息模块

-   价格信息：提供柑橘品市场价格信息，帮助农民了解市场动态。
-   供需信息：发布柑橘供需信息，促进农产品交易。
-   政策资讯：发布柑橘相关政策和资讯，帮助农民及时了解政策变化。

### 6、柑橘交流社区模块

- 发布帖子：用户可以发帖交流柑橘种植问题或分享种植情况，促进经验分享和技术交流。
- 评论：用户可以对帖子进行评论，交流细节，进一步探讨种植技术、市场趋势等问题。
- 帖子浏览推荐：基于协同过滤算法，在帖子浏览后，生成“可能喜欢”板块，推荐5条相似度最高的帖子。

## 技术栈

-   SpringBoot：2.2.1.RELEASE
-   Mybatis-Plus：8.0.29
-   MySQL：8.0.29
-   Hutool：5.8.25
-   Knife4j：4.0.0
-   WebSocket：2.7.10
-   Redis：2.7.6
-   JWT：4.2.1
-   Htmlunit：2.49.1
-   Jsoup：1.15.3

## 本地大模型部署

```
经过测试，qwen对于中文的支持更好一点
```

### 1、下载Ollama

**https://ollama.com（需要翻墙）**

### 2、下载大模型

**可以在[Ollama](https://ollama.com/search)网页的[Models](https://ollama.com/models)中找到想要下载的模型，cmd打开控制台，输入`ollama run xxx`，即可下载xxx模型**

<img src="https://hzzzzzy-typora.oss-cn-guangzhou.aliyuncs.com/images/image-20250101200720634.png" alt="image-20250101200720634" style="zoom:60%;" />.

**比如这里下载的是llama3模型，默认下载8B的版本（不需要翻墙），在该步骤完成后即可在控制台进行对话**

### 3、下载本地知识库（AnythingLLM）

**https://anythingllm.com/desktop（无需翻墙）**

**可以选择本地大模型或商业大模型（填入API key即可）**

<img src="https://hzzzzzy-typora.oss-cn-guangzhou.aliyuncs.com/images/image-20250101212224266.png" alt="image-20250101212224266" style="zoom:67%;" />

**向量数据库默认使用内置的向量数据库 LanceDB**

<img src="https://hzzzzzy-typora.oss-cn-guangzhou.aliyuncs.com/images/image-20250101214437913.png" alt="image-20250101214437913" style="zoom:67%;" />

**Embedding 配置使用 AnythingLLM 自带的 AnythingLLMEmbedder**

<img src="https://hzzzzzy-typora.oss-cn-guangzhou.aliyuncs.com/images/image-20250101214506945.png" alt="image-20250101214506945" style="zoom:67%;" />

**新建工作区，然后点击上传按钮，即可上传本地知识库（支持导入网址）**

**数据质量决定了回答的效果**

<img src="https://hzzzzzy-typora.oss-cn-guangzhou.aliyuncs.com/images/image-20250101212622937.png" alt="image-20250101212622937" style="zoom: 33%;" />

**将聊天模式设置为查询模式，在查询模式下AI会仅基于它查询出来的数据来回答问题**

<img src="https://hzzzzzy-typora.oss-cn-guangzhou.aliyuncs.com/images/image-20250101212931451.png" alt="image-20250101212931451" style="zoom:50%;" />

**最大上下文片段可以调整每一次查询，让它从数据库中取出几个和我们问题相关的片段**

<img src="https://hzzzzzy-typora.oss-cn-guangzhou.aliyuncs.com/images/image-20250101213049918.png" alt="image-20250101213049918" style="zoom:67%;" />

## 后端部署

1.   运行ddl.sql文件创建数据库

2.   修改配置文件中服务的地址

## 前端部署（待更新）

## 爬虫介绍

-   价格信息：https://www.cnhnb.com/hangqing/ganju/（当价格有多页时，只能爬取前五页，第六页需要进行登录）
-   供需信息：https://www.lvguo.net/ganju
-   咨询：http://www.moa.gov.cn/so/s?qt=%E6%9F%91%E6%A9%98

## 项目截图

<img src="https://hzzzzzy-typora.oss-cn-guangzhou.aliyuncs.com/images/image-20250109173926274.png" alt="image-20250109173926274" style="zoom:67%;" />

<img src="https://hzzzzzy-typora.oss-cn-guangzhou.aliyuncs.com/images/image-20250109174005511.png" alt="image-20250109174005511" style="zoom:67%;" />

![image-20250109174107764](https://hzzzzzy-typora.oss-cn-guangzhou.aliyuncs.com/images/image-20250109174107764.png)

<img src="https://hzzzzzy-typora.oss-cn-guangzhou.aliyuncs.com/images/image-20250110151724289.png" alt="image-20250110151724289" style="zoom:67%;" />

<img src="https://hzzzzzy-typora.oss-cn-guangzhou.aliyuncs.com/images/image-20250110151739684.png" alt="image-20250110151739684" style="zoom:67%;" />

<img src="https://hzzzzzy-typora.oss-cn-guangzhou.aliyuncs.com/images/image-20250110151754087.png" alt="image-20250110151754087" style="zoom:67%;" />

