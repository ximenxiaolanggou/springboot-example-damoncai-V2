package top.damoncai.datamatrix;

import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;

import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Demo_04_Corrent {

    static{
        System.load("C://opencv//opencv//build//java//x64//opencv_java460.dll");
    }
    public static void main(String[] args) {
        String imgUrl = "D://code.jpg";
        Mat src = Imgcodecs.imread(imgUrl ,1);
        Mat src_gray = new Mat();
        test1(src,src_gray);
    }

    public static void test1(Mat src ,Mat src_gray){
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        List<MatOfPoint> markContours = new ArrayList<MatOfPoint>();
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        /**图片太小就放大**/
        if (src.width()*src.height()<90000){
            Imgproc.resize(src,src,new Size(800,600));
        }
        Mat src_all=src.clone();
        //彩色图转灰度图
        Imgproc.cvtColor(src ,src_gray ,Imgproc.COLOR_RGB2GRAY);
        //对图像进行平滑处理
        Imgproc.GaussianBlur(src_gray, src_gray, new Size(3,3), 0);
        /**Imgcodecs.imwrite("F:\\output\\EH.jpg", src_gray);**/
        Imgproc.Canny(src_gray,src_gray,112,255);

        /**Imgcodecs.imwrite("F:\\output\\1-2.jpg", src_gray);**/
        Mat hierarchy = new Mat();
        Imgproc.findContours(src_gray ,contours ,hierarchy ,Imgproc.RETR_TREE ,Imgproc.CHAIN_APPROX_NONE);

        for ( int i = 0; i< contours.size(); i++ ) {
            MatOfPoint2f newMtx = new MatOfPoint2f( contours.get(i).toArray() );
            RotatedRect rotRect = Imgproc.minAreaRect( newMtx );
            double w = rotRect.size.width;
            double h = rotRect.size.height;
            double rate =  Math.max(w, h)/Math.min(w, h) ;
            /***
             * 长短轴比小于1.3，总面积大于60
             */
            if (rate < 1.3 && w < src_gray.cols()/4 && h<src_gray.rows()/4 && Imgproc.contourArea(contours.get(i))>60) {
                /***
                 * 计算层数，二维码角框有五层轮廓（有说六层），这里不计自己这一层，有4个以上子轮廓则标记这一点
                 */
                double[] ds = hierarchy.get(0, i);
                if (ds != null && ds.length>3){
                    int count =0;
                    if (ds[3] == -1){/**最外层轮廓排除*/
                        continue;
                    }
                    /***
                     * 计算所有子轮廓数量
                     */
                    while ((int) ds[2] !=-1){
                        ++count;
                        ds = hierarchy.get(0 ,(int) ds[2]);
                    }
                    if (count >= 4){
                        markContours.add(contours.get(i));
                    }
                }
            }
        }
        /**
         * 这部分代码画框，调试用**/
        for(int i=0; i<markContours.size(); i++){
            Imgproc.drawContours(src_all,markContours,i,new Scalar(0,255,0) ,-1);
        }
        Imgcodecs.imwrite("D:\\output\\2-1.jpg", src_all);

        /***
         * 二维码有三个角轮廓，少于三个的无法定位放弃，多余三个的循环裁剪出来
         */
        if (markContours.size() < 3){
            return;
        }else{
            for (int i=0; i<markContours.size()-2; i++){
                List<MatOfPoint> threePointList = new ArrayList<>();
                for (int j=i+1;j<markContours.size()-1; j++){
                    for (int k=j+1;k<markContours.size();k++){
                        threePointList.add(markContours.get(i));
                        threePointList.add(markContours.get(j));
                        threePointList.add(markContours.get(k));
                        capture(threePointList ,src ,i+"-"+j+"-"+k);
                        threePointList.clear();
                    }
                }
            }
        }
    }

    /***
     * 另一种实现，识别能力比第一种弱
     * @param src
     * @param src_gray
     */
    public static void test2(Mat src,Mat src_gray){
        List<MatOfPoint> contours = new ArrayList<MatOfPoint>();
        List<MatOfPoint> markContours = new ArrayList<MatOfPoint>();
        if (src.width()*src.height()<90000){
            Imgproc.resize(src,src,new Size(800,600));
        }
        Mat src_all=src.clone();
        Mat threshold_output = new Mat();

        //彩色图转灰度图
        Imgproc.cvtColor(src ,src_gray ,Imgproc.COLOR_RGB2GRAY);
        //对图像进行平滑处理
        Imgproc.blur(src_gray ,src_gray ,new Size(3,3));
        Imgproc.equalizeHist(src_gray,src_gray);
        //指定112阀值进行二值化
        Imgproc.threshold(src_gray ,threshold_output,112,255 ,Imgproc.THRESH_BINARY );

        /**Imgcodecs.imwrite("F:\\output\\1-2.jpg", threshold_output);**/
        Mat hierarchy = new Mat();
        Imgproc.findContours(threshold_output ,contours ,hierarchy ,Imgproc.RETR_TREE ,Imgproc.CHAIN_APPROX_SIMPLE);

        int c=0,ic=0,area=0;
        int parentIdx=-1;
        for ( int i = 0; i< contours.size(); i++ ) {
            double[] ds = hierarchy.get(0, i);
            int k=i;
            if (ds == null) {
                continue;
            }
            if (ds != null && ds.length>3){
                int count =0;
                if (ds[3] == -1){
                    continue;
                }
                while ((int) ds[2] !=-1){
                    ++count;
                    ds = hierarchy.get(0 ,(int) ds[2]);
                }
                if (count >= 2){
                    markContours.add(contours.get(i));
                }
            }
        }
        Point[] point = new Point[markContours.size()];
        for(int i=0; i<markContours.size(); i++)
        {
            point[i] = centerCal(markContours.get(i));
        }

    }


    /**
     * 对图片进行矫正，裁剪
     * @param contours
     * @param src
     * @param idx
     */
    public static void capture(List<MatOfPoint> contours ,Mat src ,String idx){
        Point[] pointthree = new Point[3];
        for(int i=0; i<contours.size(); i++)
        {
            pointthree[i] = centerCal(contours.get(i));
        }

        /**画线
         * **/
        Mat sline = src.clone();
        Imgproc.line(sline ,pointthree[0],pointthree[1] ,new Scalar(0,0,255),2);
        Imgproc.line(sline ,pointthree[1],pointthree[2] ,new Scalar(0,0,255),2);
        Imgproc.line(sline ,pointthree[0],pointthree[2] ,new Scalar(0,0,255),2);
        Imgcodecs.imwrite("D:\\output\\cvRio-"+idx+".jpg", sline);

        double[] ca = new double[2];
        double[] cb = new double[2];

        ca[0] =  pointthree[1].x - pointthree[0].x;
        ca[1] =  pointthree[1].y - pointthree[0].y;
        cb[0] =  pointthree[2].x - pointthree[0].x;
        cb[1] =  pointthree[2].y - pointthree[0].y;
      /*  if (Math.max(ca[0],cb[0])/Math.min(ca[0],cb[0]) > 1.5 || Math.max(ca[1],cb[1])/Math.min(ca[1],cb[1])>1.3){
            return;
        }*/
        double angle1 = 180/3.1415*Math.acos((ca[0]*cb[0]+ca[1]*cb[1])/(Math.sqrt(ca[0]*ca[0]+ca[1]*ca[1])*Math.sqrt(cb[0]*cb[0]+cb[1]*cb[1])));
        double ccw1;
        if(ca[0]*cb[1] - ca[1]*cb[0] > 0) {
            ccw1 = 0;
        } else {
            ccw1 = 1;
        }
        ca[0] =  pointthree[0].x - pointthree[1].x;
        ca[1] =  pointthree[0].y - pointthree[1].y;
        cb[0] =  pointthree[2].x - pointthree[1].x;
        cb[1] =  pointthree[2].y - pointthree[1].y;
        double angle2 = 180/3.1415*Math.acos((ca[0]*cb[0]+ca[1]*cb[1])/(Math.sqrt(ca[0]*ca[0]+ca[1]*ca[1])*Math.sqrt(cb[0]*cb[0]+cb[1]*cb[1])));
        double ccw2;
        if(ca[0]*cb[1] - ca[1]*cb[0] > 0) {
            ccw2 = 0;
        }else {
            ccw2 = 1;
        }

        ca[0] =  pointthree[1].x - pointthree[2].x;
        ca[1] =  pointthree[1].y - pointthree[2].y;
        cb[0] =  pointthree[0].x - pointthree[2].x;
        cb[1] =  pointthree[0].y - pointthree[2].y;
        double angle3 = 180/3.1415*Math.acos((ca[0]*cb[0]+ca[1]*cb[1])/(Math.sqrt(ca[0]*ca[0]+ca[1]*ca[1])*Math.sqrt(cb[0]*cb[0]+cb[1]*cb[1])));
        int ccw3;
        if(ca[0]*cb[1] - ca[1]*cb[0] > 0) {
            ccw3 = 0;
        }else {
            ccw3 = 1;
        }

        System.out.println("angle1:"+angle1+",angle2:"+angle2+",angle3:"+angle3);
        if (Double.isNaN(angle1) || Double.isNaN(angle2) || Double.isNaN(angle3)){
            return;
        }

        Point[] poly= new Point[4];
        if(angle3>angle2 && angle3>angle1)
        {
            if(ccw3==1)
            {
                poly[1] = pointthree[1];
                poly[3] = pointthree[0];
            }
            else
            {
                poly[1] = pointthree[0];
                poly[3] = pointthree[1];
            }
            poly[0] = pointthree[2];
            Point temp = new Point(pointthree[0].x + pointthree[1].x - pointthree[2].x , pointthree[0].y + pointthree[1].y - pointthree[2].y );
            poly[2] = temp;
        } else if(angle2>angle1 && angle2>angle3)
        {
            if(ccw2==1)
            {
                poly[1] = pointthree[0];
                poly[3] = pointthree[2];
            }
            else
            {
                poly[1] = pointthree[2];
                poly[3] = pointthree[0];
            }
            poly[0] = pointthree[1];
            Point temp = new Point(pointthree[0].x + pointthree[2].x - pointthree[1].x , pointthree[0].y + pointthree[2].y - pointthree[1].y );
            poly[2] = temp;
        } else if(angle1>angle2 && angle1 > angle3)
        {
            if(ccw1==1)
            {
                poly[1] = pointthree[1];
                poly[3] = pointthree[2];
            }
            else
            {
                poly[1] = pointthree[2];
                poly[3] = pointthree[1];
            }
            poly[0] = pointthree[0];
            Point temp = new Point(pointthree[1].x + pointthree[2].x - pointthree[0].x , pointthree[1].y + pointthree[2].y - pointthree[0].y );
            poly[2] = temp;
        }

        Point[] trans=new Point[4];

        int temp =50;
        trans[0] = new Point(0+temp,0+temp);
        trans[1] = new Point(0+temp,100+temp);
        trans[2] = new Point(100+temp,100+temp);
        trans[3] = new Point(100+temp,0+temp);

        double maxAngle = Math.max(angle3,Math.max(angle1,angle2));
        System.out.println(maxAngle);
        if (maxAngle<75 || maxAngle>115){ /**二维码为直角，最大角过大或者过小都判断为不是二维码*/
            return;
        }

        Mat perspectiveMmat=Imgproc.getPerspectiveTransform(Converters.vector_Point_to_Mat(Arrays.asList(poly),CvType.CV_32F),Converters.vector_Point_to_Mat(Arrays.asList(trans),CvType.CV_32F)); //warp_mat
        Mat dst = new Mat();
        //计算变换结果
        Imgproc.warpPerspective(src,dst ,perspectiveMmat,src.size(),Imgproc.INTER_LINEAR);

        Rect roiArea = new Rect(0, 0, 200, 200);
        Mat dstRoi = new Mat(dst, roiArea);
        Imgcodecs.imwrite("F:\\output\\dstRoi-"+idx+".jpg", dstRoi);
    }

    public static BufferedImage toBufferedImage(Mat m) {
        int type = BufferedImage.TYPE_BYTE_GRAY;

        if (m.channels() > 1) {
            type = BufferedImage.TYPE_3BYTE_BGR;
        }

        int bufferSize = m.channels() * m.cols() * m.rows();
        byte[] b = new byte[bufferSize];
        m.get(0, 0, b); // get all the pixels
        BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);

        final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        System.arraycopy(b, 0, targetPixels, 0, b.length);

        return image;
    }

    public static Point centerCal(MatOfPoint matOfPoint){
        double centerx=0,centery=0;
        int size = matOfPoint.cols();
        MatOfPoint2f mat2f = new MatOfPoint2f( matOfPoint.toArray() );
        RotatedRect rect = Imgproc.minAreaRect( mat2f );
        Point vertices[] = new Point[4];
        rect.points(vertices);
        centerx = ((vertices[0].x + vertices[1].x)/2 + (vertices[2].x + vertices[3].x)/2)/2;
        centery =  ((vertices[0].y + vertices[1].y)/2 + (vertices[2].y + vertices[3].y)/2)/2;
        Point point= new Point(centerx,centery);
        return point;
    }
}
