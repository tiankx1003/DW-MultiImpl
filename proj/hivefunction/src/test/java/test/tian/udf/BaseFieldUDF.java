package test.tian.udf;

import jodd.util.StringUtil;
import org.apache.hadoop.hive.ql.exec.UDF;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * @author tiankx
 * @version 1.0.0
 * @date 2020/9/4 16:49
 *
 * UDF编写步骤
 * 继承UDF类
 * 重写evaluate方法
 *
 */
public class BaseFieldUDF extends UDF {
    public static void main(String[] args) throws JSONException {
        String line = "aaa";
        String x = new BaseFieldUDF().evaluate(line, "mid");
        System.out.println(x);
    }

    public String evaluate(String line, String key) throws JSONException {
        // 1. 处理line 服务器时间 | json
        String[] log = line.split("\\|");
        // 2. 合法性校验，切分出的字符串长度为2，且json字符串不为空
        if (log.length != 2 || StringUtil.isBlank(log[1]))
            return "";
        // 3. 处理json，json字符串转为Json对象
        JSONObject baseJson = new JSONObject(log[1].trim());

        String result = "";
        // 4. 根据传进来的key查找相应的value
        if ("et".equals(key)) {
            if (baseJson.has("et")) result = baseJson.getString("et");
        } else if ("st".equals(key))
            result = log[0].trim();
        else {
            JSONObject cm = baseJson.getJSONObject("cm");
            if (cm.has(key)) result = cm.getString(key);
        }

        return result;

    }
}
