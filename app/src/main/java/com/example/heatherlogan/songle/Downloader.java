package com.example.heatherlogan.songle;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLConnection;
import java.io.InputStream;
import android.util.Log;
import java.net.URL;

public class Downloader {

    private static String tag = "Songs";

    static final int POST_PROGRESS = 1;

    public static void DownloadFromUrl(String URL, FileOutputStream fos) {

        try {

            System.out.println("Download URL: "+ URL);
            URL url = new URL(URL);

            URLConnection ucon = url.openConnection();

            InputStream is = ucon.getInputStream();
            BufferedInputStream bis = new BufferedInputStream(is);
            BufferedOutputStream bos = new BufferedOutputStream(fos);

            /*Read and write file*/

            byte data[] = new byte [1024];
            int count;

            while ((count = bis.read(data)) != -1){

                bos.write(data, 0, count);
            }

            // prevent file from getting corrupted
            bos.flush();
            bos.close();


        } catch (IOException e){
            Log.d(tag, "Error: " + e);
        }
    }
}
