package t.stracts;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import m.M;
import t.Yy;
import t.Err.NoWordError;

public class Node {
    
     //认知
     private String rz;

     //时间戳
     private Long sj;

    //自我
    private String zw;

    //可读时间
    private String kdsj;

    //是否是新产生的认知
    private boolean newCreate = false;

    private int level = 0;

    //数据
    private SelfObj zwData;

       //子内容
    List<Node> nodes = new ArrayList<>();

    //是否是话题开始
    private Boolean start = false;

    //是否是话题结束
    private Boolean end = false;

    private Node parent;

    public static void memera(String s,String hd){
        //进行对象格式写入
        Node node = new Node();
        node.setRz(s);
        node.setSj(System.currentTimeMillis());
        node.setZw(hd);
        //设置可读时间
        node.setKdsj(new Date().toString());

    }
      
    //获取认知list，方便进行数叶子计算
    public void nodeRzList(Node node,List<String> includRz){

        includRz.add(node.getRz());

        for (Node one : node.getNodes()) {
            nodeRzList(one,includRz);
        }

    }

    //获取树的最深层级
    public void levelDeepVal(Node node,Integer val){

        if(node.getLevel() >= val){
            val  = node.getLevel();
        }

        for (Node one : node.getNodes()) {

           levelDeepVal(one,val);

        }

    }
 
    public boolean gz(Node rz){
        return gz(this,rz,1);
     }
 
     //！！！递归方法功能开发时，需要注意不要把子元素属性和当前对象属性搞混，方法内
     //！！！应当都是子元素属性，而不是使用当前对象属性
     public boolean gz(Node node,Node rz,int djc){//应该是费力查询，也就是先递归完成，在子元素
        
         //都没有找到，在回到上层查找插入
         
          //否则循环递归所有节点进行插入构造
          //如果包含true，说明子元素插入完成，直接停止查找插入返回即可
         
          for (Node one : node.getNodes()) {
            boolean temp = gz(one, rz,djc+1);
            if(temp == true){
                return true;
            }
         }


         //子元素没有找到插入位置，旧查找自身，找到插入就返回true，否则返回false
         //如果子元素和自身相同则跳过，认知树不存在重复认知
         //TODO 场景记忆认知存在重复节点，可能需要处理去重，认知树构造不应该包含重复节点认知
         if(node.getRz().contains(rz.getRz()) && !node.getRz().equals(rz.getRz())){
             Node child = rz;
             child.setLevel(djc);
             //子元素持有父元素引用，方便后续查找能力
             child.setParent(node);
             node.getNodes().add(child);
             return true;
         }else{
             return false;
         }
        
     }

    //逻辑扩展-和组合扩展同理，不过针对的内容和扩展方式方法不同
    public void ljcx(Node node) {
         //从底层向上层构造
        //循环遍历所有子元素
        for (Node one : node.getNodes()) {
            ljcx(one);
        }
        List<Node> newNodes = new ArrayList<>();
        //进行对子元素逻辑扩展
        if(node!= null && node.getNodes().size() > 0){
           for (int i = 0; i < node.getNodes().size(); i++) {
            
                   Node one = node.getNodes().get(i);
                   List<List<String>> nRzs = Yy.lj(one);
                    //对返回的新旧认知部分进行认知节点构建完善
                   List<String> oldList = nRzs.get(0);
                   List<String> newList = nRzs.get(1);
    
                   for (int j = 0; j < oldList.size(); j++) {
                        String  nrz =  one.getRz().replace(oldList.get(j), newList.get(j));
                        Node nNode = new Node();
                        nNode.setRz(nrz);
                        //标记为新认知，以便后续存储
                        nNode.setNewCreate(true);
                        newNodes.add(nNode);
                   }
    
                   node.getNodes().addAll(newNodes);
                   
               
            }
        }
        
    }

