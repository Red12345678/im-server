package com.yk;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
        assertTrue( true );
    }

    public static void main(String[] args) {
        //
        String url="https://demo.xiaoheiban.cn/api/add/classroom/teacher/5a4f2234fcfa4b36fe380b10";
        String param="mobile=18271632161&memberName=物理班&classroomId=5ab86890fcfa4b7d5f9acc04";
        String s = HttpUtil.post(url,param,null);
        System.out.println(s);
    }
}
