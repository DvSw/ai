package t.func.evt;

//没有进行封装，不具备足够通用性，最简原则
//类似组合模式对对象进行解耦
//日志：对自我感受状态变化进行记录
public class EventHan {


    //被感受对象持有，用于监听感受数据变化

    //统一处理方法：有点粗糙，应该使用多监听回调处理类进行分别处理，而非统一处理
    public void listene(int value,Object o){
       
    }

    //为所有感受状态注册属性变化事件监听
    public void regist(){
       
       
    }

    public void init(){
       
    }
}
