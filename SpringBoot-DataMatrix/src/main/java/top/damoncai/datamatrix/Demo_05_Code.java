package top.damoncai.datamatrix;

import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;


import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.zxing.Binarizer;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.EncodeHintType;
import com.google.zxing.LuminanceSource;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.NotFoundException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.common.HybridBinarizer;


public class Demo_05_Code {

    /**
     *
     * @Title: deEncodeByPath
     * @Description: 替换原图片里面的二维码
     * @param @param filePath
     * @param @param newPath    设定文件
     * @return void    返回类型
     * @throws
     */
    public static void deEncodeByPath(String filePath, String newPath) {

        // 原图里面二维码的url
        String originalURL = null;
        try {
            // 将远程文件转换为流
            BufferedImage readImage = ImageIO.read(new File(filePath));
            LuminanceSource source = new BufferedImageLuminanceSource(readImage);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);

            Map<DecodeHintType, Object> hints = new HashMap<DecodeHintType, Object>();
            hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
            Result result = null;
            result = new MultiFormatReader().decode(binaryBitmap, hints);
            originalURL = result.getText();

            // 解码
            ResultPoint[] resultPoint = result.getResultPoints();
            System.out.println("原二维码里面的url:" + originalURL + ",\npoints1： " + resultPoint[0] + ",\npoints2： " + resultPoint[1] + ",\npoints2： "
                    + resultPoint[2] + ",\npoints2： " + resultPoint[3]);

            // 获得二维码坐标
            float point1X = resultPoint[0].getX();
            float point1Y = resultPoint[0].getY();
            float point2X = resultPoint[1].getX();
            float point2Y = resultPoint[1].getY();

            // 替换二维码的图片文件路径
            BufferedImage writeFile = ImageIO.read(new File(newPath));

            // 宽高
            final int w = (int) Math
                    .sqrt(Math.abs(point1X - point2X) * Math.abs(point1X - point2X) + Math.abs(point1Y - point2Y) * Math.abs(point1Y - point2Y))
                    + 12 * (7 - 1);
            final int h = w;

            Hashtable<EncodeHintType, Object> hints2 = new Hashtable<EncodeHintType, Object>();
            hints2.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);
            hints2.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            hints2.put(EncodeHintType.MARGIN, 1);

            Graphics2D graphics = readImage.createGraphics();
            //此处,偏移,会有定位问题
            int x = Math.round(point1X) - 36;
            int y = Math.round(point2Y) - 36;

            // 开始合并绘制图片
            graphics.drawImage(writeFile, x, y, w, h, null);
            // logo边框大小
            graphics.setStroke(new BasicStroke(2));
            // //logo边框颜色
            graphics.setColor(Color.WHITE);
            graphics.drawRect(x, y, w, h);
            readImage.flush();
            graphics.dispose();

            // 打印替换后的图片
            ImageIO.write(readImage, "jpg", new File("D://code222.jpg"));
        }

        catch (IOException e) {
//            log.error("资源读取失败" + e.getMessage());
            e.printStackTrace();
        }
        catch (NotFoundException e) {
//            log.error("读取图片二维码坐标前发生异常:" + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        deEncodeByPath("D:\\code1.png", "D:\\code1.png");
    }

}
