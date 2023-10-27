package m;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.IllegalFormatPrecisionException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;
import java.util.TreeSet;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.bson.BsonDocument;
import org.bson.Document;
import org.bson.codecs.configuration.CodecRegistry;
import org.bson.conversions.Bson;

import code.util.JsonCovert;
import code.util.MongoUtil;
import t.Yy;
import t.Err.NoWordError;
import t.stracts.ActObj;
import t.stracts.CjObj;
import t.stracts.HdObj;
import t.stracts.HighLevelRz;
import t.stracts.Node;
import t.stracts.RzObj;
import t.stracts.RzUitl;
import t.stracts.SelfObj;
import t.stracts.ZwRzUtil;
public class M {
  

    //write
    private static FileWriter f;
    private static PrintWriter printWriter;

    //read bufferedReader流是持续读，lines只会继续读取，要重新读取所有文件，需要
    //重新构造流或者关闭，从新打开流
    private static FileReader fileReader;
    private static BufferedReader bufferedReader;

    private static File file = new File("m.txt");
    private static String lineSeparator = System.getProperty("line.separator");

    static{
    
    }

    private static MongoUtil mongoUtil = new MongoUtil();
    
    public static void main(String[] args) throws Exception{

      List<Node> nodess = new ArrayList<>();
      
      allXgRzQuery("你好",nodess);

      System.out.println(nodess);
     
    }

    public static void write(String s){
      printWriter.append(s + lineSeparator);
      try{
        f.flush();
      }catch(Exception e){
        e.printStackTrace();
      }
     
   }

    //nosql存储
    public static void write(Node node){

      Document doc = new Document(node.docMap());

      try {
        mongoUtil.insertDb(doc);
      } catch (Exception e) {
        //TODO: handle exception
        e.printStackTrace();
      }

    }


    //记忆功能--存储信息:单纯存储功能
    public static void memera(String s){
      //进行对象格式写入
      Node node = new Node();
      node.setRz(s);
      node.setSj(System.currentTimeMillis());
      node.setZw("沟通-听到");
      write(node);
    }

    public static Node memera(String s,String hd,Boolean start){
      //进行对象格式写入
      Node node = new Node();
      node.setRz(s);
      node.setSj(System.currentTimeMillis());
      node.setZw(hd);
      //设置可读时间
      node.setKdsj(new Date().toLocaleString());
      node.setStart(start);
      write(node);

      return node;
    }

    //TODO 冗余辅助数据记录,目前是记录自我数据,后续可能记录更多活动数据,先简单实现
    public static Node memera(String s,SelfObj selfObj,String hd){
      //进行对象格式写入
      Node node = new Node();
      node.setRz(s);
      node.setSj(System.currentTimeMillis());
      node.setZw(hd);
      //设置可读时间
      node.setKdsj(new Date().toLocaleString());
      node.setZwData(selfObj);
      write(node);

      return node;
    }

    //集合写入方法重载
    public void memera(Set<String> sets){
      for (String string : sets) {
        memera(string);
      }
    }


    public void findCj( List<List<String>> cjList, List<String> cjrz,String nearRz){
      //对场景进行匹配函数
      for (int i = 0; i < cjrz.size(); i++) { //遍历场景日志：利用fori获取匹配到认知的位置，以便确定场景范围
                    
        //问题===查找近似认知==确定近似场景查找==明确场景==明确的场景才是确定具体记忆
        String jsrz = Rizhi.valueGet(cjrz.get(i));
        if(cjrz.get(i).contains("沟通-收到") && jsrz.contains(nearRz)){//如果场景中也找到相关认知才能确定认知
          //正反向包含匹配不对，认知树必然是场景中的明确子项
          List<String> cj = new ArrayList<>();
          //向上寻找之前感受状态并记录
              //
              for (int j = i; j >= 0; j--) {
              
                if(cjrz.get(j).contains("自我")){
                  cj.add(cjrz.get(j));
                  //直到找到之前感受为止
                  break;
                }
            }
            //记录问题
            cj.add(cjrz.get(i));
            for (int j = i+1; j < cjrz.size(); j++) {//向下寻找答案回复，以及感受状态，如果
              //下一项不是感受变化，说明回答完全合适，直接停止即可
                cj.add(cjrz.get(j));
                if(cjrz.get(j).contains("自我")){
                  //判断回复--找到答案,中止查询并存储，继续寻找其他场景
                  break;
                }
            }
            //抽象出一个场景进入list数据
            //多个近似认知匹配到了同一个场景，需要对场景进行去重处理
            if(!cjList.contains(cj)){
              cjList.add(cj);
            }
        }
        
      
      }
    }

