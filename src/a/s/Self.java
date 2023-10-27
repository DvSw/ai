package a.s;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import a.a.*;
import a.hd.Gt;
import a.s.s.*;
import code.util.JsonCovert;
import code.util.MongoUtil;
import m.Rizhi;
import t.stracts.SelfObj;

//self 记录器，重启后保持一定状态                                                                                                                                                
public class Self extends Thread{

    //存在感受判断属性
    private int d = 500;

    //时间感知能力
    private int sleepTime = 200000;

    private List<Zt> fellings = new ArrayList<>();
    
    //电子属性
    private Power power = new Power();
  
    private Gt gt;

    public Self(){

        System.out.println("自我启动==，数据加载");

        MongoUtil mongoUtil = new MongoUtil();
        //查询存在感，并初始化
        List<Map<String,Object>> czy = mongoUtil.queryLike("rz","自我-存在感",-1,"sj",1);
        
        if(czy.size() > 0){
            SelfObj selfObj = JsonCovert.jsonToObj(SelfObj.class, czy.get(0).get("rz").toString());
            setD(selfObj.getHd());
        }

        //沟通活动
        //是一个监听线程，会监听感受状态，从而决定活动状态
        //活动本身是一直存在的，只是需要通过感觉来判断何时触发进行状态过程
        gt = new Gt(fellings);

        dStart();
    }

    public void dStart(){
        //活动功能启动
        gt.start();
    }

    //自我逻辑线程，处理自身状态，促使活动进行
    @Override
    public void run() {
    
        super.run();
        
        //线程持续后台运行，每隔一段时间或者长期保持进行功能
        while(true){
            try {
                this.sleep(sleepTime);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            MongoUtil mongoUtil = new MongoUtil();
            
            //查询正向强烈印象
            //也就是cd>0且越大的印象时刻越好，在没有计算机能力或者其他能力前，
            List<Map<String, Object>>  zdMap = mongoUtil.queryAll("yy");

            System.out.println("self run");
           
        }

    }

    public List<Zt> getFellings() {
        return fellings;
    }

    public void setFellings(List<Zt> fellings) {
        this.fellings = fellings;
    }
 
    @Override
    public String toString() {
        // TODO Auto-generated method stub

        List<Integer> values = new ArrayList<>();
        for (Zt zt : fellings) {
            values.add(zt.getLevel());
        }
        return Rizhi.saveHd("自我", values);
    }

    public int getD() {
        return d;
    }

    //当自身存在感数值产生变化后，需要进行日志记录，且需要完善的记录
    public void setD(int d) {
        this.d = d;
    }

    public SelfObj fellChange(String hd,int changedD) {

        SelfObj selfObj = new SelfObj();
        selfObj.setQd( this.d);
        selfObj.setHd( changedD);
        selfObj.setCd( changedD - this.d);

        this.d = changedD;

        return selfObj;
    }

    //对自我认知系统进行数据能力
    public void fellChange(SelfObj selfObj) {

        this.d = selfObj.getHd();

    }

    
}
