package top.damoncai.datamatrix;

import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.LuminanceSource;
import com.google.zxing.Result;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.datamatrix.DataMatrixReader;

import com.google.zxing.client.j2se.BufferedImageLuminanceSource;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.net.URLDecoder;

public class Demo_03_zxing {

    public static void main(String[] args) {
        try {

            DataMatrixDecoder DataMatrixdecode = new DataMatrixDecoder();
            System.out.println("解析成功");
            System.out.println("内容为：" + DataMatrixdecode.text);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}


class DataMatrixDecoder {
    String text;
    DataMatrixDecoder(){
        try {
            String imagePath = "D:\\k2.jpg";
            File file = new File(imagePath);
            DataMatrixReader reader = new DataMatrixReader();
            BufferedImage image = ImageIO.read(file);
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap imageBinaryBitmap = new BinaryBitmap(binarizer);
            Result result = reader.decode(imageBinaryBitmap);
            text = new String(URLDecoder.decode(result.getText(), "utf-8").getBytes("iso-8859-1"),"UTF-8");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
