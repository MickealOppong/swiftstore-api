package com.swiftstore.util;

import org.springframework.stereotype.Component;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Component
public class AppImageUtil {

    public static byte[] compressImage(byte[] data){
        Deflater deflater = new Deflater();
        deflater.setLevel(Deflater.BEST_COMPRESSION);
        deflater.setInput(data);
        deflater.finish();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4*1024];
        while (!deflater.finished()){
            int size = deflater.deflate(tmp);
            outputStream.write(tmp,0,size);
        }

        try{
            outputStream.close();
        }catch (IOException e){

        }
        return outputStream.toByteArray();
    }



    public static byte[] deCompressImage(byte[] data) throws DataFormatException {
        Inflater inflater = new Inflater();
        inflater.setInput(data);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] tmp = new byte[4*1024];
        while (!inflater.finished()){
            int size = inflater.inflate(tmp);
            outputStream.write(tmp,0,size);
        }

        try{
            outputStream.close();
        }catch (IOException e){

        }
        return outputStream.toByteArray();
    }
}
