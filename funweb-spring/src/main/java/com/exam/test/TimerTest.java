package com.exam.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;


public class TimerTest {

    public static void main(String[] args) {
        // 데몬스레드 방식은 생성자에 true 설정
        Timer timer = new Timer();

        TimerTask timerTask1 = new TimerTask() {
            @Override
            public void run() {
                System.out.println("timerTask1......");
            }
        };

        String str = "2019-05-21T14:34";
        str = str.replace("T", " ");
        System.out.println(str);

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Date date = null;
        try {
            date = sdf.parse(str);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        //timer.scheduleAtFixedRate(timerTask1, date, 2000);
        
        
        TimerTask timerTask2 = new TimerTask() {
            @Override
            public void run() {
                System.out.println("timerTask2........");
            }
        };
        
        
        timer.scheduleAtFixedRate(timerTask1, 500, 2000);
        timer.scheduleAtFixedRate(timerTask2, 0, 5000);
        
        
        // 각 태스크 개별적으로 취소
        timerTask1.cancel();
        
        // 타이머에 등록된 모든 태스크 취소
        timer.cancel();
    }

}
