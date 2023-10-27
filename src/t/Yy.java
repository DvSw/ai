package t;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import code.util.MongoUtil;
import t.Err.NoWordError;
import t.stracts.Node;
import t.stracts.Word;

//语言-文本功能
public class Yy{
    
    //词典
    static List<Word> words = new ArrayList<>();
    //快速定位容器
    static Map<String,Word> wordsMap = new HashMap();

    //对词典进行初始化构造
    static{
        init();
    }

    public static void init(){
        MongoUtil mongoUtil = new MongoUtil();
        List<Map<String, Object>>  zdMap = mongoUtil.queryAll("yy");
        words = Word.mapToWord(zdMap);
        wordsMap = Word.quikeMap(words);
    }

   
    public static int judeType(String s){

        //TODO 语法结构 ,行动语句是 主体+ 动词 或者主体+助动词+动词 结构，所以不能依赖
        //助动词判断，只要符合行动语句规则就是行为语句，
        List<String> act  = Arrays.asList("要","应该","该","需","必须","需要");
        List<String> emo = Arrays.asList(
            "高兴","开心","愉快","愉悦","满足","放松","安全","悲伤","恐惧","贪婪",
            "压力","痛苦","疑惑","厌恶");
        for (String string : act) {

            if(s.contains(string)){
                return 2;
            }
    
        }

        for (String em : emo) {

            if(s.contains(em)){
                return 1;
            }
    
        }
       
        return 0;
    }

   //利用语法规则进行组合新语句
    public static void zhcx(Node node) {
        //三个list代表主谓宾，循环组合，即可得到新认知，然后插入到树中

        node.zh(node);

    }

    //反义词汇创造，任何事物可能具有反义,设计语言学
    public static void ljcx(Node node){
        //逻辑扩展，

        node.ljcx(node);
    }

    //语言扩展能力：
    public static void cx(Node node) {

        //需要提供面板进行词典刷新能力
        //先组合扩展
        zhcx(node);
        //然后逻辑扩展
        ljcx(node);

    }
    //对子元素进行语言规则组合，然后直接完善升级认知树
    public static List<List<String>> lj(Node node) {


        List<List<String>> res = new ArrayList<>();

        List<String> wyList = new ArrayList<>();
        List<String> fwyList = new ArrayList<>();

        List<Node> childs = node.getNodes();
        Node preCh = new Node();
        for (Node child : childs) {

            Word word = wordsMap.get(child.getRz());
            if(null != word){
                //throw new NoWordError(child.getRz()+"不存在，完善");
                if(wordsMap.get(child.getRz()).equals("动词")){
               
                    //进行对谓语加反义：存在问题，有些词其前面本身就是反义词
                    //所以要找到前词进行判断，如果是非义，直接去掉即可
                    //反义判断 ：是否包含非义词-
                    if(preCh !=null && preCh.getRz().contains("不") ){//包含
                        wyList.add(preCh.getRz()+child.getRz());
                        fwyList.add(child.getRz());
                    }else{//不包含
                        wyList.add(child.getRz());
                        fwyList.add("不"+child.getRz());
                    }
                }
            }


            
        }
        res.add(wyList);
        res.add(fwyList);
        return res;
    }

    //对子元素进行语言规则组合，然后直接完善升级认知树
    public static void zh(Node node) throws NoWordError {

        List<Node> res = new ArrayList<>();

        List<Node> zyList = new ArrayList<>();
        List<Node> wyList = new ArrayList<>();
        List<Node> byList = new ArrayList<>();

        List<Node> childs = node.getNodes();

        for (Node child : childs) {
            if(child.getRz().length() > 1){//简单语句判断
                return;
            }
            //将子元素按照词性进行分组，如果是语句，先跳过处理
            //字典功能扩展，不光存储单字，还可以存储一些词性短语
            //对child进行单词查询，如果没有查到则提示或者报错，对词汇表进行完善升级
            Word word = wordsMap.get(child.getRz());
            if(null == word){
                throw new NoWordError(child.getRz()+"不存在，完善");
            }

            if(wordsMap.get(child.getRz()).equals("名词")){
                zyList.add(child);
                byList.add(child);
            }
            if(wordsMap.get(child.getRz()).equals("动词")){
                wyList.add(child);
            }
            if(wordsMap.get(child.getRz()).equals("动名词")){
                zyList.add(child);
                wyList.add(child);
                byList.add(child);
            }

            //通过循环产出所有认知组合,剔除自身后,添加到父元素
            for (Node zy : zyList) {
                for (Node wy : wyList) {
                    for (Node by : byList) {
                        String nRz = zy.getRz()+wy.getRz()+by.getRz();
                        Node one = new Node();
                        one.setRz(nRz);
                        //标记为新认知，以便后续存储
                        one.setNewCreate(true);
                        if(!node.getRz().equals(nRz)){

                            res.add(one);
                        }
                    }
                }
            }
            node.getNodes().addAll(res);
            //

        }

        
        return;
    }

    //TODO 英文语法规则实现较为简单，但是本质都是语法规则，后期都需要添加的能力

    public static void main(String[] args) {
       
        //hello means greetings , greetings mean like you ,people like you,you should feel happy.
        // 
        List<String>  res = bzfg("you should feel happy");
       
        System.out.println(res);
    }

    public static String replaceBdToBlank(String s) {

        String res = s.toString();

        List<String> bdfh = Arrays.asList(",",".");
        //大部分情况下，不一定是标准空格，后续需要对此进行完善升级

        for (int i = 0; i < bdfh.size(); i++) {
             res =  res.replace(bdfh.get(i), " ");
        }

        return res;

    }


    //英文16中词性：n名词，u不可数名词，c可数名词，v动词，vi不及物动词，vt及物动词
    //av助动词 mv情态动词 conj连接词 adj形容词 adv副词 art冠词 prep介词 pron代名词
    //num数词 int感叹词
    public static List<String>  bzfg(String s)  {

        List<String> xgdy = new ArrayList<>();

        List<String> bdfh = Arrays.asList(",",".");
 
        List<String> zy = Arrays.asList("me","you");

        String[] allWords = s.split(" ");//英文基于空格进行分割
 
        for (int i = 0; i < allWords.length ;i++) {

             //每组合成一个短语就抛出-利用顺序结构
             Word one = wordsMap.get(allWords[i]);
             
             //TODO 跳过标点符号处理,后续处理复杂语句，可能仍旧需要标点符合语法能力
             if(bdfh.contains(allWords[i])){
                
                continue;
             }

             if(one == null){
                System.out.println(allWords[i]+" 不存在");
                return xgdy;
            }

            if(one.getCxs().contains("n")){//具有名词属性的单词，直接进行查询即可
                xgdy.add(one.getText());
            }
            
        }

        return xgdy;
    }

}
