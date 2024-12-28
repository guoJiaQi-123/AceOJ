package com.tyut.ojcodebox.errorfiles;

/**
 * @version v1.0
 * @author OldGj 2024/12/28
 * @apiNote 无限睡眠
 */
public class SleepError {
    public static void main(String[] args) {
        long ONE_HOUR = 60 * 1000 * 60;
        try {
            Thread.sleep(ONE_HOUR);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }
}
