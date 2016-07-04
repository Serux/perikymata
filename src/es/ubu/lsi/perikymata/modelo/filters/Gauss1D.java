package es.ubu.lsi.perikymata.modelo.filters;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Gauss1D {
	
	/**
	 * Default Constructor of the 1D filter.
	 */
	public Gauss1D(){
	}
	
	/**
	 * Returns an array with the shape of the upper part of a sin.
	 * @param size Size of the array.
	 * @return array of floats with an upper sin distribution.
	 */
	private float[] calculateHannArray(int size){
		float[] out= new float[size];
		for(int i=0;i<size;i++){
			out[i]=(float) (0.5*(1-(Math.cos((2*Math.PI*i)/(size-1)))));
		}
		return out;
	}
	
	/**
	 * Convolves an array using a Hann distribution of the given size.
	 * @param in Array to convolve.
	 * @param size Size of the Hann Function (upper sin)
	 * @return Convolved Array
	 */
	public List<Integer> convolve1D(List<Integer> in, int size)
	{
		List ret = new ArrayList<>(in.size());
		float[] kernel = calculateHannArray(size);
	    int i, j, k;
	    int[] out = new int[in.size()+(kernel.length/2)];

	    // convolution from out[0] to out[kernelSize-2]
	    for(i = 0; i < kernel.length - 1; ++i)
	    {
	        out[i] = 0;  // init to 0 before sum
	        for(j = i, k = 0; j >= 0; --j, ++k)
	        	if(i<out.length && j<in.size())
	        		out[i] += in.get(j) * kernel[k];
	    }
	    
	    // start convolution from out[kernelSize-1] to out[dataSize-1] (last)
	    for(i = (kernel.length-1); i < out.length; ++i)
	    {
	        out[i] = 0;  // init to 0 before accumulate
	        for(j = i, k = 0; k < kernel.length; --j, ++k)
	        	if(j<in.size())
	        		out[i] += in.get(j) * kernel[k];
	    }
	    //Remove Zero-Values at the start of the array.
	    int[] fixedArray = Arrays.copyOfRange(out,Math.round(kernel.length/2)-2,out.length);
	    //Convert to List and return.
	    return IntStream.of(fixedArray).boxed().collect(Collectors.toList());
	}


}

