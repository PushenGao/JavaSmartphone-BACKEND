package test;

import util.DistanceUtil;

public class TestDistanceUtil {
	
	public static void main(String args[]){
		String loc1 = "120.56, 80.94";
		String loc2 = "120.58, 80.92";
		System.out.println(DistanceUtil.getDistance(loc1, loc2));
	}
	
}
