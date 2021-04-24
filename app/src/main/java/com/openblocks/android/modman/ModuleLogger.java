package com.openblocks.android.modman;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;

import com.openblocks.moduleinterface.OpenBlocksModule;
import com.openblocks.moduleinterface.callbacks.Logger;

public class ModuleLogger implements Logger {

    static ModuleLogger logger;

    public static ModuleLogger getInstance() {
        if (logger == null) {
            logger = new ModuleLogger();
        }

        return logger;
    }

    SpannableStringBuilder log = new SpannableStringBuilder();
    Class<Object> class_before;

    LiveLog liveLog = log -> { };


    @Override
    public void debug(Class<? extends OpenBlocksModule> module_class, String text) {
        log("DEBUG", module_class, text, 0xFF999999, false);
    }

    @Override
    public void trace(Class<? extends OpenBlocksModule> module_class, String text) {
        log("TRACE", module_class, text, 0xFFFFDB5A, false);
    }

    @Override
    public void info(Class<? extends OpenBlocksModule> module_class, String text) {
        log("INFO", module_class, text, 0xFF000000, false);
    }

    @Override
    public void warn(Class<? extends OpenBlocksModule> module_class, String text) {
        log("WARN", module_class, text, 0XFFFFAD1F, false);
    }

    @Override
    public void err(Class<? extends OpenBlocksModule> module_class, String text) {
        log("ERROR", module_class, text, 0xFFF31B1B, false);
    }

    @Override
    public void fatal(Class<? extends OpenBlocksModule> module_class, String text) {
        log("FATAL", module_class, text, 0xFFC80D0D, true);
    }


    private void log(String log_level, Class<? extends OpenBlocksModule> module_class, String log_text, int color, boolean bold) {
        String text = getLogStart(module_class) + " [" + log_level + "]: " + log_text;
        int start = log.length();

        log.append(
                text,
                new ForegroundColorSpan(color),
                Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        );

        if (bold)
            log.setSpan(new StyleSpan(android.graphics.Typeface.BOLD), start, log.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);


        // TODO: 3/21/21 Because some logs might be bit spammy, we might should optimize
        //               so the UI doesn't freeze if there are soo many log calls
        liveLog.onLogChange(getText());
    }


    private String getLogStart(Class<? extends OpenBlocksModule> clazz) {
        // TODO: 3/21/21 Because the module might implement other interfaces,
        //  we should just get classes that extends OpenBlocksModule
        return clazz.getInterfaces()[0].getName() + " " + clazz.getName();
    }

    public SpannableString getText() {
        return SpannableString.valueOf(log);
    }

    public void clearLog() {
        log.clear();
    }

    public void setLiveLog(LiveLog liveLog) {
        this.liveLog = liveLog;
        this.liveLog.onLogChange(getText());
    }

    public interface LiveLog {
        void onLogChange(SpannableString log);
    }
}
