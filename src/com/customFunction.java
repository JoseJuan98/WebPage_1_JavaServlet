package com;

import java.util.Iterator;
import java.util.List;

import es.unex.pi.model.Route;

public class customFunction {
	public static boolean containsBlockedRt(List<Route> list) {
		
		Iterator<Route> itRt = list.iterator();
		
		while(itRt.hasNext()) {
			Route rt = itRt.next();
			if(rt.getBlocked() == 1) {
				return true;
			}
		}
		
		return false;
	}
}