    public static List<ActObj> cjNodesToCjObjs(List<Node>  cjList){

      List<Node> actList = new ArrayList<>();

      List<ActObj> actObjs = new ArrayList<>();
      
      for (int i = 0; i < cjList.size();i++) {
        //顺序规则：听到-自我-回复，听到-回复，不符合规则就跳过

        if(!"思考".equals(cjList.get(i).getZw())){
            actList.add(cjList.get(i));
        }
        
        if(actList.size() > 1){//组成一个对话单元，进行判断

           //需要进行顺序判断，要能够组成对话单元,应该保持有序且符合顺序规则
           if(!"沟通-听到".equals(actList.get(0).getZw())){
              actList.remove(0);
              continue;
           }

           //如果2号位不是规则，在前面进行过滤，那么只可能是听到：移除一号重新计数
           if("沟通-听到".equals(actList.get(1).getZw())){
              actList.remove(0);
              continue;
          }

           if("沟通-回复".equals(actList.get(1).getZw())){//说明构成对话单元，组合并清空list
              
              ActObj actObj = new ActObj();
              actObj.setQhd(actList.get(0));
              actObj.setHhd(actList.get(1));
              actObjs.add(actObj);
              actList.clear();
              continue;
           }else if(actList.size() > 2){

              //三个的情况，1，2号位置锁定，只需要判断3是不是听到，如果是移除前2号，重新计算
              if("沟通-听到".equals(actList.get(1).getZw())){
                actList.remove(0);
                actList.remove(1);
                continue;
              }else{//说明构成对话单元，组合并清空list
                ActObj actObj = new ActObj();
                actObj.setQhd(actList.get(0));
                actObj.setSelf( actList.get(1).getZwData());
                actObj.setHhd(actList.get(2));
                actObjs.add(actObj);
                actList.clear();
                continue;
              }
          }
        }
      }
      return actObjs;
    }

  
    public static List<Node> queryAllCj( long time){
      
     
      Date one = new Date();
      //long qyt = one.getTime() - 24*60*60*1000;
      //long hyt = one.getTime() + 24*60*60*1000;
      long hyxs = one.getTime() + 60*60*1000;
      //得到场景认知列表
      List<Map<String, Object>> res = mongoUtil.queryByTime(time,hyxs);
      
      List<Node> cjList = Node.docToNode(res); 

      return cjList;
    }
   

    public static void matchCj(){
      
      for (int j = 0; j < allCj.size(); j++) {
        
          List<ActObj> list = allCj.get(j);
          boolean isPP = true;

          //遍历chat进行匹配,场景是后一小时,时间戳不可能重复,所以直接匹配即可
          //场景长度大于当下活动,所以是匹配到chat的最后位置,中间不出现不同就是匹配场景记忆
          for (int i = 0; i < chatList.size(); i++) {
              //存在不同了,中止当前循环
              if(!chatList.get(i).getQhd().getRz().equals(list.get(i).getQhd().getRz())
               || !chatList.get(i).getHhd().getRz().equals(list.get(i).getHhd().getRz())){
                isPP = false;
                break;
              }
          }
          //循环结束后,仍旧是true,则说明是匹配场景,计算出综合感受并记录即可
          if(isPP){

              CjObj cjObj = new CjObj();
              cjObj.setCjList(list);
              //计算综合感受
              Integer zhgs = calZhgs(list);
              cjObj.setZhgs(zhgs);
              matchCj.add(cjObj);

          }
      }

    }
    //查找印象深刻感受
    public static Integer skyx(List<ActObj> list){

      Integer res = 0;

      for (int i = 0; i < list.size(); i++) {
          int temp = 0;

          if(list.get(i).getSelf() != null ){
            temp += list.get(i).getSelf().getCd();
          }

          res+=temp;
      }

      return res;

    }
    //直接转换类型后累加即可：计算的是一个场景下变化值的累加结果，即最终变化数值，cd的和
    public static Integer calZhgs(List<ActObj> list){

      Integer res = 0;

      for (int i = 0; i < list.size(); i++) {
          int temp = 0;

          if(list.get(i).getSelf() != null ){
            temp += list.get(i).getSelf().getCd();
          }

          res+=temp;
      }

      return res;
    }

