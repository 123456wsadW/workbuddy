package com.example.workbuddyassistant;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputType;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity {
    private static final String OFFICIAL_URL = "https://www.workbuddy.ai/";
    private EditText emailInput;
    private TextView status;
    private int white = Color.rgb(244, 246, 252);
    private int muted = Color.rgb(174, 183, 202);
    private int panel = Color.rgb(28, 34, 49);
    private int accent = Color.rgb(143, 111, 255);

    @Override public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(Color.rgb(14, 17, 26));
        getWindow().setNavigationBarColor(Color.rgb(14, 17, 26));
        buildUi();
    }

    private void buildUi() {
        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(Color.rgb(14, 17, 26));

        LinearLayout page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setPadding(dp(22), dp(30), dp(22), dp(28));
        scroll.addView(page);

        TextView eyebrow = text("WORKBUDDY · ACCOUNT SETUP", 11, accent, true);
        page.addView(eyebrow);

        TextView title = text("国际版注册助手", 27, white, true);
        LinearLayout.LayoutParams titleLp = wrap();
        titleLp.topMargin = dp(8);
        page.addView(title, titleLp);

        TextView intro = text("准备邮箱并打开官方注册入口。验证码、第三方授权和最终确认由你本人完成。", 14, muted, false);
        LinearLayout.LayoutParams introLp = wrap();
        introLp.topMargin = dp(10);
        introLp.bottomMargin = dp(24);
        page.addView(intro, introLp);

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(16), dp(16), dp(16), dp(16));
        card.setBackgroundColor(panel);
        page.addView(card, match());

        card.addView(text("你的邮箱地址", 14, white, true));
        emailInput = new EditText(this);
        emailInput.setSingleLine(true);
        emailInput.setTextColor(white);
        emailInput.setHintTextColor(Color.rgb(119, 129, 151));
        emailInput.setHint("name@example.com");
        emailInput.setTextSize(16);
        emailInput.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
        emailInput.setImeOptions(android.view.inputmethod.EditorInfo.IME_ACTION_DONE);
        emailInput.setPadding(dp(12), dp(10), dp(12), dp(10));
        emailInput.setBackgroundTintList(ColorStateList.valueOf(accent));
        LinearLayout.LayoutParams emailLp = match();
        emailLp.topMargin = dp(8);
        card.addView(emailInput, emailLp);

        TextView hint = text("邮箱只用于本机复制到剪贴板，不会上传或保存到本应用。", 12, muted, false);
        LinearLayout.LayoutParams hintLp = wrap();
        hintLp.topMargin = dp(8);
        card.addView(hint, hintLp);

        Button launch = button("复制邮箱并打开官方注册入口", accent);
        LinearLayout.LayoutParams launchLp = match();
        launchLp.topMargin = dp(18);
        card.addView(launch, launchLp);
        launch.setOnClickListener(v -> prepareRegistration());

        Button copy = button("仅复制邮箱", Color.rgb(66, 78, 105));
        LinearLayout.LayoutParams copyLp = match();
        copyLp.topMargin = dp(9);
        card.addView(copy, copyLp);
        copy.setOnClickListener(v -> copyEmail(true));

        Button mail = button("打开邮箱查看验证邮件", Color.rgb(43, 119, 105));
        LinearLayout.LayoutParams mailLp = match();
        mailLp.topMargin = dp(9);
        page.addView(mail, mailLp);
        mail.setOnClickListener(v -> openMail());

        status = text("状态：等待输入邮箱", 13, muted, false);
        LinearLayout.LayoutParams statusLp = wrap();
        statusLp.topMargin = dp(18);
        page.addView(status, statusLp);

        LinearLayout info = new LinearLayout(this);
        info.setOrientation(LinearLayout.VERTICAL);
        info.setPadding(dp(15), dp(14), dp(15), dp(14));
        info.setBackgroundColor(panel);
        LinearLayout.LayoutParams infoLp = match();
        infoLp.topMargin = dp(14);
        page.addView(info, infoLp);

        info.addView(text("注册方式说明", 14, white, true));
        TextView details = text(
            "WorkBuddy 官方指南当前列出的注册入口包括 Google、GitHub 和 X。请在官方页面选择你实际拥有的登录方式，并自行完成授权与服务条款确认。若页面提供邮箱注册选项，也可按页面提示操作；仅输入邮箱不保证一定能创建账号。",
            13, muted, false);
        LinearLayout.LayoutParams detailsLp = wrap();
        detailsLp.topMargin = dp(8);
        info.addView(details, detailsLp);

        TextView safety = text("本工具不会索取邮箱密码、读取验证码、保存登录令牌，也不会自动勾选协议或绕过验证。", 12, muted, false);
        LinearLayout.LayoutParams safetyLp = wrap();
        safetyLp.topMargin = dp(12);
        page.addView(safety, safetyLp);

        setContentView(scroll);
    }

    private void prepareRegistration() {
        String email = currentEmail();
        if (email == null) return;
        hideKeyboard();
        new AlertDialog.Builder(this)
            .setTitle("打开 WorkBuddy 官方页面？")
            .setMessage("将把邮箱复制到剪贴板并打开官方主页。进入注册页面后，请按页面支持的方式继续；第三方授权、验证码和最终确认由你操作。")
            .setNegativeButton("取消", (d, w) -> {})
            .setPositiveButton("继续", (d, w) -> {
                copyToClipboard(email);
                try {
                    Intent i = new Intent(Intent.ACTION_VIEW, Uri.parse(OFFICIAL_URL));
                    startActivity(i);
                    setStatus("已打开官方主页，邮箱已复制。请粘贴并按页面提示继续。", true);
                } catch (Exception e) {
                    setStatus("无法打开浏览器。请检查网络或默认浏览器设置。", false);
                }
            }).show();
    }

    private String currentEmail() {
        String email = emailInput.getText().toString().trim();
        if (email.isEmpty()) {
            emailInput.setError("请输入邮箱地址");
            emailInput.requestFocus();
            setStatus("请先输入邮箱地址。", false);
            return null;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailInput.setError("邮箱格式不正确");
            emailInput.requestFocus();
            setStatus("邮箱格式不正确，请检查 @ 和域名。", false);
            return null;
        }
        return email;
    }

    private void copyEmail(boolean showStatus) {
        String email = currentEmail();
        if (email == null) return;
        copyToClipboard(email);
        if (showStatus) setStatus("邮箱已复制。", true);
    }

    private void copyToClipboard(String email) {
        ClipboardManager manager = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        manager.setPrimaryClip(ClipData.newPlainText("WorkBuddy registration email", email));
    }

    private void openMail() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_APP_EMAIL);
        try {
            startActivity(intent);
            setStatus("已请求打开系统邮箱应用。", true);
        } catch (Exception e) {
            try {
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:")));
            } catch (Exception ignored) {
                setStatus("未找到邮箱应用，请从桌面手动打开邮箱。", false);
            }
        }
    }

    private void hideKeyboard() {
        try {
            InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(emailInput.getWindowToken(), 0);
        } catch (Exception ignored) {}
    }

    private void setStatus(String message, boolean ok) {
        status.setText("状态：" + message);
        status.setTextColor(ok ? Color.rgb(119, 220, 177) : Color.rgb(255, 157, 157));
    }

    private TextView text(String value, int size, int color, boolean bold) {
        TextView t = new TextView(this);
        t.setText(value);
        t.setTextSize(size);
        t.setTextColor(color);
        if (bold) t.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        t.setLineSpacing(dp(2), 1f);
        return t;
    }

    private Button button(String label, int color) {
        Button b = new Button(this);
        b.setText(label);
        b.setTextSize(14);
        b.setAllCaps(false);
        b.setTextColor(Color.WHITE);
        b.setBackgroundTintList(ColorStateList.valueOf(color));
        return b;
    }

    private LinearLayout.LayoutParams match() {
        return new LinearLayout.LayoutParams(-1, -2);
    }
    private LinearLayout.LayoutParams wrap() {
        return new LinearLayout.LayoutParams(-1, -2);
    }
    private int dp(int v) {
        return (int) (v * getResources().getDisplayMetrics().density + 0.5f);
    }
}
