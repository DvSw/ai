package code.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;

public class JsonCovert{

    public static void main(String[] args) {
        
    }
    private static ObjectMapper mapper = new ObjectMapper();
    static {
        //mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));
        //mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY,true);
        //mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES); //禁止未知属性打断反序列化
        //mapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        //mapper.configure(org.apache.htrace.fasterxml.jackson.core.JsonParser.Feature.ALLOW_UNQUOTED_CONTROL_CHARS, true);
        //mapper.configure(JsonParser.Feature.ALLOW_UNQUOTED_FIELD_NAMES, false);//不要引号
        //mapper.configure(JsonGenerator.Feature.QUOTE_FIELD_NAMES, false);//生成不要引号
    }

    public  static <T> T jsonToObj(Class<T> tClass, String feeJSON) {

        try {
            return   mapper.readValue(feeJSON,tClass);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> List<T> jsonToList(Class<T> tClass, String feeJSON) {
        List<T> t;
        try {
            JavaType javaType = mapper.getTypeFactory().constructCollectionType(List.class, tClass);
             //t = mapper.readValue(feeJSON,new TypeReference<List<T>>(){});
            t = mapper.readValue(feeJSON,javaType);
            return t;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static <T> T jsonToObj(String feeJSON) {
        if( feeJSON ==null || "".equals(feeJSON)){
            return null;
        }
        try {
            return mapper.readValue(feeJSON,new TypeReference<T>(){});
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public  static <T> String objToJson(T tClass) {
        String t="";
        try {
               t = mapper.writeValueAsString(tClass);
            return t;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // public  static <T> String objToJsonWithFilter(T t, Class<T> tClass, Set ignoreFilds) {
    //     String a="";
    //     try {
    //         JsonFilterUtil.addFilterForMapper(mapper,tClass,ignoreFilds);
    //         a = mapper.writeValueAsString(t);
    //         return a;
    //     } catch (Exception e) {
    //         e.printStackTrace();
    //         return null;
    //     }
    // }
}
