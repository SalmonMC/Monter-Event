package me.AmazeMC.mobevent.utils;

import java.util.Random;

public class NumberUtils {

    public static Integer randomizeInt(int number) {
        Random r = new Random();
        return r.nextInt(number);
    }
}
