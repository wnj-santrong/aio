package com.santrong.system.status;

import java.util.Hashtable;

/**
 * @author weinianjie
 * @date 2014年7月21日
 * @time 下午5:16:50
 */
public class StatusMgr {
	
	public static int uniVodMax = 0;// 最大直播点播数
	public static int UniUsrCount = 0;// 正在直播人数
	public static int VodUsrCount = 0;// 正在点播人数
	
	// confId--entry
	private static Hashtable<String, RoomStatusEntry> roomlist = new Hashtable<String, RoomStatusEntry>();
	
	public static RoomStatusEntry getRoomStatus(String key) {
		return roomlist.get(key);
	}
	
	public static void setRoomStatus(String key, RoomStatusEntry entry) {
		roomlist.put(key, entry);
	}
	
	public static Hashtable<String, RoomStatusEntry> getHashtable_Room() {
		return roomlist;
	}
}
