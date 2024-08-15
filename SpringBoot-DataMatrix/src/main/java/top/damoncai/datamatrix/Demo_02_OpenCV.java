package top.damoncai.datamatrix;

import org.bytedeco.opencv.opencv_core.StringVector;
import org.bytedeco.opencv.opencv_wechat_qrcode.WeChatQRCode;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.Mat;


public class Demo_02_OpenCV {

    public static void main(String[] args) {

        Mat img = opencv_imgcodecs.imread("D://code1.jpg");

        System.out.println(deCode(img));

    }

    public static String deCode(Mat img) {
        //微信二维码对象，要返回二维码坐标前2个参数必传；后2个在二维码小或不清晰时必传。
        WeChatQRCode we = new WeChatQRCode();

        List<Mat> points = new ArrayList<Mat>();
        //微信二维码引擎解码，返回的valList中存放的是解码后的数据，points中Mat存放的是二维码4个角的坐标
        StringVector stringVector = we.detectAndDecode(img);

        System.out.println(stringVector.get(0).getString(StandardCharsets.UTF_8));
        return stringVector.get(0).getString(StandardCharsets.UTF_8);
    }
}
