package com.hrycan.utils;

/**
 * @author Nicholas Hrycan
 *
 */
public class Paginator {

	public ArrayLocation calculateArrayLocation(int totalHits, int pageNumber, int pageSize) {
		ArrayLocation al = new ArrayLocation();
		
		if (totalHits < 0 || pageNumber < 1 || pageSize < 1) {
			al.setStart(0);
			al.setEnd(0);
			return al;
		}
		
		int start = (pageNumber - 1) * pageSize;
		int end = Math.min(pageNumber * pageSize, totalHits);
		if (start > end) {
			start = Math.max(0, end - pageSize);
		}
		
		al.setStart(start);
		al.setEnd(end);
		return al;
	}
}
