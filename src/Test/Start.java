package Test;

import java.util.Date;
import java.util.Timer;

import com.sohu.bean.searchBean;

public class Start {

	public static void main(String[] args) {
		 Date date=new Date();//��ȡ��ǰʱ��
	   	  int delay=0;
	   	  
	   	  //ǡ������8�������20�㿪����������������������ݿ�
	   	  if((date.getHours()==8 || date.getHours()==20) && 
	   			  date.getMinutes()==0 && date.getSeconds()==0)
	   		  delay=0;
	   	  else if(date.getHours()<8)//0~8��֮����������8:00�������ݿ�
	   		  delay=(7-date.getHours())*60*60*1000+(59-date.getMinutes())
	   		         *60*1000+(60-date.getSeconds())*1000;
	   	  else if(date.getHours()<20){//8~20��֮������������������20:00�������ݿ�
	   		  delay=(19-date.getHours())*60*60*1000+(59-date.getMinutes())
	    		         *60*1000+(60-date.getSeconds())*1000;
			}
	   	  else{//20~24��֮�����������������ڵڶ���8:00�������ݿ�
	   		  delay=(23-date.getHours())*60*60*1000+(59-date.getMinutes())
	     		         *60*1000+(60-date.getSeconds())*1000+
	     		         8*60*60*1000;
	   	  }
	   	  
	   	  System.out.println("���� "+delay/1000+" ��������ݿ�~");
	   	  Timer timer =new Timer();
	   	  //����ʱ��ȷ������������,����ÿ��12��Сʱ����һ�����ݿ�
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
