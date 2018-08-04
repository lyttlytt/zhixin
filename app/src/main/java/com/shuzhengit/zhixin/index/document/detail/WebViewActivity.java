package com.shuzhengit.zhixin.index.document.detail;

import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.library.util.LogUtils;
import com.shuzhengit.zhixin.R;
import com.shuzhengit.zhixin.bean.DocumentPicture;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class WebViewActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        WebView webView = (WebView)findViewById(R.id.webView);
        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setUseWideViewPort(true);

        //设置自适应屏幕，两者合用~
        settings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        settings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小
        webView.loadUrl("file:///android_asset/wen(1).html"); //获取 asset 目录下的test.html文件
//        webView.loadDataWithBaseURL("file:///android_asset/wen(1).html",null,"text/html","utf-8",null);
//        HttpProtocol.getApi()
//                .findDocumentDetailByDocumentId(getIntent().getIntExtra("id",0),0)
//        .compose(RxSchedulersHelper.io_main())
//        .compose(RxResultHelper.handleResult())
//        .subscribe(new RxSubscriber<Document>() {
//            @Override
//            protected void _onNext(Document document) {
//                String s = changeDocumentBody(document.getAllPic(), document.getContent());
//                String html = "<html>" +
//                        "<head><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0, minimum-scale=1.0, maximum-scale=1.0, user-scalable=no\" /><link rel=\"stylesheet\" href=\"file:///android_asset/wechatStyle.css\" type=\"text/css\" /> </head><body>" + s +
//                        "</body></html>";
//                webView.loadDataWithBaseURL(null,html,"text/html","utf-8",null);
//                writeFile(html);
//                Log.i("TAG",html.toString());
//            }
//        });
    }

    private void writeFile(String html) {
        File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + "/wen.txt");
        Log.i("TAG",file.getAbsolutePath());
        if (file.exists()){
            file.delete();
        }
        FileOutputStream fos = null;
        DataOutputStream dos = null;
        try {
            file.createNewFile();
             fos = new FileOutputStream(file);
            fos.write(html.getBytes());
            Log.i("TAG","write");
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            Log.i("TAG","success");
            if (fos!=null){
                try {
                    fos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (dos!=null){
                try {
                    dos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private String changeDocumentBody(List<DocumentPicture> imgSrcs, String documentBody) {
        String oldChars = "";
        String newChars = "";
        for (int i = 0; i < imgSrcs.size(); i++) {
            oldChars = "<!--IMG#" + i + "-->";
            // 在客户端解决WebView图片屏幕适配的问题，在<img标签下添加style='max-width:90%;height:auto;'即可
            // 如："<img" + " style=max-width:100%;height:auto; " + "src=\"" + imgSrcs.get(i).getSrc() + "\"" + "/>"
//            newChars = "<img" + " src=\"" + imgSrcs.get(i).getSrc() + "\"" + "/>";
            newChars = "<img" + " src=\"" + imgSrcs.get(i).getSrc() + "\"" + "/>"+"<p></p>";
            documentBody = documentBody.replace(oldChars, newChars);
        }
        LogUtils.e("TAG",documentBody);
        return documentBody;
    }
}
