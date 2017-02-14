package com.changingpay.threadtest;

import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;

/**
 * Created by 公司 on 2017/2/6.
 */
public class ByteTest {
    @Test
    public void print(){
        byte[] test = new byte[]{10};
        this.toBcd("10",test);
        int len = test.length;
        System.out.println(len);
        for(int i=0; i<len ; i++){
            System.out.print(test[i]+" ");
        }
    }

    @Test
    public void printIsoHead(){
        byte[] head = new byte[0];
        head = String.format("%04d", new Object[]{Integer.valueOf(200)}).getBytes();
        System.out.println(head[0]);
    }

    @Test
    public void printIntToHex(){
        int len = Integer.parseInt(Integer.toHexString(56 & 255).length() > 1?Integer.toHexString(56 & 255):"0" + Integer.toHexString(56 & 255), 16);
        Integer i = null;
        System.out.println(i == null);
    }

    @Test
    public void printWriteByte(){
        ByteArrayOutputStream output = new ByteArrayOutputStream();
        output.write((byte)56);
        byte[] b = output.toByteArray();
        System.out.println(b[0]);
    }

    private void toBcd(String value, byte[] buf) {
        int charpos = 0;
        int bufpos = 0;
        if(value.length() % 2 == 1) {
            buf[0] = (byte)(value.charAt(0) - 48);
            charpos = 1;
            bufpos = 1;
        }

        while(charpos < value.length()) {
            buf[bufpos] = (byte)(value.charAt(charpos) - 48 << 4 | value.charAt(charpos + 1) - 48);
            charpos += 2;
            ++bufpos;
        }

    }
}
