# WorkBuddy 国际版注册助手（Android）

当前版本：1.2.0  
包名：`com.example.workbuddyassistant`  
最低系统：Android 6.0（API 23）；目标设备 Android 12（API 31/32）可运行。  
免 Root，无额外权限，无第三方依赖。

## 功能

- 邮箱输入、格式校验、长度限制和即时错误提示。
- 用户主动点击后，才读取剪贴板并粘贴到输入框。
- 用户确认后，将邮箱复制到系统剪贴板并打开 WorkBuddy 官方注册指南。
- 支持单独复制邮箱、打开官方主页、打开系统邮箱应用。
- 对浏览器未安装、邮箱应用未安装、剪贴板不可用等情况提供提示。
- 邮箱不上传、不写入应用持久存储；不收集邮箱密码，不读取验证码，不保存 OAuth/登录令牌。
- 不自动勾选协议，不绕过验证码、第三方授权或服务端校验。

## 官方注册流程说明

WorkBuddy 官方 Windows 注册指南目前列出的方式为 Google、GitHub 和 X。官方文档没有承诺任意邮箱可以独立通过邮箱验证码注册。是否出现邮箱入口，以官方注册页面实际展示为准。

- 官方网站：https://www.workbuddy.ai/
- 官方注册指南：https://www.workbuddy.ai/docs/workbuddy/From-Beginner-to-Expert-Guide/Installation-Win-Guide

本应用不伪造邮箱注册接口，也不向网页注入脚本。若官方页面显示邮箱输入框，用户可在确认后手动粘贴已复制的邮箱；验证码和最终提交由用户本人完成。

## 构建环境

- JDK 17 或更高版本
- Gradle 8.9
- Android SDK Platform 35
- Android Build Tools 35.0.0

```bash
gradle --no-daemon assembleDebug
```

APK 输出：`app/build/outputs/apk/debug/app-debug.apk`。
