package com.trendy.fw.common.page;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PageKit {
	private static Logger log = LoggerFactory.getLogger(PageKit.class);

	public static final int OTHER_FLAG = 0;
	public static final int CURRENT_FLAG = 1;

	public static List<int[]> getPageNumberList(PageBean pageBean, int displayTotal) {
		List<int[]> list = new ArrayList<int[]>();

		int[] pageNumRange = getPageNumberRange(pageBean.getTotalPageNum(), pageBean.getCurPageNum(), displayTotal);
		int startPageNum = pageNumRange[0];// 起始显示
		int endPageNum = pageNumRange[1];// 终止显示

		for (int i = startPageNum; i <= endPageNum; i++) {
			if (i == pageBean.getCurPageNum()) {
				list.add(new int[] { i, CURRENT_FLAG });
			} else {
				list.add(new int[] { i, OTHER_FLAG });
			}
		}
		return list;
	}

	public static int[] getPageNumberRange(int totalNum, int curPageNum, int displayTotal) {
		int gapNum = (displayTotal + 1) / 2;// 前后间距
		int startPageNum = 1;// 起始显示
		int endPageNum = totalNum;// 终止显示

		// 首页在间距之内
		int startGap = curPageNum - gapNum;
		if (startGap <= 0) {
			startPageNum = 1;
			if (displayTotal > totalNum) {
				endPageNum = totalNum;
			} else {
				endPageNum = displayTotal;
			}
		}

		// 尾页在间距之内
		int endGap = curPageNum + gapNum;
		if (endGap >= endPageNum) {
			if (displayTotal >= endPageNum) {
				startPageNum = 1;
			} else {
				startPageNum = endPageNum - displayTotal + 1;
			}
		}

		if (startGap > 0 && endGap <= endPageNum) {
			startPageNum = curPageNum - (displayTotal - gapNum);
			endPageNum = curPageNum + (displayTotal - gapNum);
		}

		return new int[] { startPageNum, endPageNum };
	}
}
