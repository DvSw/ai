package t.stracts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import m.M;

public class  RzUitl {
 
    private static M m = new M();
    
    public static void main(String[] args) {

        List<String> list = new ArrayList<>();

        String  s1 = "你吃饭了吗?";
        String  s2 = "吃饭";
        String  s3 = "吃";
        String  s4 = "饭";
        list.add(s1);
        list.add(s2);
        list.add(s3);
        list.add(s4);

       // renzhiwanshan(list);

       
       List<String> rzs = m.readAllRz(); //全部认知
       List<String> aboutMsg = new ArrayList<>();

       quanmianhuiyi(rzs, "你好",aboutMsg);

       System.out.println(aboutMsg);
 
        //只构建了关联认知树，还有无关联认知元素，应当单独成一颗树
        
        //循环构造，直到全面认知中没有任何元素
        /**
        int count  = 0;
        while(rzs.size() > 0){

            List<Node> res = cons(rzs);

            for (int i = 0; i < res.size(); i++) {
                count ++;
                //
                System.out.println("第 "+(count)+" 颗相关认知树");
                Node node = res.get(i);
                System.out.println(node);
            }
    
        }*/
        
        /**List<String> rzs = cxrz(list);
        //联合增加新认知，然后继续抽象直至不在产生新认知
        while(rzs!= null && rzs.size() >0 ){
            list.addAll(rzs);
            rzs = cxrz(list);
        }
        System.out.println("finall ==== "+list);*/
        //
       
        //构造认知树存储于list,以便观察认知结构情况 
        
       
        /** list.add("1");
        list.add("2");
        list.add("3");
        list.add("4");
         
        for (int i = 0; i < list.size(); i++) {
            List copy = new ArrayList<>(Arrays.asList(Arrays.copyOf(list.toArray(), list.size())))  ;
            for (int j = 0; j < i; j++) {
                copy.remove(0);
            }
            //System.out.println(i+1);
            List<List> all = new ArrayList<>();
            qpl4(copy,new ArrayList<>(),all);
            all.add(0,Arrays.asList(i+1));
            System.out.println(all);
        }*/
        
    }

    //完全相关认知树功能能力
   /**public static List<Node> wanquanrenzhishu(List<String> rzs){

        //List copy = new ArrayList<>(Arrays.asList(Arrays.copyOf(rzs.toArray(), rzs.size())))  ;

        List<Node> all = new ArrayList<>();
        int count  = 0;

        while(rzs.size() > 0){

            List<Node> res = cons(rzs);


            
            all.addAll(res);
        }

        return all;
    } */ 

    //完全相关认知树功能能力:升级替换字符串为node节点
    public static List<Node> wanquanrenzhishu2(List<Node> rzs){

        //List copy = new ArrayList<>(Arrays.asList(Arrays.copyOf(rzs.toArray(), rzs.size())))  ;

        List<Node> all = new ArrayList<>();
        int count  = 0;

        while(rzs.size() > 0){

            List<Node> res = cons2(rzs);

         /**for (int i = 0; i < res.size(); i++) {
                count ++;
                //
                System.out.println("第 "+(count)+" 颗相关认知树");
                Node node = res.get(i);
                System.out.println(JsonCovert.objToJson(node));
            }  */ 

            
            all.addAll(res);
        }

        return all;
    }


    public static List<Node> strToNode(List<String> list){

            List<Node> res = new ArrayList<>();

            for (String rz : list) {
                Node node = new Node();
                node.setRz(rz);
                res.add(node);
            }

            return res;
    }

