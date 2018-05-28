package com.odde.bbuddy.budget;

import org.junit.Test;

import java.text.SimpleDateFormat;

import static org.junit.Assert.*;

public class NowTest {

    @Test
    public void testGetNowTime() {
        SimpleDateFormat df = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss.SSS");
        String nowTime = df.toString();

        Now now = new Now();
        String time = now.getNowTime();
        String.format()
        assertEquals();

    }

}