## ShiZhong
帮助制定及管理一些乱七八糟的计划，同时提供了几个并没什么用的小工具。

[![996.icu](https://img.shields.io/badge/link-996.icu-red.svg)](https://996.icu) [![LICENSE](https://img.shields.io/badge/license-NPL%20(The%20996%20Prohibited%20License)-blue.svg)](https://github.com/996icu/996.ICU/blob/master/LICENSE) [![HitCount](http://hits.dwyl.io/0xZhangKe/ShiZhong.svg)](http://hits.dwyl.io/0xZhangKe/ShiZhong)

## 介绍
这是一个 Android 平台的 APP，项目基于 RxAndroid+Retrofit+Glide 开发，使用MVP架构（好像大家都喜欢这么写......）。


先看一下这个 APP 截图。
## 截图

![计划列表](screenshots/show_plan.jpg)

这个是首页。


![计划详情](screenshots/ration_plan_detail.jpg)

这个是定量计划的详情，我在 Material Design 官网上学（抄）的，一个买家秀一个卖家秀。


![计划详情](screenshots/clock_plan_detail.jpg)

这个是打卡计划的详情页面。


![TODO列表](screenshots/show_todo.jpg)

这个是 TODO 列表（市面上有一万个做 TODO 的 APP。。。），根据时间的紧急度做了颜色渐变。


![设置](screenshots/setting.jpg)

设置页面其实真的没啥好设置的。

## 技术点
这个破 APP 也没啥技术难点，挺简单的反正。 
用的是 MVP 架构（真麻烦），里面也有几个 HTTP 接口，用的 RxAndroid+Retrofit，这俩加起来是真的好用，但是 Retrofit 的 baseURL 设置之后就不能更改，这个不太好我觉得，好在网上有很多解决方案，搜一下就有。
数据库 ORM 用的是 GreenDao。
代码总共也不多，写的还算是挺整洁的，注释啥的都有。

## 地址
我上架到酷安了，别的没上传，太麻烦了，国内一堆的市场。

酷安地址：
https://www.coolapk.com/apk/199335


另外 APK 我也传到 Github 上了（记得点 star 啊）：

https://github.com/0xZhangKe/ShiZhong

还有啥问题欢迎给我提 issue，我会尽力改的。

如果觉得还不错的话，欢迎关注我的个人公众号，我会不定期发一些干货文章~

![公众号](screenshots/qrcode_for_gsubscription.jpg)

也可以加我微信：

![公众号](screenshots/qrcode_for_account.jpg)
