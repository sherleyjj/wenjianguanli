package com.example.service.downlad.impl;

import cn.hutool.core.io.FileUtil;
import com.example.common.eumn.DownloadType;
import com.example.service.downlad.IDownloadStrategy;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

public class DefaultDownload implements IDownloadStrategy {
    //准备使用@value从配置文件读取
    private DownloadType downloadType;
    @Override
    public DownloadType getDownloadType() {
        return downloadType;
    }

    @Override
    public void download(HttpServletResponse response, byte[] sources) throws IOException {
        // 设置response的Header

        response.addHeader("Content-Length", "" + sources.length);
        OutputStream toClient = new BufferedOutputStream(response.getOutputStream());
        response.setContentType("application/octet-stream");
        int i = 0;
        int count = 1000;
        //限速成功
        for (; i < sources.length-count; i+=count) {
            toClient.write(sources,i,count);
            try {
                Thread.sleep(5);
            } catch (InterruptedException e) {
            }
        }
        i-=count;

        toClient.write(sources,i,sources.length-i);
        toClient.flush();
        toClient.close();
    }
    public void download(HttpServletResponse response , File file) throws IOException {
        download(response, FileUtil.readBytes(file));
    }
}
