package top.damoncai.datamatrix;

import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.imageio.ImageIO;

import org.apache.commons.lang3.StringUtils;

import com.drew.imaging.jpeg.JpegMetadataReader;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.Tag;
import com.drew.metadata.exif.ExifInteropDirectory;

public class ImageUtils {

    /**
     * 获取图片正确显示需要旋转的角度（顺时针）
     *
     * @param filePath
     * @return
     */
    public static int getRotateAngleForPhoto(String filePath) {
        File file = new File(filePath);
        int angle = 0;
        Metadata metadata;
        try {
            metadata = JpegMetadataReader.readMetadata(file);
            Iterable<Directory> directories = metadata.getDirectories();
            metadata.getFirstDirectoryOfType(ExifInteropDirectory.class);
            for (Directory directory : directories) {
                if ("Exif IFD0".equals(directory.getName())) {
                    Collection<Tag> tags = directory.getTags();
                    for (Tag tag : tags) {
                        if ("Orientation".equals(tag.getTagName())) {
                            angle = getNum(tag.getDescription());
                            break;
                        }
                    }
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return angle;
    }

    /**
     * 旋转照片
     *
     * @param fullPath
     * @param angel
     * @return
     */
    public static String rotatePhonePhoto(String fullPath, int angel) {
        BufferedImage src;
        try {
            src = ImageIO.read(new File(fullPath));
            int src_width = src.getWidth(null);
            int src_height = src.getHeight(null);
            int swidth = src_width;
            int sheight = src_height;
            if (angel == 90 || angel == 270) {
                swidth = src_height;
                sheight = src_width;
            }
            Rectangle rect_des = new Rectangle(new Dimension(swidth, sheight));
            BufferedImage res = new BufferedImage(rect_des.width, rect_des.height, BufferedImage.TYPE_INT_RGB);
            Graphics2D g2 = res.createGraphics();
            g2.translate((rect_des.width - src_width) / 2, (rect_des.height - src_height) / 2);
            g2.rotate(Math.toRadians(angel), src_width / 2, src_height / 2);
            g2.drawImage(src, null, null);
            ImageIO.write(res, "jpg", new File(fullPath));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return fullPath;
    }

    private static int getNum(String str) {
        String regEx = "[^0-9]";
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        String numStr = m.replaceAll("").trim();
        if (StringUtils.isNotBlank(numStr)) {
            return Integer.valueOf(numStr);
        } else {
            return 0;
        }
    }

    /**
     * 处理ios照片
     *
     * @param filePath
     */
    public static void processIosPhotos(String filePath) {
        // 计算旋转的角度
        int angle = getRotateAngleForPhoto(filePath);
        if (angle != 0) {
            rotatePhonePhoto(filePath, angle);
        }
    }

    public static void main(String[] args) throws Exception {
        String filePath = "D://code.jpg";
        int angle = getRotateAngleForPhoto(filePath);
        System.out.println("旋转角度:" + angle);
        rotatePhonePhoto(filePath, angle);
        System.out.println("hao");
    }
}
