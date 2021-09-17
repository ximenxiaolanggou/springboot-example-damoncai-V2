package top.damoncai.parsesql;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zhishun.cai
 * @date 2021/4/12 15:45
 */

public class ParseMySQL {
    public static void main(String[] args) {
        try {
            FileReader reader = new FileReader("d:/edge_server.sql");
            BufferedReader br = new BufferedReader(reader);
            String line;
            StringBuilder sb = new StringBuilder("");
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }
            String content = sb.toString();


            // 对于多个空格替换成一个空格
            Pattern p = Pattern.compile("\\s+");
            Matcher m = p.matcher(content);
            content = m.replaceAll(" ");

            // 以分号分割字符串
            String[] contents = content.split(";");

            // 获取所有建表语句
            List<String> tables = new ArrayList<>();
            for (String s : contents) {
                if (s.contains("CREATE TABLE")) {
                    tables.add(s);
                }
            }

            List<String> result = new ArrayList<>();
            // 用逗号分割表语句，第一句存在表名，最后一个无用
            for (String s : tables) {
                String[] s1 = s.split(", ");
                String[] s2 = s1[0].split(" \\( ");
                String tableName = s2[0].split(" ")[2].replace("`","");

                result.add("table" + tableName);

                // 创建一个集合存储所有列
                List<String> columns = new ArrayList<>();
                columns.add(s2[1].split(" ")[0].replace("`",""));

                for (int i = 1; i < s1.length - 1; i++) {
                    columns.add(s1[i].split(" ")[0].replace("`",""));
                }

                String column = "";
                for (int i = 0; i < columns.size(); i++) {
                    column += columns.get(i);
                    if (i + 1 < columns.size()) {
                        column += ",";
                    }
                }
                result.add("columns" + column);
                result.add("-------------------------------------");
            }

            // 写入到文件中
            File write = new File("d:/device_iot2.sql");
            write.createNewFile();
            BufferedWriter out = new BufferedWriter(new FileWriter(write));
            for (String s : result) {
                // \r\n即为换行
                out.write(s + "\r\n");
            }

            // 把缓存区内容写入文件
            out.flush();
            // 关闭文件
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
