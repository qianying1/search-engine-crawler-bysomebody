package com.sohu.crawler;

/**
 *  һ���ӿڣ�ʵ���� accept() ���������Գ�ȡ�����ӽ��й��ˡ�
 *  @author Bob Hu
 */
public interface LinkFilter 
{
	public boolean accept(String url);
}