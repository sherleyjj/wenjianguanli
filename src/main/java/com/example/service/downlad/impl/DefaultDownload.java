package com.example.service.downlad.impl;

import cn.hutool.core.io.FileUtil;
import com.example.common.eumn.DownloadType;
import com.example.service.downlad.IDownloadStrategy;
import org.springframework.beans.factory.annotation.Value;

import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.net.URLEncoder;

public class DefaultDownload implements IDownloadStrategy {
    //TODO 准备使用@value从配置文件读取
    private DownloadType downloadType;

    @Value("${download.count}")
    private int count; //速度应该为100kByte/S
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

        /**TODO Bug fix success
         * status1: count > length
         * status2: count < length
         */
        //限速成功
        for (; i < sources.length-count; i+=count) {
            toClient.write(sources,i,count);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
            }
        }
        i = i==0?0:i-count;
        toClient.write(sources,i,sources.length-i);
        toClient.flush();
        toClient.close();
    }
    public void download(HttpServletResponse response , File file) throws IOException {
        download(response, FileUtil.readBytes(file));
    }
    public void download(HttpServletResponse response , String filepath) throws IOException {
        byte[] bytes = FileUtil.readBytes(filepath);
        response.reset();
        //TODO 设计中文问题必须URL编码 Content-Disposition 响应头指示回复的内容该以何种形式展示，是以内联的形式（即网页或者页面的一部分），还是以附件的形式下载并保存到本地。
        response.addHeader("Content-Disposition", "attachment;filename=" + URLEncoder.encode(FileUtil.getName(filepath), "UTF-8"));
        download(response,bytes);
    }
}