    //全组认知抽象本质能力，需要加入全排列能力，才能完成对所有认知组合进行抽象本质能力，即下列方法实现
    public static List<String> cxrz(List<String> list){//多项认知共同抽象依旧是最短认知匹配最大认知
     
        //如果认知数据只有一条，无法进行抽象本质，直接返回新的空认知集合即可
        if(list == null || list.size() < 2){
            return new ArrayList<>();
        }
        
        //不过需要便利所有认知，匹配全量认知，从而实现多项认知共同抽象
        List<String> res = new ArrayList<>();
        //找到最短认知，放到最前面，循环匹配抽象即可
        int  minPosition = 0;
        for (int i = 0; i < list.size()-1; i++) {//循环相连比较，便利所有得到最小
            if( list.get(i).length() < list.get(i+1).length()){
                minPosition = i;
            }else{
                minPosition = i+1;
            }
        }
        String s2 = list.get(minPosition);
        list.remove(minPosition);
        list.add(0, s2);

        //按长度间隔分解s2，和s1进行匹配
        for (int i = 1; i < s2.length(); i++) {//长度限制分割连续短语,不包含本身，属于认知去重
            //便利全部语句
            for (int j = 0; j+i <= s2.length(); j++) {
                String rz = s2.substring(j,j+i);
                //循环所有认知判断认知包含
                int allCount = 0;
                for(String s1 : list){
                    if(s1.contains(rz)){//认知存在构建认知树
                      allCount++;
                    }
                }
                //还原计数，清除认知
                if(allCount == list.size()){//存储认知即可，在启动时进行认知树构造加载即可
                    //抽象本质认知 
                    //组合扩展认知 
                    res.add(rz);
                }
                allCount = 0;
            }
            
        }

        return res;
    }
    
    public static void cxrz(String s1,String s2){//多项认知共同抽象依旧是最短认知匹配最大认知
        //不过需要便利所有认知，匹配全量认知，从而实现多项认知共同抽象

        //交换位置，保障s1最长
        if(s2.length() > s1.length()){//交换
            String temp = s1;
            s1 = s2;
            s2 = temp;
        }

        //按长度间隔分解s2，和s1进行匹配
        for (int i = 1; i <= s2.length(); i++) {//长度限制分割连续短语
            //便利全部语句
            for (int j = 0; j+i <= s2.length(); j++) {
                String rz = s2.substring(j,j+i);
                //判断认知包含
                if(s1.contains(rz)){//认知存在构建认知树
                    System.out.println(rz);
                }
            }
            
        }
    }


    public static void qpl4(List<String> list,List<String> zh,List<List> all){

        if(list.size() <= 0){
           
            return;
        }

        zh.add(list.get(0));
        list.remove(0);
       
     
        for (int i = 0; i < list.size(); i++) {
            List<String> now = new ArrayList<>();
            //装载以前数据
            now.addAll(zh);
            now.add(list.get(i));
            all.add(now);
        }

        for (int i = 0; i < list.size(); i++) {
            qpl4(list,zh,all);
        }
    }
  

  
    public Set<String> renzhiwanshan(List<String> list){

        Set<String> allXrz = new HashSet<>();

        //qpl
        for (int i = 0; i < list.size(); i++) {
            List copy = new ArrayList<>(Arrays.asList(Arrays.copyOf(list.toArray(), list.size())))  ;
            for (int j = 0; j < i; j++) {
                copy.remove(0);
            }
            //System.out.println(i+1);
            List<List> all = new ArrayList<>();

            qpl4(copy,new ArrayList<>(),all);

            all.add(0,Arrays.asList(list.get(i)));
            //System.out.println(all);
            //遍历all的所有元素进行cxrz方法进行抽象本质认知
            for (List one : all) {
              List<String> xrz =  cxrz(one);
              //System.out.println(xrz);
              allXrz.addAll(xrz);
            }
            
        }
        System.out.println(allXrz);
        //cxrz

        return allXrz;
    }
 
