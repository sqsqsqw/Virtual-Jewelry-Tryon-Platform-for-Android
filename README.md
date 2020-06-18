# Virtual Jewelry Tryon Platform for Android

基于Android的虚拟饰品试穿平台，客户端部分
## 前言
由于前期设计及和后期实现的差别问题，将饰品定义为能够在面部穿戴的物品，包括但不限于耳环，帽子眼镜等物品。测试数据中目前只存在帽子和眼镜种类。

本设计用于大连工业大学信息科学与工程学院的毕业设计答辩，不用于商业用途。基于[DrinkCupTea-ShoppingMall][1] 的demo进行修改再开发，Repository的许可证为MIT，其实是个人的无意之举，若有侵权等问题请及时联系作者。
[1]: https://github.com/DrinkCupTea/ShoppingMall



## 准备
Android App需要与服务器端程序以及试穿模块同时运行，若缺少服务器端程序则会直接因为连接不到服务器而退出，若缺少试穿模块，则没有虚拟试穿功能。同时还需要一款浏览器，个人测试使用QQ浏览器。

当服务器端程序以及试穿模块部署成功后更改/app/src/main/assest/config.properties文件指向对应的地址
```
back_url = http://192.168.1.106:8081
tryon_url = https://192.168.1.106/b/src/index.html
```
##运行
个人测试使用真机调试，IDE为Android Studio，准备工作完成后即可安装apk或直接运行或通过命令运行