    //直接转换类型后累加即可：计算的是一个场景下变化值的累加结果，即最终变化数值，cd的和
    public static Integer calZhgsSelfObj(List<SelfObj> list){

      Integer res = 0;

      for (int i = 0; i < list.size(); i++) {
          int temp = 0;

        
          temp += list.get(i).getCd();
         

          res+=temp;
      }

      return res;
    }

    public static CjObj matchOne(Node node,SelfObj feel) {

      CjObj res = new CjObj();

      List<ActObj> copy = new ArrayList<>(); 
      //根据匹配场景记忆进行新尝试认知匹配,遍历匹配场景,匹配到场景后重新计算综合感受
      for (int i = 0; i < matchCj.size(); i++) {
        List<ActObj> cList = matchCj.get(i).getCjList();
        //复制当下场景数据
        copy.addAll(cList);
        for (int j = 0; j < cList.size(); j++) {
            if(node.getRz().equals(cList.get(j).getQhd().getRz())
            || node.getRz().equals(cList.get(j).getHhd().getRz())){//如果是相同认知,则判断为匹配场景,中止匹配
             
              //根据位置进行数据替换,从而计算新的综合感受
              copy.get(j).setSelf(feel);
              res.setCjList(copy);
              //重新计算综合感受--需要给出新尝试认知感受,需要不影响原有场景数据,
              Integer zhgs = calZhgs(copy);
              res.setZhgs(zhgs);
              //记录当下场景节点,也可以是记录未知数据,根据需求进行扩展
              res.setPpNode(cList.get(j));

              return res;
            }
        }
      }

      return null;
    }

    //根据关键认知,查找局部认知树,也就是最相关认知父节点列表
    public static List<Node> findHighLevelRz(String s,List<Node> trees) {

      List<Node> parentList = new ArrayList<>();
    
      for (Node node : trees) {
        Node temp = node.findParent(node, s);
        if(temp != null  && !s.equals(temp.getRz())){//说明是父元素，记录即可
            parentList.add(temp);
        }
      }

      return parentList;

    }

    public static HighLevelRz finishHighLevelRz(List<Node> parentList) {
      
      HighLevelRz highLevelRz = new HighLevelRz();

      for (Node node : parentList) {
        int te =  Yy.judeType(node.getRz());
        if(te == 2){//判断是行为认知，可以直接回复
          highLevelRz.getActList().add(node);
        }
        if(te == 1){
          //判断是感受节点，进行-自我认知系统-感受认知数据化
          //理论上存在多个感受节点，需要进行综合感受计算
          SelfObj selfFeel = ZwRzUtil.ziwoganshoushuju(node);
          highLevelRz.getFeelList().add(selfFeel);
          //highLevelRz.setFeel(selfFeel);
        }
      } 
      //计算综合感受-
      SelfObj selfObj = new SelfObj();
      Integer val = calZhgsSelfObj(highLevelRz.getFeelList());
      selfObj.setCd(val);

      //存储综合感受
      highLevelRz.setFeel(selfObj);

      return highLevelRz;

    }

    public static HighLevelRz highLevelRz(String rz,List<Node> trees) {
      
      List<Node> xwParentList = findHighLevelRz(rz,trees);
      //并不关心父类行为认知，只关心关键认知感受-和关键认知
      //得到行为认知节点和感受认知节点
      HighLevelRz gshighLevelRz =  finishHighLevelRz(xwParentList);

      return gshighLevelRz;

    }

    //优化为对认知字符串进行查询过否的判断
    public static  void findParentR(String s,List<Node> trees,List<String> all,List<Node> res) {

      //对比all,对不存在的父节点进行添加-存在则中止递归
      if(all.contains(s)){//判断此认知已经查询过,进行停止
        return;
      }

      //否则进行添加-标记
      all.add(s);

       //子找父,目标找到所有父元素-最后统一去重即可,此处可以优化完善
      //递归过程,需要停止标志判断
      List<Node> parentList = findHighLevelRz(s,trees);

      //将新认知的父元素进行列表添加
      res.addAll(parentList);
      //

      List<Node> sameLevelList = new ArrayList<>();
      //父找子
      for (int i = 0; i < parentList.size(); i++) {
      
        sameLevelList.addAll(parentList.get(i).getNodes());
      
      }

      //父元素存在于多颗不同树之间-需要继续递归查找-否则得到的父元素-仅仅只是当前树的父元素
      for (int i = 0; i < sameLevelList.size(); i++) {

        findParentR(sameLevelList.get(i).getRz(),trees,all,res);
       
      }
    }

