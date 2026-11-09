package com.example.workbuddyassistant;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class MainActivity extends Activity {
    private static final String OFFICIAL_URL = "https://www.workbuddy.ai/";
    private static final String OFFICIAL_GUIDE_URL =
            "https://www.workbuddy.ai/docs/workbuddy/From-Beginner-to-Expert-Guide/Installation-Win-Guide";

    private final int background = Color.rgb(14, 17, 26);
    private final int panel = Color.rgb(28, 34, 49);
    private final int white = Color.rgb(244, 246, 252);
    private final int muted = Color.rgb(174, 183, 202);
    private final int accent = Color.rgb(143, 111, 255);
    private EditText emailInput;
    private TextView status;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        getWindow().setStatusBarColor(background);
        getWindow().setNavigationBarColor(background);
        buildUi();
    }

    private void buildUi() {
        ScrollView scroll = new ScrollView(this);
        scroll.setFillViewport(true);
        scroll.setBackgroundColor(background);

        LinearLayout page = new LinearLayout(this);
        page.setOrientation(LinearLayout.VERTICAL);
        page.setPadding(dp(22), dp(28), dp(22), dp(28));
        scroll.addView(page);

        page.addView(text("WORKBUDDY · ACCOUNT SETUP", 11, accent, true));

        TextView title = text("国际版注册助手", 27, white, true);
        LinearLayout.LayoutParams titleLp = wrap();
        titleLp.topMargin = dp(8);
        page.addView(title, titleLp);

        TextView intro = text(
                "准备邮箱并进入 WorkBuddy 官方注册说明。邮箱填写由你主动触发；验证码、第三方授权和最终确认始终由你本人完成。",
                14, muted, false);
        LinearLayout.LayoutParams introLp = wrap();
        introLp.topMargin = dp(10);
        introLp.bottomMargin = dp(22);
        page.addView(intro, introLp);

        LinearLayout card = new LinearLayout(this);
        card.setOrientation(LinearLayout.VERTICAL);
        card.setPadding(dp(16), dp(16), dp(16), dp(16));
        card.setBackgroundColor(panel);
        page.addView(card, match());

        card.addView(text("邮箱地址", 14, white, true));

        emailInput = new EditText(this);
        emailInput.setSingleLine(true);
        emailInput.setTextColor(white);
        emailInput.setHintTextColor(Color.rgb(119, 129, 151));
        emailInput.setHint("name@example.com");
        emailInput.setTextSize(16);
        emailInput.setInputType(InputType.TYPE_CLASS_TEXT
                | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
                | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS);
        emailInput.setImeOptions(EditorInfo.IME_ACTION_DONE);
        emailInput.setFilters(new InputFilter[]{new InputFilter.LengthFilter(254)});
        emailInput.setPadding(dp(12), dp(10), dp(12), dp(10));
        emailInput.setBackgroundTintList(ColorStateList.valueOf(accent));
        emailInput.setContentDescription("输入用于注册的邮箱地址");
        if (android.os.Build.VERSION.SDK_INT >= 26) {
            emailInput.setAutofillHints(View.AUTOFILL_HINT_EMAIL_ADDRESS);
            emailInput.setImportantForAutofill(View.IMPORTANT_FOR_AUTOFILL_YES);
        }
        LinearLayout.LayoutParams emailLp = match();
        emailLp.topMargin = dp(8);
        card.addView(emailInput, emailLp);

        emailInput.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (emailInput.getError() != null) emailInput.setError(null);
            }
            @Override public void afterTextChanged(android.text.Editable s) {}
        });
        emailInput.setOnEditorActionListener((v, actionId, event) -> {
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if (currentEmail() != null) hideKeyboard();
                return true;
            }
            return false;
        });

        TextView hint = text(
                "支持常见邮箱服务商。输入内容仅在本机内存中处理；不会上传，也不会写入应用持久存储。",
                12, muted, false);
        LinearLayout.LayoutParams hintLp = wrap();
        hintLp.topMargin = dp(8);
        card.addView(hint, hintLp);

        LinearLayout helperRow = new LinearLayout(this);
        helperRow.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams helperLp = match();
        helperLp.topMargin = dp(12);
        card.addView(helperRow, helperLp);

        Button paste = button("从剪贴板粘贴", Color.rgb(66, 78, 105));
        LinearLayout.LayoutParams half = new LinearLayout.LayoutParams(0, -2, 1f);
        helperRow.addView(paste, half);
        paste.setOnClickListener(v -> pasteEmail());

        Button clear = button("清空", Color.rgb(66, 78, 105));
        LinearLayout.LayoutParams half2 = new LinearLayout.LayoutParams(0, -2, 1f);
        half2.leftMargin = dp(8);
        helperRow.addView(clear, half2);
        clear.setOnClickListener(v -> {
            emailInput.setText("");
            emailInput.requestFocus();
            setStatus("邮箱已清空。", true);
        });

        Button prepare = button("复制邮箱并打开官方注册指引", accent);
        LinearLayout.LayoutParams prepareLp = match();
        prepareLp.topMargin = dp(16);
        card.addView(prepare, prepareLp);
        prepare.setOnClickListener(v -> prepareRegistration());

        Button copy = button("仅复制邮箱", Color.rgb(66, 78, 105));
        LinearLayout.LayoutParams copyLp = match();
        copyLp.topMargin = dp(8);
        card.addView(copy, copyLp);
        copy.setOnClickListener(v -> copyEmail());

        Button home = button("打开 WorkBuddy 官方网站", Color.rgb(43, 119, 105));
        LinearLayout.LayoutParams homeLp = match();
        homeLp.topMargin = dp(12);
        page.addView(home, homeLp);
        home.setOnClickListener(v -> {
            if (openUrl(OFFICIAL_URL, "无法打开 WorkBuddy 官方网站，请检查浏览器或网络。")) {
                setStatus("已打开 WorkBuddy 官方网站。", true);
            }
        });

        Button mail = button("打开邮箱查看验证邮件", Color.rgb(43, 119, 105));
        LinearLayout.LayoutParams mailLp = match();
        mailLp.topMargin = dp(8);
        page.addView(mail, mailLp);
        mail.setOnClickListener(v -> openMail());

        status = text("状态：等待输入邮箱", 13, muted, false);
        status.setAccessibilityLiveRegion(View.ACCESSIBILITY_LIVE_REGION_POLITE);
        LinearLayout.LayoutParams statusLp = wrap();
        statusLp.topMargin = dp(16);
        page.addView(status, statusLp);

        LinearLayout info = new LinearLayout(this);
        info.setOrientation(LinearLayout.VERTICAL);
        info.setPadding(dp(15), dp(14), dp(15), dp(14));
        info.setBackgroundColor(panel);
        LinearLayout.LayoutParams infoLp = match();
        infoLp.topMargin = dp(14);
        page.addView(info, infoLp);

        info.addView(text("官方注册方式说明", 14, white, true));
        TextView details = text(
                "WorkBuddy 官方 Windows 注册指南当前列出的方式是 Google、GitHub 和 X。官方指南没有承诺支持任意邮箱单独注册。因此本工具只负责准备和复制邮箱；如果官方页面实际出现邮箱输入框，请由你确认后粘贴。不要把邮箱输入等同于账号注册成功。",
                13, muted, false);
        LinearLayout.LayoutParams detailsLp = wrap();
        detailsLp.topMargin = dp(8);
        info.addView(details, detailsLp);

        Button guide = button("查看官方注册指南", Color.rgb(66, 78, 105));
        LinearLayout.LayoutParams guideLp = match();
        guideLp.topMargin = dp(10);
        info.addView(guide, guideLp);
        guide.setOnClickListener(v -> {
            if (openUrl(OFFICIAL_GUIDE_URL, "无法打开官方注册指南，请稍后重试。")) {
                setStatus("已打开官方注册指南。", true);
            }
        });

        TextView safety = text(
                "隐私边界：不索取邮箱密码，不读取验证码，不保存登录令牌，不自动勾选协议，也不绕过验证码、第三方授权或服务端校验。",
                12, muted, false);
        LinearLayout.LayoutParams safetyLp = wrap();
        safetyLp.topMargin = dp(14);
        page.addView(safety, safetyLp);

        setContentView(scroll);
    }

    private void prepareRegistration() {
        final String email = currentEmail();
        if (email == null) return;
        hideKeyboard();

        new AlertDialog.Builder(this)
                .setTitle("准备打开官方注册指引？")
                .setMessage("确认后，本应用会把邮箱复制到剪贴板并打开 WorkBuddy 官方注册指引。若官方页面提供邮箱输入框，请由你手动粘贴；如果页面只提供 Google、GitHub 或 X，请按官方流程自行授权。")
                .setNegativeButton("取消", (dialog, which) -> {})
                .setPositiveButton("继续", (dialog, which) -> {
                    if (!copyToClipboard(email)) return;
                    if (openUrl(OFFICIAL_GUIDE_URL, "邮箱已复制，但无法打开官方注册指引。请检查浏览器或网络。")) {
                        setStatus("邮箱已复制。请在官方页面确认是否提供邮箱入口，再由你手动粘贴。", true);
                    }
                })
                .show();
    }

    private String currentEmail() {
        String email = emailInput.getText().toString().trim();
        if (email.isEmpty()) {
            showInputError("请输入邮箱地址", "邮箱不能为空。");
            return null;
        }
        if (email.length() > 254) {
            showInputError("邮箱长度不能超过 254 个字符", "邮箱地址过长，请检查输入。");
            return null;
        }
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(toAsciiDomain(email)).matches()) {
            showInputError("邮箱格式不正确", "请检查邮箱用户名、@ 符号和域名。");
            return null;
        }
        return email;
    }

    private String toAsciiDomain(String email) {
        int at = email.lastIndexOf('@');
        if (at <= 0 || at == email.length() - 1) return email;
        try {
            String local = email.substring(0, at);
            String domain = java.net.IDN.toASCII(email.substring(at + 1),
                    java.net.IDN.USE_STD3_ASCII_RULES);
            return local + "@" + domain;
        } catch (IllegalArgumentException ignored) {
            return email;
        }
    }

    private void showInputError(String fieldError, String message) {
        emailInput.setError(fieldError);
        emailInput.requestFocus();
        setStatus(message, false);
    }

    private void copyEmail() {
        String email = currentEmail();
        if (email == null) return;
        if (!copyToClipboard(email)) return;
        setStatus("邮箱已复制到剪贴板。", true);
    }

    private void pasteEmail() {
        try {
            ClipboardManager manager =
                    (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            ClipData clip = manager == null ? null : manager.getPrimaryClip();
            if (clip == null || clip.getItemCount() == 0) {
                setStatus("剪贴板中没有可粘贴的内容。", false);
                return;
            }
            CharSequence value = clip.getItemAt(0).coerceToText(this);
            if (value == null || value.toString().trim().isEmpty()) {
                setStatus("剪贴板内容为空。", false);
                return;
            }
            emailInput.setText(value.toString().trim());
            emailInput.setSelection(emailInput.length());
            setStatus("已粘贴剪贴板内容，请检查邮箱格式。", true);
        } catch (Exception e) {
            setStatus("读取剪贴板失败，请手动输入邮箱。", false);
        }
    }

    private boolean copyToClipboard(String email) {
        try {
            ClipboardManager manager =
                    (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
            if (manager == null) {
                setStatus("系统剪贴板不可用，请检查设备设置。", false);
                return false;
            }
            manager.setPrimaryClip(ClipData.newPlainText("Email address", email));
            return true;
        } catch (Exception e) {
            setStatus("复制邮箱失败，请检查系统剪贴板设置。", false);
            return false;
        }
    }

    private boolean openUrl(String url, String errorMessage) {
        try {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        } catch (ActivityNotFoundException | SecurityException e) {
            setStatus(errorMessage, false);
            return false;
        } catch (Exception e) {
            setStatus(errorMessage, false);
            return false;
        }
    }

    private void openMail() {
        Intent emailIntent = new Intent(Intent.ACTION_MAIN);
        emailIntent.addCategory(Intent.CATEGORY_APP_EMAIL);
        try {
            startActivity(emailIntent);
            setStatus("已请求打开邮箱应用。", true);
        } catch (ActivityNotFoundException e) {
            try {
                Intent mailto = new Intent(Intent.ACTION_VIEW, Uri.parse("mailto:"));
                startActivity(Intent.createChooser(mailto, "选择邮箱应用"));
                setStatus("请选择邮箱应用。", true);
            } catch (Exception ignored) {
                setStatus("未找到邮箱应用，请从桌面手动打开。", false);
            }
        } catch (Exception e) {
            setStatus("无法打开邮箱应用，请从桌面手动打开。", false);
        }
    }

    private void hideKeyboard() {
        try {
            InputMethodManager imm =
                    (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
            if (imm != null) imm.hideSoftInputFromWindow(emailInput.getWindowToken(), 0);
        } catch (Exception ignored) {
        }
    }

    private void setStatus(String message, boolean ok) {
        if (status == null) return;
        status.setText("状态：" + message);
        status.setTextColor(ok ? Color.rgb(119, 220, 177) : Color.rgb(255, 157, 157));
        status.setContentDescription("状态：" + message);
    }

    private TextView text(String value, int size, int color, boolean bold) {
        TextView view = new TextView(this);
        view.setText(value);
        view.setTextSize(size);
        view.setTextColor(color);
        if (bold) view.setTypeface(Typeface.DEFAULT, Typeface.BOLD);
        view.setLineSpacing(dp(2), 1f);
        return view;
    }

    private Button button(String label, int color) {
        Button view = new Button(this);
        view.setText(label);
        view.setTextSize(14);
        view.setAllCaps(false);
        view.setTextColor(Color.WHITE);
        view.setBackgroundTintList(ColorStateList.valueOf(color));
        return view;
    }

    private LinearLayout.LayoutParams match() {
        return new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private LinearLayout.LayoutParams wrap() {
        return new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    private int dp(int value) {
        return (int) (value * getResources().getDisplayMetrics().density + 0.5f);
    }
}