    public static List<Node> quanmianhuiyi2(List<Node> rzs,String reciveMsg,List<Node> all){
            
        Set<Node> xrz = new HashSet();

        //主体
        //查询相关认知--包含查询以及被包含查询
        //查询每次必然能匹配到，所有需要去重，也就是重复内容不进行重复添加
        //后续可以改为set容器，优化掉判断逻辑
        //理论上只对新查询出的认知进行递归查询
        for (Node rzNode : rzs) {

            String rz = rzNode.getRz();

            if((reciveMsg.contains(rz) || rz.contains(reciveMsg)) && !all.contains(rz)){
                xrz.add(rzNode);
            }
        }

        //对查询出的内容进行循环递归调用，直到不在有任何内容相匹配--全面遍历即可,仍旧需要中止判断，不然就会一直遍历
        //如果元素有变动，继续递归查询，否则中止,此次也可以不判断，直接继续循环，判断节约循环逻辑成本
        //取差集
        xrz.remove(all);
        //存储新元素
        all.addAll(xrz);
        //判断有无新元素，有就继续递归
        if(xrz.size() > 0){
            for (Node rzNode : xrz) {
                String rz = rzNode.getRz();
                quanmianhuiyi2(rzs, rz, all);
            }
        }
        
        return all;
    }
    //TODO 对空元素可能需要单独处理，是否应该存储空元素，存储的话，所有涉及到认知的地方都需要判空处理，简单实现不存储空元素，存储处做判断
    //递归是多次重复调用，涉及结果合并，最简单的方式是容器传递，避免合并过程
    //也就是引用传递，而非对结果返回后进行合并，可以极大提升性能和效率
    public static List<String> quanmianhuiyi(List<String> rzs,String reciveMsg,List<String> all){
        
        Set<String> xrz = new HashSet();

        //主体
        //查询相关认知--包含查询以及被包含查询
        //查询每次必然能匹配到，所有需要去重，也就是重复内容不进行重复添加
        //后续可以改为set容器，优化掉判断逻辑
        //理论上只对新查询出的认知进行递归查询
        for (String rz : rzs) {
            if((reciveMsg.contains(rz) || rz.contains(reciveMsg)) && !all.contains(rz)){
                xrz.add(rz);
            }
        }

        //对查询出的内容进行循环递归调用，直到不在有任何内容相匹配--全面遍历即可,仍旧需要中止判断，不然就会一直遍历
        //如果元素有变动，继续递归查询，否则中止,此次也可以不判断，直接继续循环，判断节约循环逻辑成本
        //取差集
        xrz.remove(all);
        //存储新元素
        all.addAll(xrz);
        //判断有无新元素，有就继续递归
        if(xrz.size() > 0){
            for (String rz : xrz) {
                quanmianhuiyi(rzs, rz, all);
            }
        }
        
        return all;
    }
   //相关认知树构造功能
   /**public static List<Node> huiyi(List<String> rzs,String reciveMsg){

        List<String> aboutMsg = new ArrayList<>();
        //查询相关认知--包含查询以及被包含查询
        quanmianhuiyi(rzs, reciveMsg, aboutMsg);
        //这里做递归查询所有相关的


        //构造认知树--根据信息量进行加载构造--后续涉及到场景相关性判断
        List<Node> trees = wanquanrenzhishu(aboutMsg);

        return trees;
    } */


    //建立关联子项==分层底部便利插入认知树构造法
   /** public static List<Node> cons(List<String> list){
        //涉及到元素分组，然后逐个组进行便利构建
        Map<Integer,List<String>> all = new HashMap();
        
        //便利所有即可完成构造，然后进行存储即可
        List<Node> trees = new ArrayList<>();

       
        //长度分组--并将构造用到的元素进行剔除
        //关联构造--未来还涉及非关联构造扩展功能能力
        //先获得长度数组--进行去重并排序
        List<Integer> lenthes = new ArrayList<>();
        for (int i = list.size()-1; i >= 0; i--) {
            if(!lenthes.contains(list.get(i).length())){
                lenthes.add(list.get(i).length()); 
            }
        }
        lenthes.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                // TODO Auto-generated method stub
                return o2-o1;
            }
            
        });
        //然后根据长度进行分组--先建立容器，一次遍历即可完成分组--map比较省事，一次遍历即可
        //分组都是map实现-map是无序的，要通过lenthes进行排序构造
        for (int i = 0; i < lenthes.size(); i++) {
            List one = new ArrayList<>();
            all.put(lenthes.get(i),one);
        }
        for (int j = 0; j < list.size(); j++) {
            int len = list.get(j).length();
            all.get(len).add(list.get(j));
         }

        //循环lenthes,map进行数据提取，list进行数据移除，进行构造
       
        for (int i = 0; i < 1; i++) {//构造头部顶级层级树进行存储

            List<String> leverList = all.get(lenthes.get(i));

            for (int j = 0; j < leverList.size(); j++) {

                 Node node = new Node();
                 trees.add(node);
                 node.setRz(leverList.get(j));

                 //移除头部元素
                 list.remove(leverList.get(j));
            }

          
        }
        
        //分层插入认知树构造法

        for (int i = 1; i < lenthes.size(); i++) {//层级循环-一层一棵树

            List<String> leverList = all.get(lenthes.get(i));

            //元素循环--包含同级关联构造，虽然不可能存在重复情况，但是代码会简练
            //然后便利所有node进行多树构造
            //仅仅在最高层级node添加进树的列表
            for (int j = 0; j < leverList.size(); j++) {
              
                //便利树列表进行所有认知构造--单个元素对所有树进行匹配插入，插入构造法
                for (Node tree : trees) {

                    Boolean isUsed = tree.gz(leverList.get(j));
                    //如果被使用，移除已使用元素--被使用元素，也应当做为头部元素
                    //不断生成全组合树
                    if(isUsed){
                       //list.remove(leverList.get(j));
                    }
                }

            } 
           
        }
       
        return trees;
    } */

