package com.santrong.system.tip;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

/**
 * @author weinianjie
 * @date 2014年8月22日
 * @time 上午11:34:16
 */
public class TipService {
	public static int Disk_Lack = 100;// 磁盘空间不足
	
	private static HashMap<Integer, TipItem> tips = new HashMap<Integer, TipItem>();
	
	public TipItem getTip(int key) {
		return tips.get(key);
	}
	
	public void setTip(int key, TipItem value) {
		tips.put(key, value);
	}
	
	public HashMap<Integer, TipItem> getMap() {
		return tips;
	}
	
	public TipItem removeTip(int key) {
		return tips.remove(key);
	}
	
	public List<TipItem> getList() {
		List<TipItem> tipList = new ArrayList<TipItem>();
		for(Iterator<Integer> itor = tips.keySet().iterator(); itor.hasNext();) {
			tipList.add(tips.get(itor.next()));
		}
		return tipList;
	}
}
