
import org.junit.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class CSVTest {
    @Test
    public void exportCsv() {
        List<String> dataList=new ArrayList<String>();
        dataList.add("3,张三,男");
        dataList.add("2,李四,男");
        dataList.add("1,小红,女");
        boolean isSuccess=CSVUtils.exportCsv(new File("F:/Document/Desktop/test.csv"), dataList);
        System.out.println(isSuccess);
    }

    /**
     * CSV导出
     *
     * @throws Exception
     */
    @Test
    public void importCsv()  {
        List<String> dataList=CSVUtils.importCsv(new File("F:/Document/Desktop/test.csv"));
        if(dataList!=null && !dataList.isEmpty()){
            for(String data : dataList){
                System.out.println(data);
            }
        }
    }

    @Test
    public void test(){
        try {
            BufferedReader reader = new BufferedReader(new FileReader("F:/Document/Desktop/test.csv"));//换成你的文件名
            reader.readLine();//第一行信息，为标题信息，不用,如果需要，注释掉
            String line = null;
            while((line=reader.readLine())!=null){
                String item[] = line.split(",");//CSV格式文件为逗号分隔符文件，这里根据逗号切分

                for (String i:item) {
                    if (i.equals(item[item.length-1])){
                        System.out.print(i);
                        break;
                    }
                    System.out.print(i+",");
                }
//                String last = item[item.length-1];//这就是你要的数据了
//                //int value = Integer.parseInt(last);//如果是数值，可以转化为数值
                System.out.println();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


}
