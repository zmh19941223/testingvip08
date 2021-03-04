package com.testing.class15;

import com.testing.common.Encrypt;

/**
 * @Classname EncryptTest
 * @Description 类型说明
 * @Date 2021/2/23 20:20
 * @Created by 特斯汀Roy
 */
public class EncryptTest {
    public static void main(String[] args) {
        Encrypt enc=new Encrypt();
        String encPwd = enc.enCrypt("123456");
        System.out.println(encPwd);
        System.out.println(enc.deCrypt(encPwd));
    }
}
