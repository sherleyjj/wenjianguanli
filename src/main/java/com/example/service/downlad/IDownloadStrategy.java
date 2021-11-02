package com.example.service.downlad;

import com.example.common.eumn.DownloadType;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public interface IDownloadStrategy {
    DownloadType getDownloadType();
    void download(HttpServletResponse response,byte [] sources) throws IOException;
}