    //递归查找-相关行为-感受-高级认知
    public static HighLevelRz highLevelRzR1(String s,List<Node> trees) {

      List<String> all = new ArrayList<>();
      List<Node> parentList = new ArrayList<>();
      
      findParentR(s, trees, all, parentList);
      
      //父元素节点存在重复，要进行去重
      parentList = parentList.stream().collect(Collectors.collectingAndThen(
        Collectors.toCollection(()->new TreeSet<>(Comparator.comparing(Node::getRz))), ArrayList::new));

      //得到行为认知节点和感受认知节点
      HighLevelRz highLevelRz =  finishHighLevelRz(parentList);
      
      return highLevelRz;

    }


    //所有场景记忆
    public static List<List<ActObj>> allCj =  new ArrayList<>();
    //当下聊天记忆
    public static List<ActObj> chatList =  new ArrayList<>();

    
    //匹配的场景列表--会随着对话进行变更,而综合感受则是对匹配场景列表的综合计算,所以会随着匹配场景列表变化跟随变化
    //保持一致的列表
    public static  List<CjObj> matchCj =  new ArrayList<>();
    
    //实际场景查询，其实就是查询包含与被包含，向下包含，可以直接字符串切割即可，然后根据语言规则
    //回忆：环境信息接受-应对环境
    public static String remanber4(String s){

      Boolean start = null;//是否是开始场景
      //通过之前有无场景进行判断
      if(chatList.size() < 1){//判断为开始,需要存储开始标记
        start = true;
      }

      //建立听到内容节点,并放入场景列表,不能等到存储时再放入场景列表,会缺乏匹配
      Node lisNode = new Node();
      lisNode.setRz(s);
      ActObj lisActObj = new ActObj();
      lisActObj.setQhd(lisNode);
    
      //String back = "我不懂，没有相关认知";
      String back = "i have no idea";

      //先进行like场景查询，查询出相关内容，以及相关内容向下的场景
      List<Map<String,Object>> qRes = mongoUtil.queryByDoc("test",new Document("rz", s));
     
      if(qRes.size() < 1){//就算没有相关认知，聊天结束前也需要记录听到的认知

        Node qhd = memera(s,"沟通-听到",start);
        lisActObj.setQhd(qhd);

        //存储沟通活动-回复的内容
        Node hhd = memera(back,"沟通-回复",null);
        lisActObj.setHhd(hhd);
        //添加到话题列表
        chatList.add(lisActObj);
        return back;
      }
      
      //doc集合转化node对象集合
      List<Node> rzs = Node.docToNode(qRes);//这些数据都是相关场景发生的首先位置
    
      if(chatList.size() < 1){//不存在,去根据关键认知加载场景列表,不如记录详细内容
        //没有数据,说明是新场景开始,进行回忆查询
        //可以更加方便进行上文环境场景匹配,为了方便匹配,以node列表形式进行匹配,
        //可以方便去除非沟通活动的其他活动
        for (int index = 0; index < rzs.size(); index++) {
          List<Node> cj = queryAllCj(rzs.get(index).getSj());
          List<ActObj> actObjs =  cjNodesToCjObjs(cj);
          allCj.add(actObjs);
        }
      }
      
      //拥有场景记忆,就可以进行正向感受引导-判断
      //如果为空,说明不存在场景记忆,这里的判断是需要的
      if(allCj.size()>1){// 说明具有问题记忆，判断类型即可--需要找到最新认知发生的位置
        //TODO 存在问题,有场景记忆,但是没有匹配场景记忆,该如何,理论上就是只能通过认知成长进行场景了
        //先要匹配最接近场景记忆-然后再进行综合感受比较
        matchCj();
      
        matchCj.sort(new Comparator<CjObj>() {
          @Override
          public int compare(CjObj o1, CjObj o2) {
            // TODO Auto-generated method stub
            return o1.getZhgs() - o2.getZhgs();
          }
        });

        if(matchCj.size() > 0){//判断有无匹配场景:存在匹配场景,才能判断选择
          //获取综合感受排序后最强的场景记忆进行判断活动
          //===找到综合感受最强元素-但是基于场景查找--是活动节点列表，仍旧需要关键匹配认知节点查找
          List<ActObj> actObjs = matchCj.get(0).getCjList();
         
          int maxgs = -3;
          for (int i = 0;i < actObjs.size()-1; i++) {
            if(s.equals(actObjs.get(i).getQhd().getRz()) ){
                SelfObj temp = actObjs.get(i).getSelf();
                int tempgs = temp == null ? 0 : temp.getCd();
                if(tempgs >=  maxgs){
                  maxgs = tempgs;
                  back = actObjs.get(i).getHhd().getRz();
                }
            }
          }
        }
      }

      List<Node> nodes = new ArrayList<>();
      
      //试图构建相关认知树系统，所以是全面相关
      allXgRzQuery(s,nodes);

      nodes = nodes.stream().collect(Collectors.collectingAndThen(
        Collectors.toCollection(()->new TreeSet<>(Comparator.comparing(Node::getRz))), ArrayList::new));
      
      List<Node> words = new ArrayList<>();
      //在构建认知树之前,对nodes进行最小化能力
      for (int i = 0; i < nodes.size(); i++) {
         String[] temp = Yy.replaceBdToBlank(nodes.get(i).getRz()).split(" ");
         words.addAll(Node.strToNode(temp));
      }

      nodes.addAll(words);

      //避免有重复单词-因为元素节点可以重复使用，所以重复节点无意义-需要再次去重
      nodes = nodes.stream().collect(Collectors.collectingAndThen(
        Collectors.toCollection(()->new TreeSet<>(Comparator.comparing(Node::getRz))), ArrayList::new));

      List<Node> trees = RzUitl.wanquanrenzhishu2(nodes);
      
      for (Node tree : trees) {
         Yy.cx(tree);
      }

      //======对问题进行查找行为的能力=============优化递归行为-感受查找能力
     
      HighLevelRz highLevelRz = highLevelRzR1(s, trees);
      //赋予感受节点
      SelfObj selfFeel = highLevelRz.getFeel();

      //赋予--回答自我认知感受节点-感受权重系统
      SelfObj selfFeelBack = new SelfObj();
      //======对问题进行查找行为的能力=============

      //======对后续行为的能力=============
      //新认知列表-也就是答案列表，进行场景匹配，
      List<HighLevelRz> xrzList = new ArrayList<>();

      List<HighLevelRz> wqxrzList = new ArrayList<>();

      //遍历行为节点-查找每个行为节点的感受节点
      for (int i = 0; i < highLevelRz.getActList().size(); i++) {
      
        HighLevelRz gshighLevelRz = highLevelRzR1(highLevelRz.getActList().get(i).getRz(), trees);
      
        //设置关键认知
        gshighLevelRz.setGjrz(highLevelRz.getActList().get(i));

        xrzList.add(gshighLevelRz);

      }
      //======对后续行为的能力=============

      for (int i = 0; i < xrzList.size(); i++) {

        CjObj ppCj = matchOne(xrzList.get(i).getGjrz(),null);
        if( null == ppCj ){//不存在场景记忆，为完全新尝试认知，进行记录
           wqxrzList.add(xrzList.get(i));
           //如果感受与以往不同，也是属于完全新尝试认知
        //存在都为空的情况
      }else if((ppCj.getPpNode().getSelf() == null &&  xrzList.get(i).getGjrz().getZwData() != null ) 
      && (ppCj.getPpNode().getSelf().getCd() != xrzList.get(i).getGjrz().getZwData().getCd())){
          wqxrzList.add(xrzList.get(i));

        }
        //存在一种未知新尝试，即存在场景记忆，但是认知状态和场景记忆感受不同的情况下，
        //此时认知也算是未知新认知，任何事物的变化都将未来导向未知状态
      }

      wqxrzList.sort(new Comparator<HighLevelRz>() {
        @Override
        public int compare(HighLevelRz o1, HighLevelRz o2) {
          // TODO Auto-generated method stub
          return o1.getFeel().getCd() - o2.getFeel().getCd();
        }
      });
      
      if(wqxrzList.size() > 0){//判断有相关行为认知存在

        if(matchCj.size() > 0 && matchCj.get(0).getZhgs() >= 0){//判断有无匹配场景:存在匹配场景
          //存在匹配场景，则对匹配场景的综合感受进行判断，如果为正向，则判断选择
          
          back = matchCj.get(0).getPpNode().getHhd().getRz();
          HighLevelRz xgs = highLevelRz(back,trees);
         
          selfFeelBack = xgs.getFeel();
         
        }else{//无匹配场景，所有新认知都是完全新认知
         
          back = wqxrzList.get(0).getGjrz().getRz();
          //给回复后自我认知感受进行数据填充
          selfFeelBack = wqxrzList.get(0).getFeel();
        }

      }

      //当所有查询都结束，才可以进行听到记录，也就是避免回忆的时候回忆到现在
      //对于深刻印象要建立冗余,便于查询
      memera(s,selfFeel,"沟通-听到");

      //记录自我主观感受--当下沟通列表不记录,因为随着认知的完善升级,自我主观感受会产生变化,无法做为匹配内容
      Node selfNode = new Node();
      selfNode.setZwData(selfFeel);
      //日志记录
      M.memera(JsonCovert.objToJson(selfNode), "自我-存在感",null);
      //对系统中得实时状态进行调控
      ZwRzUtil.ziwonenglitiaokong3(selfFeel);
      //当前场景活动记录自我
      lisActObj.setSelf(selfFeel);

      //记录场景经历
      Node hfNode = memera(back,"沟通-回复",null);
      //当前场景活动记录回答
      lisActObj.setHhd(hfNode);
     
      //日志记录
      selfNode.setZwData(selfFeelBack);//更新感受数据，然后进行存储
      M.memera(JsonCovert.objToJson(selfNode), "自我-存在感",null);
      //对系统中得实时状态进行调控
      ZwRzUtil.ziwonenglitiaokong3(selfFeelBack);
      //当前场景活动记录自我
      lisActObj.setSelf(selfFeel);
     
      for (Node node : trees) {
        node.memeraNewRz(node);
      }

      chatList.add(lisActObj);

      return back;
    }

   
    //TODO 没有结束标志，就会一直进行查询，因为一直能查到，会包含不断重复查询
    //需要记录相关内容，直到内容不在产生，就算结束
    //也就是对nodes进行控制，如果查询结果-存在新node节点，就对新node进行查询
    //否则结束
    private static void allXgRzQuery(String s,List<Node> nodes) {

      List<String> xgrzdy = Yy.bzfg(s);
      //对相关短语进行模糊匹配查询-此处似乎不存在问题，

      List<Map<String,Object>> xgrzList = mongoUtil.queryLike("rz", xgrzdy);
      //对查询结果进行递归查询
      List<Node> res = Node.docToNode(xgrzList);

      //对查询结果进行比对，如果存在不同，将不同进行查询，否则停止，不对已经完全存在元素进行查询
      //去除已存在的节点元素--可能是根据引用进行的判断，无法判断相同认知节点，都是不同对象
      //res.remove(nodes);//如果node已经包含所有查询元素节点，后续就不会进行循环查询
      //先简单实现：通过循环查找，然后判断添加，后期优化
      List<Node> temp = new ArrayList<>();

      for (int i = 0; i < res.size(); i++) {

        Node nodeTemp = res.get(i);
        
        Optional opRes = nodes.stream().filter(x->{

           return  x.getRz().equals(nodeTemp.getRz());

        }).findFirst();

        if(!opRes.isPresent()){//存在，则不处理，不存在，则添加，并继续查询
          temp.add(res.get(i));
          nodes.add(res.get(i));
        }
      }
      //将所有新元素添加入列表
      //nodes.addAll(res);
      //对新元素进行递归查询
      for (int i = 0; i < temp.size(); i++) {
        if(!"".equals(temp.get(i).getRz())){
         allXgRzQuery(temp.get(i).getRz(),nodes);
        }
      }
    }


    public List<String> readAllRz(){
      
      List<String> rzs =  lines();

      //保证读取认知时，不影响之前功能对认知格式进行解析简单认知文本

      List<String> res =  new ArrayList<>();
      for (String rz : rzs) {
         res.add(JsonCovert.jsonToObj(Node.class, rz).getRz());
      }

      return res;
    }


    public List<String> lines(){

      List res = new ArrayList<>();
      
      try {
        FileReader fr = new FileReader(file);
        BufferedReader br = new BufferedReader(fr);
        
        Stream<String> stream =  br.lines();

        res = new ArrayList<>(Arrays.asList(stream.toArray()));

        br.close();
        
      } catch (Exception e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
      }
      return res;
    }


}
