package t.stracts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//语言单词对象
public class Word {

    //文本内容
    private String text;

    //词性 动词，名词 ==语句无此属性
    private String cx;

    private String xxcx;

    private List<String> cxs;
    

    public String getXxcx() {
        return xxcx;
    }

    public void setXxcx(String xxcx) {
        this.xxcx = xxcx;
    }

    public List<String> getCxs() {
        return cxs;
    }

    public void setCxs(List<String> cxs) {
        this.cxs = cxs;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getCx() {
        return cx;
    }

    public void setCx(String cx) {
        this.cx = cx;
    }
  
    //字典list生成快速定位map
    public static Map<String, Word> quikeMap( List<Word> words) {

        Map<String, Word> res = new HashMap<>();

        for (Word word : words) {
            res.put(word.getText(), word);
        }

       
        return res;

    }

    //字典map转字典对象集合
    public static List<Word> mapToWord(List<Map<String, Object>>  zdMap) {

        List<Word> res = new ArrayList<>();

        for (Map<String, Object> map : zdMap) {
            res.add(mapToWord(map));
        }

        return res;

    }

    public static Word mapToWord(Map<String, Object> map) {

        Word word = new Word();

        word.setCxs( (List<String>) map.get("cxs"));
        word.setText(map.get("text").toString());

        return word;
    }
    
}
