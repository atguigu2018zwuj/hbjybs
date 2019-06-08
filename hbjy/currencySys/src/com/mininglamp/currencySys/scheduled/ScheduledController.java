package com.mininglamp.currencySys.scheduled;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;

@Controller
public class ScheduledController {
	
	/*@Scheduled(cron="0/5 * * * * ?")*/			
	public void GZSB(){
		System.out.println("定时任务一已执行!");
	}

}
