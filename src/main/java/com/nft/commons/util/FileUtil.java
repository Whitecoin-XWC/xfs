package com.nft.commons.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;

public class FileUtil {

    public static String getContent(String filePath){
        InputStreamReader read = null;
        BufferedReader bufferedReader = null;
        try { StringBuffer stringBuffer = new StringBuffer();

           File file=new File(filePath);
           if(file.isFile() && file.exists()) { //判断文件是否存在
               read = new InputStreamReader(
                       new FileInputStream(file), "UTF-8");//考虑到编码格式
               bufferedReader = new BufferedReader(read);
               String lineTxt = null;
               while ((lineTxt = bufferedReader.readLine()) != null) {
                   stringBuffer.append(lineTxt);
                   if(stringBuffer.length() >= 1000){
                       break;
                   }
               }
           }
           return stringBuffer.toString();
       }catch (Exception e){
            return "";
       } finally {
            try {
                if(read != null) {
                    read.close();
                }
                if(bufferedReader != null){
                    bufferedReader.close();
                }
            } catch (Exception ee){

            }
        }
    }
}
