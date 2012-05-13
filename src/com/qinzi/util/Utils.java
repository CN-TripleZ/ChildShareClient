package com.qinzi.util;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

import android.graphics.Bitmap;


public final class Utils {
	
    public static String saveTempFile(Bitmap bm, String filePath, String fileName) throws IOException {   
    	File dir = new File(filePath);
		if (!dir.exists()) {
			dir.mkdir();
		}
		File file = new File(filePath, fileName);   
        BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(file));   
        bm.compress(Bitmap.CompressFormat.JPEG, 100, bos);  
        bos.flush();   
        bos.close();   
        return file.getPath();
    }  
}
