package es.ubu.lsi.perikymata.util;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import es.ubu.lsi.perikymata.MainApp;
import javafx.embed.swing.SwingFXUtils;
import javafx.fxml.FXML;
import javafx.scene.shape.LineTo;
import javafx.scene.shape.MoveTo;
import javafx.scene.shape.PathElement;

public class ProfileUtil {
	
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

	private static List<int[]> Bresenham(int x0, int y0, int x1, int y1) {
		// TODO revisar y comentar
		int x, y, dx, dy, p, incE, incNE, stepx, stepy;
		dx = (x1 - x0);
		dy = (y1 - y0);
		List<int[]> llist = new LinkedList<>();
	
		// determinar que punto usar para empezar, cual para terminar */
		if (dy < 0) {
			dy = -dy;
			stepy = -1;
		} else {
			stepy = 1;
		}
	
		if (dx < 0) {
			dx = -dx;
			stepx = -1;
		} else {
			stepx = 1;
		}
	
		x = x0;
		y = y0;
		/* se cicla hasta llegar al extremo de la línea */
		if (dx > dy) {
			p = 2 * dy - dx;
			incE = 2 * dy;
			incNE = 2 * (dy - dx);
			while (x != x1) {
				x = x + stepx;
				if (p < 0) {
					p = p + incE;
				} else {
					y = y + stepy;
					p = p + incNE;
				}
				llist.add(new int[] { x, y });
			}
		} else {
			p = 2 * dx - dy;
			incE = 2 * dx;
			incNE = 2 * (dx - dy);
			while (y != y1) {
				y = y + stepy;
				if (p < 0) {
					p = p + incE;
				} else {
					x = x + stepx;
					p = p + incNE;
				}
				llist.add(new int[] { x, y });
			}
		}
		return llist;
	}

	/**
	 * Uses the pathList of drawn line to get all the pixels that are under the
	 * line.
	 * 
	 * @return List of coordinates of the pixels under the line.
	 */
	public static List<int[]> getProfilePixels(List<PathElement> freeDrawPathList) {
		LinkedList<int[]> profile = new LinkedList<>();
		int x0 = (int) ((MoveTo) (freeDrawPathList.get(0))).getX();
		int y0 = (int) ((MoveTo) (freeDrawPathList.get(0))).getY();
		profile.add(new int[] { x0, y0 });
		int x1;
		int y1;
		for (int i = 1; i < freeDrawPathList.size(); i++) {
			x1 = (int) ((LineTo) (freeDrawPathList.get(i))).getX();
			y1 = (int) ((LineTo) (freeDrawPathList.get(i))).getY();
			profile.addAll(Bresenham(x0, y0, x1, y1));
			x0 = x1;
			y0 = y1;
		}
		return profile;
	}

	/**
	 * Returns these mean of the intensity profile with a width of two pixels at
	 * each side by using the orthogonal vectors of the given the coordinates of
	 * a line.
	 * 
	 * @param profileCoords
	 *            Coordinates of a single-pixeled line.
	 * @return intensity profile
	 */
	public static List<Integer> getIntensityProfile(List<int[]> profileCoords,MainApp mainApp) {
		BufferedImage img = SwingFXUtils.fromFXImage(mainApp.getFullImage(), null);
		return ProfileUtil.getOrthogonalProfile(img, profileCoords, 2);
	}

	/**
	 * Finds the list of maximum local intensities in the profile. If there are
	 * two or more points next to each other with the same intensity, the middle
	 * point is taken.
	 * 
	 * @param profile
	 *            Intensity profile.
	 * @return List of the indexes where perikymata has been found.
	 */
	public static List<Integer> findLocalPeaks(List<Integer> profile) {
		int l = profile.size();
		int lastMaxIndex = 0;
		int lastMaxValue = 0;
		List<Integer> peaks = new ArrayList<>();
	
		for (int i = 0; i < profile.size() - 1; i++) {
	
			if (profile.get(i) > lastMaxValue) {
				// Intensity is growing
				lastMaxValue = profile.get(i);
				lastMaxIndex = i;
			} else if (profile.get(i) < lastMaxValue) {
				// There is a local max, so index is set to a known value.
				lastMaxValue = 0;
				// If There is more than one consecutive max value, the mid
				// point
				// is stored.
				if (lastMaxIndex == i - 1) {
					peaks.add(i - 1);
				} else {
					peaks.add((i + lastMaxIndex) / 2);
				}
			}
		}
		// Check if the profile ends with one or more maxes.
		if (profile.get(l - 1) != 0) {
			if (profile.get(l - 1) == lastMaxValue)
				peaks.add(((l - 1) + lastMaxIndex) / 2);
			else if (profile.get(l - 2) < profile.get(l - 1))
				peaks.add(l - 1);
		}
		return peaks;
	}
}
