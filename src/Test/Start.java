package Test;

import java.util.Date;
import java.util.Timer;

import com.sohu.bean.searchBean;

public class Start {

	public static void main(String[] args) {
		 Date date=new Date();//获取当前时间
	   	  int delay=0;
	   	  
	   	  //恰好在整8点或者整20点开启服务器，则立马更新数据库
	   	  if((date.getHours()==8 || date.getHours()==20) && 
	   			  date.getMinutes()==0 && date.getSeconds()==0)
	   		  delay=0;
	   	  else if(date.getHours()<8)//0~8点之启动，则在8:00更新数据库
	   		  delay=(7-date.getHours())*60*60*1000+(59-date.getMinutes())
	   		         *60*1000+(60-date.getSeconds())*1000;
	   	  else if(date.getHours()<20){//8~20点之间启动服务器，则在20:00更新数据库
	   		  delay=(19-date.getHours())*60*60*1000+(59-date.getMinutes())
	    		         *60*1000+(60-date.getSeconds())*1000;
			}
	   	  else{//20~24点之间启动服务器，则在第二天8:00更新数据库
	   		  delay=(23-date.getHours())*60*60*1000+(59-date.getMinutes())
	     		         *60*1000+(60-date.getSeconds())*1000+
	     		         8*60*60*1000;
	   	  }
	   	  
	   	  System.out.println("还有 "+delay/1000+" 秒更新数据库~");
	   	  Timer timer =new Timer();
	   	  //根据时延确定开启服务器,并且每隔12个小时更新一次数据库
	     	  timer.schedule(new MyTask(),delay, 12*60*60*1000);
	}
	
	static class MyTask extends java.util.TimerTask
    {
    	@Override
    	public void run() 
    	{		
    		searchBean data = new searchBean();
    		//data.executeUpdate("truncate table dbo.news");
            
    	}
   	}

}
