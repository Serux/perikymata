#include < stdio.h >  
#include < opencv2\opencv.hpp >  
#include < opencv2\stitching\stitcher.hpp >

#ifdef _DEBUG  
#pragma comment(lib, "opencv_core2411d.lib")   
#pragma comment(lib, "opencv_imgproc2411d.lib")   //MAT processing  
#pragma comment(lib, "opencv_highgui2411d.lib")  
#pragma comment(lib, "opencv_stitching2411d.lib")

#else  
#pragma comment(lib, "opencv_core2411.lib")  
#pragma comment(lib, "opencv_imgproc2411.lib")  
#pragma comment(lib, "opencv_highgui2411.lib")  
#pragma comment(lib, "opencv_stitching2411.lib")
#endif  

using namespace cv;
using namespace std;


int main(int argc, char** argv)
{
	vector< Mat > vImg;
	vImg.clear();
	Mat rImg;


	
	for (int i = 1; i < argc; i++)
	{
		vImg.push_back(imread(argv[i]));
	}
	


	Stitcher stitcher = Stitcher::createDefault();
	
	unsigned long AAtime = 0, BBtime = 0; //check processing time
	AAtime = getTickCount(); //check processing time

	Stitcher::Status status = stitcher.stitch(vImg, rImg);

	BBtime = getTickCount(); //check processing time 
	printf("%.2lf sec \n", (BBtime - AAtime) / getTickFrequency()); //check processing time
	
	if (Stitcher::OK == status) {
		//imshow("Stitching Result", rImg);
		imwrite("resultado.tif", rImg);
	}
	else {
		printf("Stitching fail.");
	}
	
	rImg.release();
	vImg.clear();
	
	return Stitcher::OK;
}