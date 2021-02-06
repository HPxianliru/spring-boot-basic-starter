package com.xian.test.byteBuddy;

import net.bytebuddy.ByteBuddy;
import net.bytebuddy.dynamic.loading.ClassLoadingStrategy;

/**
 *
 * @Description
 * @Author: xlr
 *@Date: Created in 9:45 下午 2020/12/24
 */
public class ByteDuddyTest {

    public static void main(String[] args) {
        Class<?> claszz = new ByteBuddy().subclass( Object.class ).name( "com.xxx.type" )
                .make()
                .load( ByteDuddyTest.class.getClassLoader(), ClassLoadingStrategy.Default.WRAPPER )
                .getLoaded();

        System.out.printf( claszz.getName() );
    }
}
