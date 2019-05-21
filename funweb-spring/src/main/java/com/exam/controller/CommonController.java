package com.exam.controller;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

import javax.annotation.PostConstruct;
import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import lombok.Setter;
import lombok.extern.log4j.Log4j;

@Controller
@Log4j
public class CommonController {
    
    @Setter(onMethod_ = @Autowired)
    private Timer timer;
    
    @PostConstruct
    public void init() {
        log.info("init() 호출됨...");
        log.info("timer : " + timer);
    }
    
    @GetMapping("/")
    public String main() {
        System.out.println("main() 호출");
        return "main";
    }
    
    @GetMapping("/welcome")
    public String welcome() {
        return "company/welcome";
    }
    
    @GetMapping("/batch/form")
    public void batchForm() {
        log.info("batchForm() 호출됨");
    }
    
    @PostMapping("/batch/process")
    public void batchProcess(@DateTimeFormat(pattern = "yyyy-MM-dd hh:mm") Date date, 
            @RequestParam(required = false) Long period,
            HttpServletRequest request) {
        
        ServletContext application = request.getServletContext();
        
        TimerTask task1 = new TimerTask() {
            @Override
            public void run() {
                log.info("task1........");
            }
        };
        
        timer.scheduleAtFixedRate(task1, date, period);
        
        application.setAttribute("task1", task1);
    }
    
    @GetMapping("/batch/cancel")
    public void batchCancel(HttpServletRequest request) {
        ServletContext application = request.getServletContext();
        
        TimerTask task1 = (TimerTask) application.getAttribute("task1");
        if (task1 != null) task1.cancel();
    }
    
}
