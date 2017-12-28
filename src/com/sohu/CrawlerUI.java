package com.sohu;

import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Toolkit;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.JLabel;

import java.util.*;
import java.io.IOException;

import com.sohu.crawler.*;
import com.sohu.*;

/**
 * �������������û�������
 * @author Bob Hu
 *
 */
public class CrawlerUI extends JFrame
{
	//���̹߳�ץȡ��ҳʹ��
    static Thread thread = null;
    
    //һ�龲̬�ı���˵���༭�������
    public JLabel crawlingUrlLabel = null;
    public JLabel inputUrlLabel = null;
    public JLabel inputTimeIntervalLabel = null;
    public JLabel timeElapsceLabel = null;
    public JLabel crawlingNumLabel = null;
    
    //һ��༭���ṩ�û�����
    public JTextField crawlingUrl = null;
    public static JTextField inputUrl = null;
    public static JTextField inputTimeInterval = null;
    public static JTextField timeElapsce = null;
    public static JTextField crawlingNumText = null;
    
    //һ�鰴ť
    public static JButton startButton = null;
    public static JButton stopButton = null;
    public static JButton exitButton = null;
    
    //�û��������
    public static CrawlerUI crawlerUI =null;
    
    //������ʱ��
    //��ʱ�� timer��������ݲɼ�������ʵ�ֶ�ʱ�ɼ�����
    //��ʱ�� auxTimer: ��ʱˢ��һ������������ʾ��һ��"��ʱ�ɼ�"��ʣ��ʱ��
	public Timer timer = new Timer();	
	public Timer auxTimer = new Timer();
	
	//�����������ڼ�¼��һ��"��ʱ�ɼ���ʱ��"
	public static Integer counter = 0;
	public static boolean isCounterInit = false;
	
	//��¼��ǰ��ȡ��url����
	public static Long crawlingNum = 0L; 
	
    public CrawlerUI()
    {
    	crawlerUI = this; 
    }
    
    /**
     *  "��������"���洴������
     */
    public void create()
    {
    	//������
		JFrame frame = new JFrame("��������");
		//���ֹ�����
		frame.setLayout(new FlowLayout()); 
		
		//������̬�ı���
		crawlingUrlLabel =       new JLabel("����ץȡ�� url: ");
		inputUrlLabel =          new JLabel("��������Ҫץȡ�����url��");
		inputTimeIntervalLabel = new JLabel("���趨��ʱץȡ���(s)��");
		timeElapsceLabel =       new JLabel("��ʱץȡʣ��ʱ��(s)��    ");
		crawlingNumLabel =       new JLabel("����ȡurl����(1sˢ��):    "); 
		
		//��������������ı༭��
		inputUrl =          new JTextField("",25);
        inputTimeInterval = new JTextField("",10);    
        crawlingUrl =       new JTextField("",25);  crawlingUrl.setEditable(false); //���ܱ༭
    	timeElapsce =       new JTextField("",10);  timeElapsce.setEditable(false);
    	crawlingNumText =   new JTextField("",10);  crawlingNumText.setEditable(false);
		
    	//������ť
	    startButton =  new JButton("��ʼ");		
	    stopButton =   new JButton("ֹͣ"); stopButton.setEnabled(false);  //���øð�ť
	    exitButton =   new JButton("�˳�");

        //��Ӱ�ť����Ϣ��Ӧ
	    startButton.addActionListener(new Listener_ok());
	    stopButton.addActionListener(new Listener_cancel());		    
	    exitButton.addActionListener(new Listener_exit());
		
	    //�����е������ӵ�����
		frame.add(inputUrlLabel);
		frame.add(inputUrl);
		
		frame.add(crawlingUrlLabel);
		frame.add(crawlingUrl);
		
		frame.add(inputTimeIntervalLabel);
		frame.add(inputTimeInterval);
		
		frame.add(timeElapsceLabel);
		frame.add(timeElapsce);
		frame.add(crawlingNumLabel);
		frame.add(crawlingNumText);
		
		frame.add(startButton);		
		frame.add(stopButton);		
		frame.add(exitButton);
		
		//���ô��ڵĴ�С
		frame.setSize(320,280);
		
		//��ȡ���ڵ�����
		Toolkit    tk  = Toolkit.getDefaultToolkit();	
		int Width  = tk.getScreenSize().width;
	    int Height = tk.getScreenSize().height;

	    //���ô�������Ļ�ϵ���ʾλ��
	    frame.setLocation((Width - 320) / 2, (Height - 280) / 2);
	    
	    //����������ϵ�"X"�ܹ��˳�����
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	   
	    //��ʹ����󻯰�ť
	    frame.setResizable(false);
	    
	    //���ڴ�����ɼ�
		frame.setVisible(true);
    }    

    /**
     * ֹͣ��ť������
     * @author Bob Hu
     *
     */
	class Listener_cancel implements ActionListener
	{
		//ʵ�ֽӿں���
		public void actionPerformed(ActionEvent e)
		{				
			thread.stop();
			
			//����ʹ����Ӧ��ť
			startButton.setEnabled(true);
			inputUrl.setEnabled(true);
            inputTimeInterval.setEditable(true);
			
			//�ı䶨ʱץȡ�������ĳ�ʼ����־
			isCounterInit = false;
			crawlingNum = 0L;

			//ʹ����������˳�����
	        timer.cancel();
	        auxTimer.cancel();	        	
		}
	}
	
