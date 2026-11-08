# WorkBuddy 国际版注册助手（Android）

Android 12 / API 31 兼容，免 Root。该应用是官方注册流程的辅助工具，不是账号批量注册器。

## 当前功能
- 邮箱格式校验和即时错误提示
- 用户确认后复制邮箱并打开 WorkBuddy 官方主页
- 一键打开系统邮箱应用查看验证邮件
- 邮箱只在本地输入框中临时使用，不写入持久存储、不上传
- 不收集邮箱密码，不读取验证码，不保存 OAuth/登录令牌
- 不自动勾选协议、不绕过第三方授权或官方安全验证

## 官方注册流程说明
WorkBuddy 官方安装指南说明，首次注册需在 Register 页面确认相关协议，再选择页面提供的注册方式。指南当前列出 Google、GitHub、X 登录方式。具体以官网页面实时显示为准。因此任意邮箱不一定可以单独通过邮箱验证码注册。

官方入口：https://www.workbuddy.ai/
官方注册指南：https://www.workbuddy.ai/docs/workbuddy/From-Beginner-to-Expert-Guide/Installation-Win-Guide

## 构建环境
- JDK 17 或更高版本
- Gradle 8.9
- Android SDK Platform 35
- Android Build Tools 35.0.0

```bash
gradle assembleDebug
```

APK 输出：`app/build/outputs/apk/debug/app-debug.apk`

Android Studio 可直接打开项目根目录构建。
