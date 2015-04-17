package util;

public class DistanceUtil {
	private final static double PI = 3.14159265358979323; 
    private final static double R = 6371229;

    public static double getDistance(String location1, String location2) {
    	String[] p1 = location1.split(",");
    	String[] p2 = location2.split(",");
    	double longt1 = Double.parseDouble(p1[0]);
    	double lat1 = Double.parseDouble(p1[1]);
    	double longt2 = Double.parseDouble(p2[0]);
    	double lat2 = Double.parseDouble(p2[1]);
    	
        double x, y, distance;
        x = (longt2 - longt1) * PI * R
                * Math.cos(((lat1 + lat2) / 2) * PI / 180) / 180;
        y = (lat2 - lat1) * PI * R / 180;
        distance = Math.hypot(x, y);
        return distance;
    }
}
