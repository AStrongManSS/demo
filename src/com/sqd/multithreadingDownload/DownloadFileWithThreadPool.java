package com.sqd.multithreadingDownload;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/***
 * 多线程下载文件
 */
public class DownloadFileWithThreadPool {

    public static void getFileWithThreadPool(String url, String filePath, int poolLength) throws IOException {
        ExecutorService exec = Executors.newFixedThreadPool(poolLength);
        long len = getContentLength(url);
        for (int i = 0; i < poolLength; i++) {
            long start = i * len / poolLength;
            long end = (i + 1) * len / poolLength;
            if(i == poolLength - 1){
                end = len;
            }
            exec.execute(new DownloadWithRange(url, filePath, start, end));
        }
        exec.shutdown();
    }

    public static long getContentLength(String urlLocation) throws IOException {
        URL url = null;
        if(urlLocation != null){
            url = new URL(urlLocation);
        }
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setReadTimeout(5000);
        conn.setRequestMethod("GET");
        long len = conn.getContentLength();

        return len;
    }

    public static void main(String[] args) {
        System.out.println(System.currentTimeMillis());
        String url = "http://www.nnydlc.com:11080/fsvr/resources/2017/11/23/stadiumMedia/pics/original/37c75a6d-fe60-40a5-aabd-c8e9b3f33fec.jpg?r=7251139705423929529&t=ff8ee6f345281757e8c61e976877ad6e";
        String filePath = "E:\\abc.jpg";
        int threadMun = 10;
        try {
            getFileWithThreadPool(url, filePath, threadMun);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
