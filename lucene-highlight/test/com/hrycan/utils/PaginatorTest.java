package com.hrycan.utils;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

/**
 * @author Nicholas Hrycan
 *
 */
public class PaginatorTest {
	private Paginator paginator;
	
	@Test
	public void testCalculateArrayLocation() {
		paginator = new Paginator();
		//hits, page, size
		ArrayLocation loc = paginator.calculateArrayLocation(0, 1, 10);
		assertTrue(loc.getEnd() == 0);
		assertTrue(loc.getStart() == 0);
		
		loc = paginator.calculateArrayLocation(10, 1, 10);
		assertTrue(loc.getStart() == 1);
		assertTrue(loc.getEnd() == 10);
		
		loc = paginator.calculateArrayLocation(20, 2, 10);
		assertTrue(loc.getStart() == 11);
		assertTrue(loc.getEnd() == 20);
		
		loc = paginator.calculateArrayLocation(12, 3, 5);
		assertTrue(loc.getStart() == 11);
		assertTrue(loc.getEnd() == 12);
		
		loc = paginator.calculateArrayLocation(12, 2, 5);
		assertTrue(loc.getStart() == 6);
		assertTrue(loc.getEnd() == 10);
		
		loc = paginator.calculateArrayLocation(12, 4, 5);
		assertTrue(loc.getStart() == 7);
		assertTrue(loc.getEnd() == 12);
		
		loc = paginator.calculateArrayLocation(12, -4, 5);
		assertTrue(loc.getStart() == 0);
		assertTrue(loc.getEnd() == 0);
		
		loc = paginator.calculateArrayLocation(12, 4, -5);
		assertTrue(loc.getStart() == 0);
		assertTrue(loc.getEnd() == 0);
		
		loc = paginator.calculateArrayLocation(121, 2, 50);
		assertTrue(loc.getStart() == 51);
		assertTrue(loc.getEnd() == 100);
		
		loc = paginator.calculateArrayLocation(121, 3, 50);
		assertTrue(loc.getStart() == 101);
		assertTrue(loc.getEnd() == 121);
		
		loc = paginator.calculateArrayLocation(20, 1, 50);
		assertTrue(loc.getStart() == 1);
		assertTrue(loc.getEnd() == 20);
		
		loc = paginator.calculateArrayLocation(20, 2, 50);
		assertTrue(loc.getStart() == 1);
		assertTrue(loc.getEnd() == 20);
		
	}
}
