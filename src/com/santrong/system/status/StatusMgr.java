package com.santrong.system.status;

import java.util.Hashtable;

/**
 * @author weinianjie
 * @date 2014年7月21日
 * @time 下午5:16:50
 */
public class StatusMgr {
	
	private static Hashtable<String, RoomStatusEntry> roomlist = new Hashtable<String, RoomStatusEntry>();
	
	public static RoomStatusEntry getRoomStatus(String key) {
		return roomlist.get(key);
	}
	
	public static void setRoomStatus(String key, RoomStatusEntry entry) {
		roomlist.put(key, entry);
	}
}
