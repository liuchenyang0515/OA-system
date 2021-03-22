package com.me.oa.utils;

import org.junit.Test;

public class MD5UtilsTest {

    @Test
    public void md5Digest() {
        // 即便只是MD5，根据彩虹表也可以很容易的反向推出简单密码，所以还需要进一步处理
        System.out.println(MD5Utils.md5Digest("test"));
        System.out.println(MD5Utils.md5Digest("test"));
    }
}