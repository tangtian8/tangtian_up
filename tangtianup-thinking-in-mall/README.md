# 新加入的同学，以下内容请务必仔细阅读，给同学们整理的学习课程的集锦宝典，帮助大家更高效的完成一期及二期课程

## Geely慕课网首页
手记、实战和免费课程都可以在这里找到
https://www.imooc.com/t/2705746

## 全套课程代码说明

### 一期项目
Java带你从0到上线开发企业级电商项目
https://coding.imooc.com/class/96.html

- 一期项目git仓库
https://git.imooc.com/Project/coding-96

### 二期项目
Java企业级电商项目架构演进之路 Tomcat集群与Redis分布式
https://coding.imooc.com/class/162.html

- 在二期git仓库中V1.0的分支是一期的代码
https://git.imooc.com/Project/coding-162/src/V1.0

- 二期的项目代码在master分支上
https://git.imooc.com/Project/coding-162/src/master

### 免费课程
SpringMVC数据绑定 

http://www.imooc.com/learn/558


## 开放访问环境

### 前台 
http://www.happymmall.com
### 后台 
http://admin.happymmall.com

## 开放账号密码访问环境

### 开放前台 
http://test.happymmall.com

### 开放后台 
http://admintest.happymmall.com

### 账号：admin 密码：admin(为了更多小伙伴们体验使用，请不要修改密码)


 
## 部署环境 
部署环境的话 ，建议看下文章最后一小段哟~首先跟着课程阿里云部署那个章节过一遍，可以先不跟着操作，做到心里有数，环境部署都做了什么。
http://coding.imooc.com/lesson/96.html#mid=3861 

## 项目图文部署
详见项目中帮助文档目录中的各种文档

## 接口文档
https://gitee.com/imooccode/happymmallwiki/wikis/Home


## 架构手记
大型项目架构演进过程及思考的点 
http://www.imooc.com/article/17545

## 思维导图
课程项目思维导图及线上环境、测试环境、部署linux和windows等解答
http://www.imooc.com/article/20193 

## 问答区
【重点】问答区常见问题整理
http://www.imooc.com/article/18998

## 群分享
课程项目群分享手记

http://www.imooc.com/article/19094

## 找工作
找工作的季节之简历及找工作的分享
http://www.imooc.com/article/19998


## 项目各种软件、环境、配置
项目环境、vsftpd、linux、mysql等各种配置、软件下载
http://learning.happymmall.com

## mybatis 相关
### mybatis-plugin插件安装
http://coding.imooc.com/learn/questiondetail/36007.html

### mybatis generator
mybatis generator使用的mysql-bin.jar包在工程项目tools文件夹下，可以直接使用


# 课程详细说明

## 环境篇
1. centos下载地址

    http://coding.imooc.com/learn/questiondetail/24353.html
    
    阿里云的下载已经失效，请前往官网下载地址下载。

    https://wiki.centos.org/Download

    http://vault.centos.org/6.8/isos/x86_64/

2.	如自己找软件比较麻烦，可以访问http://learning.happymmall.com/

3.	如自己配置比较麻烦，可以参考线上配置http://learning.happymmall.com

4.	接口文档：http://git.oschina.net/imooccode/happymmallwiki/wikis/home

5.	SQL导入，可以通过下载的mmall.sql文件或者从http://learning.happymmall.com/mmall.sql 这里下载sql文件，然后导入到自己的数据库中,可以通过Navicat 数据库客户端工具导入。

6.	课程QQ群分享的资料，这里的资料都是我分享和小伙伴们分享的，有用哟。 http://learning.happymmall.com/QQ%E5%AD%A6%E4%B9%A0%E7%BE%A4%E5%A4%A7%E5%AE%B6%E5%85%B1%E4%BA%AB%E7%9A%84useful%E6%96%87%E6%A1%A3/

7.	Nginx：http://learning.happymmall.com/nginx/ 大家可以访问这个网址，这里可以下载Nginx的Linux/Mac/Windows的版本，还有一期课程线上的配置文件。

8.	Tomcat： http://learning.happymmall.com/tomcat/ 请来这里下载windows、linux/mac 下的tomcat7
如果配置nginx+ftp服务+tomcat来做图片服务器，访问不到，请重点参考此问答 http://coding.imooc.com/learn/questiondetail/9369.html 

9.	自动化发布shell脚本： http://learning.happymmall.com/deploy/ 可以在这里下载到一期课程自动化发布脚本，当然具体的路径还要根据自己的实际项目的环境修改成自己的shell脚本哟
10.	系统环境变量: http://learning.happymmall.com/env/ 这里给大家整理了防火墙的配置和Linux系统环境变量，当然具体的路径小伙伴们根据自己的实际情况来修改好哟。
11.	FTP服务:Windows请在 http://learning.happymmall.com/ftpserver/ 下载，Linux请使用yum安装，在Linux下Vsftpd配置请访问 http://learning.happymmall.com/vsftpdconfig/ 另外在配置vsftpd服务的时候，一定要检查创建的ftp目录里面的用户、用户组权限，是否有对应ftpuser用户的权限，如果没有，可以使用chown、chgrp来指定到ftp操作用户。然后通过chmod给予对应ftp用户的读写执行权限。
12.	Git：请访问 http://learning.happymmall.com/git/ 下载windows和linux/mac 下的git客户端
13.	JDK：请访问 http://learning.happymmall.com/jdk/ 下载windows和linux/mac 下的JDK 
14.	Maven：请访问http://learning.happymmall.com/maven/下载windows和linux/mac 下的Maven
如果Maven默认仓库源比较慢，请修改settings文件，配置上阿里云的Maven仓库源，那个速度还是挺快的。
15.	MySQL：请访问 http://learning.happymmall.com/mysql/下载windows和linux/mac 下的MySQL，另外里面提供了一期课程mysql的配置文件。
16.	支付宝： 具体的支付宝对接时候各种文档等，url比较长，特意给大家整理了。请访问 http://learning.happymmall.com/alipaydoc.html 这个网址。
17.	PPT长命令：ppt里面的长命令也给小伙伴们整理了。请访问 http://learning.happymmall.com/pptcommand.html 
18.	一期课程的思维导图： http://learning.happymmall.com/happymmallv1.svg
19.	架构演进之Tomcat集群和分布式进阶课程相关资料
http://learning.happymmall.com/%E4%BA%8C%E6%9C%9F%E9%9B%86%E7%BE%A4%E5%8F%8A%E7%BC%93%E5%AD%98%E5%88%86%E5%B8%83%E5%BC%8FJava%E7%AB%AF/


## 项目代码RUN起来
1.	下载sql初始化文件mmall.sql
2.	保证在已经安装jdk，maven，tomcat，mysql等的环境并配置好
3.	解压缩源码之后，使用eclipse或者idea导入maven项目
4.  导入详见项目帮助文档中的word，图文均有




# Geely的祝福
【最后祝同学，学完能感觉自己有提高，有收获，同学的支持也是我最大的动力！】


