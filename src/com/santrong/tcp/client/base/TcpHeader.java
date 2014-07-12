package com.santrong.tcp.client.base;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

public class TcpHeader {
	int normalDataLen;	// 标准xml数据长度，4字节
	int privateDataLen;// 私有二进制数据长度，4字节
	
	private ByteBuffer byteBuffer =  ByteBuffer.allocate(8);
	
	public TcpHeader(int normalDataLen , int privateDataLen){
		this.normalDataLen = normalDataLen;
		this.privateDataLen = privateDataLen;
	}
	
	public TcpHeader(int normalDataLen){
		this.normalDataLen = normalDataLen;
		this.privateDataLen = 0;
	}
	
	/**
	 * 旧版Beyonsys录播协议头
	 * @return
	 */
	public byte[] getBytes(){		
		byteBuffer.order(ByteOrder.LITTLE_ENDIAN);
		
		byteBuffer.putInt(normalDataLen);
		byteBuffer.putInt(privateDataLen);
		
		return byteBuffer.array();
	}
   
   public static void main(String args[]){
	   TcpHeader head= new TcpHeader(1025, 2);
	   byte[] b = head.getBytes();
	   
	   ByteBuffer bf =  ByteBuffer.allocate(8);
	   bf.put(b);
	   //ByteBuffer bf2 = bf.slice();
//	   System.out.println(bf.getShort(0));
//	   System.out.println(bf.getShort(2));
//	   System.out.println(bf.getShort(4));
//	   System.out.println(bf.getShort(6));
	   
	   System.out.println(bf.getInt(0));
	   System.out.println(bf.getInt(1));
	   
   }
	
}
