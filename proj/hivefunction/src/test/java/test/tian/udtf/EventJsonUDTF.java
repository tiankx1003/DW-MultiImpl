package test.tian.udtf;

import jodd.util.StringUtil;
import org.apache.hadoop.hive.ql.exec.UDFArgumentException;
import org.apache.hadoop.hive.ql.metadata.HiveException;
import org.apache.hadoop.hive.ql.udf.generic.GenericUDTF;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.ObjectInspectorFactory;
import org.apache.hadoop.hive.serde2.objectinspector.StructObjectInspector;
import org.apache.hadoop.hive.serde2.objectinspector.primitive.PrimitiveObjectInspectorFactory;
import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;

/**
 * @author tiankx
 * @version 1.0.0
 * @date 2020/9/5 16:39
 * <p>
 * UDTF函数编写
 */
public class EventJsonUDTF extends GenericUDTF {

    /**
     * 该方法种，我们将指定输出参数的名称和参数类型
     *
     * @param argOIs
     * @return
     * @throws UDFArgumentException
     */
    @Override
    public StructObjectInspector initialize(ObjectInspector[] argOIs) throws UDFArgumentException {
        ArrayList<String> fieldnames = new ArrayList<>();
        ArrayList<ObjectInspector> fieldOIs = new ArrayList<>();

        fieldnames.add("event_name");
        fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);
        fieldnames.add("event_json");
        fieldOIs.add(PrimitiveObjectInspectorFactory.javaStringObjectInspector);

        return ObjectInspectorFactory.getStandardStructObjectInspector(fieldnames, fieldOIs);
    }

    /**
     * 输入一条记录，输出若干条记录
     *
     * @param objects
     * @throws HiveException
     */
    @Override
    public void process(Object[] objects) throws HiveException {
        // 获取传入的et
        String input = objects[0].toString();

        // isBlank和isEmpty的区别
        // 如果传进来的数据为空，直接返回过滤掉该数据
        if (StringUtil.isBlank(input)) return;
        else {
            try {
                JSONArray ja = new JSONArray(input);
                if (ja == null) return; // ????
                // 循环遍历每个事件
                for (int i = 0; i < ja.length(); i++) {
                    String[] result = new String[2];

                    // 取出每个事件的名称
                    result[0] = ja.getJSONObject(i).getString("en");
                    // 取出每个事件的整体
                    result[1] = ja.getString(i);
                    forward(result);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * 当没有记录处理的时候该方法会被调用，用来清理代码或者产生额外的输出
     *
     * @throws HiveException
     */
    @Override
    public void close() throws HiveException {

    }
}
