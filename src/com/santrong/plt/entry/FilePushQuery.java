package com.santrong.plt.entry;

import com.santrong.opt.PageQuery;

/**
 * @author weinianjie
 * @date 2014年7月18日
 * @time 下午3:17:03
 */
public class FilePushQuery extends PageQuery {
	private boolean history;

	public boolean isHistory() {
		return history;
	}

	public void setHistory(boolean history) {
		this.history = history;
	}
}
