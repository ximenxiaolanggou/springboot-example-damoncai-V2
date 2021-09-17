package top.damoncai.easyexcel;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.ExcelWriter;
import com.alibaba.excel.write.metadata.WriteSheet;
import org.junit.jupiter.api.Test;
import top.damoncai.easyexcel.entity.FillData;

import java.util.ArrayList;
import java.util.List;


/**
 * @author zhishun.cai
 * @date 2021/8/23 18:46
 */

public class TestC {

    @Test
    public void test() {
        // 模板注意 用{} 来表示你要用的变量 如果本来就有"{","}" 特殊字符 用"\{","\}"代替
        String path = Test.class.getResource("/").toString();
        System.out.println(path +"123");
        String templateFileName =
               "D:\\exceltemplate\\" + "生产报告模板.xlsx";

        // 方案1 根据对象填充
        String fileName = "D:\\exceltemplate\\" + System.currentTimeMillis() + ".xlsx";
        // 这里 会填充到第一个sheet， 然后文件流会自动关闭
        FillData fillData = new FillData();
        fillData.setName("张三");
        fillData.setNumber(5.2);
        EasyExcel.write(fileName).withTemplate(templateFileName).sheet().doFill(fillData);
//
//        // 方案2 根据Map填充
//        fileName = TestFileUtil.getPath() + "simpleFill" + System.currentTimeMillis() + ".xlsx";
//        // 这里 会填充到第一个sheet， 然后文件流会自动关闭
//        Map<String, Object> map = new HashMap<String, Object>();
//        map.put("name", "张三");
//        map.put("number", 5.2);
//        EasyExcel.write(fileName).withTemplate(templateFileName).sheet().doFill(map);
    }

    /**
     * 填充列表
     *
     * @since 2.1.1
     */
    @Test
    public void listFill() {
        // 模板注意 用{} 来表示你要用的变量 如果本来就有"{","}" 特殊字符 用"\{","\}"代替
        // 填充list 的时候还要注意 模板中{.} 多了个点 表示list
        String templateFileName =
                "D:\\exceltemplate\\" + "生产报告模板.xlsx";

        // 方案1 根据对象填充
        String fileName = "D:\\exceltemplate\\" + System.currentTimeMillis() + ".xlsx";

        // 方案2 分多次 填充 会使用文件缓存（省内存）
        ExcelWriter excelWriter = EasyExcel.write(fileName).withTemplate(templateFileName).build();
        WriteSheet writeSheet = EasyExcel.writerSheet().build();
        excelWriter.fill(data(), writeSheet);
        excelWriter.fill(data(), writeSheet);
        // 千万别忘记关闭流
        excelWriter.finish();
    }

    private List<FillData> data() {
        List<FillData> list = new ArrayList<FillData>();
        for (int i = 0; i < 10; i++) {
            FillData fillData = new FillData();
            list.add(fillData);
            fillData.setName("张三");
            fillData.setNumber(5.2);
        }
        return list;
    }
}
