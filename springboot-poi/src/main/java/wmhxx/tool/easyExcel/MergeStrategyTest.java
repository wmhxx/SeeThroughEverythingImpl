package wmhxx.tool.easyExcel;

import com.alibaba.excel.EasyExcel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.junit.jupiter.api.Test;

import java.util.LinkedList;

/**
 * @author :WangMengHe
 * @since :2021-10-28 11:25
 **/
public class MergeStrategyTest {


    @Test
    public void test(){

        String fileName = "/Users/wangmh/Desktop/"+System.currentTimeMillis()+".xlsx";
        LinkedList<Model> linkedList = new LinkedList<>();
        linkedList.add(new Model("张三", 18, "北京"));
        linkedList.add(new Model("张三", 19, "上海"));
        linkedList.add(new Model("王五", 20, "青岛"));
        linkedList.add(new Model("赵六", 27, "广州"));

        // 0,1 表示 对1,2列启用合并策略
        EasyExcel.write(fileName, Model.class)
                .registerWriteHandler(new MergeStrategy(linkedList.size(),0))
                .sheet("通用合并行策略")
                .doWrite(linkedList);

    }

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    class Model{
        private String name;

        private Integer age;

        private String address;
    }
}
