package com.hzzzzzy.utils;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.NicelyResynchronizingAjaxController;
import com.gargoylesoftware.htmlunit.ScriptException;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import com.gargoylesoftware.htmlunit.javascript.JavaScriptErrorListener;

import java.net.MalformedURLException;
import java.net.URL;

public class WebClientUtils {
    public static WebClient getWebClient(){
        WebClient webClient = new WebClient(BrowserVersion.FIREFOX);
        //配置webClient
        webClient.getOptions().setCssEnabled(false);
        webClient.getOptions().setJavaScriptEnabled(true);
        webClient.setAjaxController(new NicelyResynchronizingAjaxController());
        webClient.getOptions().setThrowExceptionOnFailingStatusCode(false);
        webClient.getOptions().setThrowExceptionOnScriptError(false);
        webClient.waitForBackgroundJavaScript(3*1000);
        webClient.getCookieManager().setCookiesEnabled(true);
        webClient.setJavaScriptErrorListener(new JavaScriptErrorListener() {
            @Override
            public void scriptException(HtmlPage htmlPage, ScriptException e) {}

            @Override
            public void timeoutError(HtmlPage htmlPage, long l, long l1) {}

            @Override
            public void malformedScriptURL(HtmlPage htmlPage, String s, MalformedURLException e) {}

            @Override
            public void loadScriptError(HtmlPage htmlPage, URL url, Exception e) {}

            @Override
            public void warn(String s, String s1, int i, String s2, int i1) {}
        });
        return webClient;
    }
}
