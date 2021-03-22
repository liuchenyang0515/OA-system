package com.me.oa.utils;

import org.apache.commons.codec.digest.DigestUtils;

public class MD5Utils {
    public static String md5Digest(String source) {
        return DigestUtils.md5Hex(source);
    }

    /**
     * 对源数据加盐混淆后生成MD5摘要
     * @param source 源数据
     * @param salt 盐值
     * @return 摘要
     */
    public static String md5Digest(String source, Integer salt) {
        char[] ca = source.toCharArray();// 字符数组
        // 对每个字符混淆处理
        for (int i = 0; i < ca.length; ++i) {
            ca[i] = (char) (ca[i] + salt);
        }
        String target = new String(ca);
//        System.out.println(target);
        // 对混淆后的字符进行MD5处理
        String md5 = DigestUtils.md5Hex(target);
        return md5;
    }

    public static void main(String[] args) {
        // 为了混淆力度更大，建议盐值是3位的整数
        System.out.println(MD5Utils.md5Digest("test", 197));
        MD5Utils.md5Digest("我爱你中国", 999);
    }
}
