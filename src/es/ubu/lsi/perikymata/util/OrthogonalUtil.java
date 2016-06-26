package es.ubu.lsi.perikymata.util;

import java.awt.image.BufferedImage;
import java.util.LinkedList;
import java.util.List;

public class OrthogonalUtil {
	
	/**
	 * Gets the orthogonal line of a point based on the previous and next points, ordered from side to side.
	 * @param pointsVector List of all the points used to get the profile.
	 * @param index index of the current point.
	 * @param length length of the orthogonal line.
	 * @return array of the points of the orthogonal line that surrounds the point. Output is ordered from side to side.
	 */
	private static int[][] ortogonalLineOfAPoint(List<int[]> pointsVector,int index,int length){
	    // distances are dx=x2-x1 and dy=y2-y1, then the normals are (-dy, dx) and (dy, -dx)
	    int[] point = pointsVector.get(index);
	    int[] prev;
	    int[] follow;
	    
	    if (index == 0){
	        prev = point;
	        follow = pointsVector.get(index+1);
        } else if ( index == ((pointsVector.size())-1)){
        	prev = pointsVector.get(index-1);
        	follow=point;
        }
        else{
        	prev = pointsVector.get(index-1);
	        follow = pointsVector.get(index+1);
        }
	    
	    int dx = follow[0]-prev[0];
	    int dy = follow[1]-prev[1];
	    int[] v1 = {-dy,dx};
	    int[] v2 = {dy,-dx};
	    
	    //Array of coords
	    int[][] orthogonal = new int[2*length+1][];
	    int ortIndex = 0;
    
    	// point -> point + (v1 or v2) are the orthonormal vectors to the
    	// prev  -> follow vector, so multiplying by "i" we can get all the
    	// points in these two directions, chess' "horse" movements can give
	    // farther coordinates with a lower max length.
	    
	  //First side vector.
	    for (int i = length-1; i>=0; i--){
	        orthogonal[ortIndex]= new int[]{i*v2[0]+point[0],i*v2[1]+point[1]*i};
	        ortIndex++;
	    }
	    //Original point
	    orthogonal[ortIndex] = point;
	    ortIndex++;
	    //Other side vector.
	    for (int i = 0; i<length; i++){
	        orthogonal[ortIndex]= new int[]{i*v1[0]+point[0],i*v1[1]+point[1]*i};
	        ortIndex++;
	    }
	    
	    return orthogonal;
	}

	/**
	 * Gets the orthogonal profile. Orthogonal means that it's using the pixels of the 
	 * sides of the point to get more data of the image and reduce the error of the profile.
	 * @param image Filtered image to calculate the profile of.
	 * @param pointsVector List of points of the image that are going to be used to calculate the orthogonal profile.
	 * @param length Number of pixels to get from each side.
	 * @return
	 */
	public static List<Integer> getOrthogonalProfile(BufferedImage image, List<int[]> pointsVector,int length){
	    List<Integer> profile= new LinkedList<>();
	    for( int i=0; i<pointsVector.size(); i++){
	    	profile.add(getProfileOfOrthogonalLine(image, ortogonalLineOfAPoint(pointsVector, i, length)));          
	    }
	    return profile;
	}
	
	/**
	 * Gets the intensity of each point of the orthogonal vector and gets the mean. 
	 * @param image Filtered image to calculate the profile of. 
	 * @param orthogonalLine Points of the orthogonal vector.
	 * @return Mean of the intensity of the orthogonal vector.
	 */
	private static int getProfileOfOrthogonalLine(BufferedImage image, int[][] orthogonalLine){
		int sum = 0;
		for(int[] point : orthogonalLine){
	    	//We assume that the image will be in grayScale (8bit) or rgb (24 bits) but
	    	//all the three channels having the same value. r = g = b. 
	    	//gray = r+g+b/3, so if they are the same gray= 3b/b=b, and blue being the 
	    	//8 rightmost bits, we can take these same 8 bits for
	    	//both RGB and GrayScale images.
			sum += image.getRGB(point[0], point[1]) & 0xFF; 
		}
		return sum/orthogonalLine.length;
		
	}
}
