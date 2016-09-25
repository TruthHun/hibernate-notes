package cn.jxzhang.test;

import static org.junit.Assert.*;

/**
 * Created by J.X.Zhang on 2016-09-25.
 */
public class MainTest {
    @org.junit.Test
    public void getEmpById() throws Exception {
        Main m = new Main();
        m.getEmpById(7369);
    }
}