	/**
	 * �˳���ť������
	 * @author Bob Hu
	 *
	 */
	class Listener_exit implements ActionListener
	{
		//ʵ�ֽӿں���
		public void actionPerformed(ActionEvent e)
		{
			//�����˳�
			System.exit(1);		
		}
	}
	
	/**
	 * ��ʼ��ť������
	 * @author Bob Hu
	 *
	 */
    class Listener_ok implements ActionListener		
    {    	
    	public void actionPerformed(ActionEvent e)		
    	{
            //��ȡ�û�����Ķ�ʱ���
    		String strTimeInterval = inputTimeInterval.getText();
    		
    		//��ȡ�����url
    		String inputContent = inputUrl.getText();
    		
    		//�ж������url�Ƿ�Ϸ�
    		boolean isInputUrlValid =( inputContent.equals("http://news.sohu.com") );
    		isInputUrlValid  = isInputUrlValid 
    		                || inputContent.matches("http://news.sohu.com/[\\d]+/n[\\d]+.shtml");
    		
    		if (!isInputUrlValid)
    		{
    			//�����Ի�����ʾ
		    	JOptionPane.showMessageDialog(null,
		    			                     "֧�ֵ�urlֻ����http://news.sohu.com"+
		    			                     "���������ӡ������ӵ���ʽΪ"+
		    			                     "http://news.sohu.com/[\\d]+/n[\\d]+.shtml",
		    			                     "url�Ƿ�", 
		    			                     JOptionPane.PLAIN_MESSAGE);
    		}
    		else if (   !strTimeInterval.matches("[\\d]+") 
		        || Long.parseLong(strTimeInterval)==0L )
		    {
		    	//�����Ի�����ʾ
		    	JOptionPane.showMessageDialog(null,
		    			                     "�����趨��ʱץȡʱ����һ������������",
		    			                     "����", 
		    			                     JOptionPane.PLAIN_MESSAGE);
		    }
		    else
		    {
	            //���´�����ʱ��
	    		timer = new Timer();
	    		auxTimer = new Timer();
	    		
	    		//��Ӧ�����ͱ༭���ʹ������仯
	            stopButton.setEnabled(true);
	            inputTimeInterval.setEditable(false);
	            
	    		
	            //��String���͵�ʱ����ת����long��
	    		long longTimeInterval = Long.parseLong(strTimeInterval);
	    		
	    		//����ִ�и����񣬼��ʱ��Ϊ�ڶ�������
	        	timer.schedule(new MyTask(), 0, longTimeInterval * 1000);
	        	auxTimer.schedule(new TimerCounter(), 0, 1000);  
		    }
    	}
    }
    
    /**
     * ��ʱ��timerʹ�õ���
     * @author Bob Hu
     *
     */
    static class MyTask extends java.util.TimerTask
    {
    	@Override
    	public void run() 
    	{		
    		if (thread != null)
    		{
    			thread.stop();
    		}
    		
    		//��Ӧ��ť��ʹ��
    		startButton.setEnabled(false);
    		inputUrl.setEnabled(false);
    		
    		//��ȡurl��ڵ�ַ
    		final String inputContent = inputUrl.getText();
    		
    		final Crawler crawler = new Crawler(crawlerUI,new String[]{inputContent});
    		
            //�����̣߳�������ҳ�Ľ���
            thread = new Thread (new Runnable() 
            {
                public void run() 
                {
                	crawler.crawling();
                }
            } );
          
            thread.start();  
    	}
   	}
    
    /**
     * ��ʱ��auxTimerʹ�õ���
     * @author Bob Hu
     *
     */
    static class TimerCounter extends java.util.TimerTask
    {
    	@Override
    	public void run()
    	{   
    		//��ʾ�Ѿ�ץȡ��url����
    		String strCrawlingNum = crawlingNum.toString();
    		crawlingNumText.setText(strCrawlingNum);
    		
    		// ��һ�ν��붨ʱ������ȡ��ʱ���
    		if (!isCounterInit)
    		{
    		    String strTimeInterval = inputTimeInterval.getText();    		    	
    		    counter = Integer.valueOf(strTimeInterval).intValue();
    		    isCounterInit = true;
    		}
    		
    		//�������ݼ�������ʾ�ڽ����ϣ���ɵ���ʱ�Ĺ���
    		counter--;
    		String strCounter = counter.toString();
    		timeElapsce.setText(strCounter);
    		
    		//�ڶ��ֶ�ʱ��ʼ
    		if(counter==0)
    		{
    			//��ȡ�û�����Ķ�ʱ���
    			String strTimeInterval = inputTimeInterval.getText();
    			counter = Integer.valueOf(strTimeInterval).intValue();
    			
    			crawlingNum = 0L;
    		}
    	}
    }    
    
    public static void main(String[]args)
    {
        CrawlerUI crawlerUI = new CrawlerUI();
        crawlerUI.create();
    }
}
    