    //建立关联子项==分层底部便利插入认知树构造法
    public static List<Node> cons2(List<Node> list){
        //涉及到元素分组，然后逐个组进行便利构建
        Map<Integer,List<Node>> all = new HashMap();
        
        //便利所有即可完成构造，然后进行存储即可
        List<Node> trees = new ArrayList<>();

       
        //长度分组--并将构造用到的元素进行剔除
        //关联构造--未来还涉及非关联构造扩展功能能力
        //先获得长度数组--进行去重并排序
        List<Integer> lenthes = new ArrayList<>();
        for (int i = list.size()-1; i >= 0; i--) {
            if(!lenthes.contains(list.get(i).getRz().length())){
                lenthes.add(list.get(i).getRz().length()); 
            }
        }
        lenthes.sort(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                // TODO Auto-generated method stub
                return o2-o1;
            }
            
        });
        //然后根据长度进行分组--先建立容器，一次遍历即可完成分组--map比较省事，一次遍历即可
        //分组都是map实现-map是无序的，要通过lenthes进行排序构造
        for (int i = 0; i < lenthes.size(); i++) {
            List one = new ArrayList<>();
            all.put(lenthes.get(i),one);
        }
        for (int j = 0; j < list.size(); j++) {
            int len = list.get(j).getRz().length();
            all.get(len).add(list.get(j));
         }

        //循环lenthes,map进行数据提取，list进行数据移除，进行构造
       
        for (int i = 0; i < 1; i++) {//构造头部顶级层级树进行存储

            List<Node> leverList = all.get(lenthes.get(i));

            for (int j = 0; j < leverList.size(); j++) {

                 Node node = new Node();
                 node = leverList.get(j);

                 trees.add(node);

                 //移除头部元素
                 list.remove(leverList.get(j));
            }

          
        }
        
        //分层插入认知树构造法

        for (int i = 1; i < lenthes.size(); i++) {//层级循环-一层一棵树

            List<Node> leverList = all.get(lenthes.get(i));

            //元素循环--包含同级关联构造，虽然不可能存在重复情况，但是代码会简练
            //然后便利所有node进行多树构造
            //仅仅在最高层级node添加进树的列表
            for (int j = 0; j < leverList.size(); j++) {
              
                //便利树列表进行所有认知构造--单个元素对所有树进行匹配插入，插入构造法
                for (Node tree : trees) {

                    Boolean isUsed = tree.gz(leverList.get(j));
                    //如果被使用，移除已使用元素--被使用元素，也应当做为头部元素
                    //不断生成全组合树
                    if(isUsed){
                       //list.remove(leverList.get(j));
                    }
                }

            } 
           
        }
       
        return trees;
    }

    public static int findMaxPosition(List<String> list){

        int maxPosition = 0;

        for (int i = 0; i < list.size()-1; i++) {//循环相连比较，便利所有得到最小
            if( list.get(i).length() > list.get(i+1).length()){
                maxPosition = i;
            }else{
                maxPosition = i+1;
            }
        }
        return maxPosition;
    }


}