    //认知组合能力:可能会组合出多个新认知,添加到原有树中，完成新树的完善升级
    public void zh(Node node) {

        //从底层向上层构造
        //循环遍历所有子元素
        for (Node one : node.getNodes()) {
            zh(one);
        }

        //进行对子元素组合
        for (Node one : node.getNodes()) {
            try {
                Yy.zh(one);
            } catch (NoWordError e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        
    }


    public Node(Map map){
        setRz(map.get("rz").toString());
        setSj(Long.valueOf( map.get("sj").toString()));
        setZw(map.get("zw").toString());
        setKdsj(String.valueOf(map.get("kdsj")));
    }
     
    public Node(String str){
        setRz(str);
    }
     

    public Node() {
    }

    public Map docMap() {

        Map map = new HashMap<>();

        map.put("rz", this.rz);
        map.put("sj", this.sj);
        map.put("zw", this.zw);
        map.put("kdsj", this.kdsj);

        return map;
    }

    //
    public static List<Node> docToNode(List<Map<String,Object>> docs) {

        List<Node> res = new ArrayList<>();
        
        for (Map doc : docs) {
            res.add(new Node(doc));
        }

        return res;
    }

    public static List<Node> strToNode(String[] docs) {

        List<Node> res = new ArrayList<>();
        
        for (String doc : docs) {
            res.add(new Node(doc));
        }

        return res;
    }


    public List<Node> getNodes() {
        return nodes;
    }



    public void setNodes(List<Node> nodes) {
        this.nodes = nodes;
    }



    public String getRz() {
        return rz;
    }

    public void setRz(String rz) {
        this.rz = rz;
    }

    public Long getSj() {
        return sj;
    }

    public void setSj(Long sj) {
        this.sj = sj;
    }

    public String getZw() {
        return zw;
    }

    public void setZw(String zw) {
        this.zw = zw;
    }

    public String getKdsj() {
        return kdsj;
    }

    public void setKdsj(String kdsj) {
        this.kdsj = kdsj;
    }


    public Node getParent() {
        return parent;
    }

    public void setParent(Node parent) {
        this.parent = parent;
    }

    //认知查找能力：查找指定元素并返回，没有则返回null
    public void find(Node node,String s,Node res) {

        //递归逐个元素进行查找
        if(node.getRz().equals(s)){
            res = node;
        }

        for (Node one : node.getNodes()) {
            find(one, s,res);
        }

    }

    public Node findParent(Node node,String s) {

        //递归逐个元素进行查找:如果问题就是顶级节点，那么就说明没有匹配答案，直接跳过即可
        //但是还是需要获得问题树
        if(node.getRz().equals(s)){
             return node;
        }

        for (Node one : node.getNodes()) {
           Node have =  findParent(one, s);
           if(have != null){
            //返回父元素
              return node;
           }
        }

        return null;
    }

    public boolean isNewCreate() {
        return newCreate;
    }

    public void setNewCreate(boolean newCreate) {
        this.newCreate = newCreate;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    //根据子元素匹配度来寻找最相关答案-返回答案节点和相关度
    public void findMatch(Node node,Node quesNode,Map<String,Object> sj) {

        List<Node> childs = node.getNodes();
        //通过做差集来获得相关度==因为子组成认知是公用部分
        //两个list求共同部分数量即相关度--通过长度-移除长度=共有长度
        Integer qSize = childs.size();
        childs.remove(quesNode.getNodes());
        Integer hSize = childs.size();

        Integer xgd = hSize - qSize;
        //最终找到最大相关度的节点

        if(xgd >= Integer.valueOf(sj.get("how").toString())){//TODO 此处可能存在问题，即拥有相同相关度的答案可能存在很多，如何选取
            // anwer = node;
            // how = xgd;
            sj.put("how", xgd);
            sj.put("anwer", node);
        }

        for (Node one : node.getNodes()) {
            findMatch(one, quesNode,sj);
        }

    }

    public void memeraNewRz(Node node) {

        if(node.isNewCreate()){//如果是新产生的认知，则需进行存储
            M.memera(node.getRz(),"思考",null);
        }

        for (Node one : node.getNodes()) {
           memeraNewRz(one);
        }
    }

    public Node findZwRz(Node node, List<String> zxEmo) {

        //当认知内容不为空时才可以进行判断，
        if(!"".equals(node.getRz()) && null != node.getRz()){
            //递归逐个元素进行查找
            //for循环情绪列表
            for (String emo : zxEmo) {
                if(node.getRz().contains(emo)){
                    return node;
                }
            }
        }
        
        for (Node one : node.getNodes()) {
            Node have = findZwRz(one, zxEmo);
            if(have != null){
                return have;
             }
        }

        return null;

    }

    public SelfObj getZwData() {
        return zwData;
    }

    public void setZwData(SelfObj zwData) {
        this.zwData = zwData;
    }

    public Boolean getStart() {
        return start;
    }

    public void setStart(Boolean start) {
        this.start = start;
    }

    public Boolean getEnd() {
        return end;
    }

    public void setEnd(Boolean end) {
        this.end = end;
    }

    

}
