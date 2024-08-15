package top.damoncai.datamatrix;

import com.aspose.barcode.barcoderecognition.BarCodeReader;
import com.aspose.barcode.barcoderecognition.BarCodeResult;
import com.aspose.barcode.barcoderecognition.DecodeType;

public class Demo_01_Recognize {

    public static void main(String[] args) {
        BarCodeReader reader = new BarCodeReader("D://code.jpg", DecodeType.DATA_MATRIX);

        for (BarCodeResult result : reader.readBarCodes()) {
            System.out.println("BarCode CodeText: " + result.getCodeText());
            System.out.println("BarCode CodeType: " + result.getCodeTypeName());
        }
    }
}
