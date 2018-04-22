package com.zhangke.shizhong.util;

import com.zhangke.shizhong.common.APPConfig;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.File;

import static org.mockito.Mockito.*;

import static org.junit.Assert.*;

/**
 * PosterUtils测试类
 * Created by ZhangKe on 2018/4/22.
 */
public class PosterUtilsTest {

    @Test
    public void getMoviePosterFileWithName() throws Exception {
        Assert.assertEquals("C:\\这个--杀手-不-太冷.jgp",
                PosterUtils.getMoviePosterFileWithName(
                        new File("C:\\"),
                        "这个 \\杀手 不\\太冷").getPath());
    }

}