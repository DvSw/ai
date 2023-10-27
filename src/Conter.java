import a.a.*;
import a.h.*;
import a.s.*;
import m.*;
import t.*;
import t.stracts.ZwRzUtil;

public class Conter{

    Self self = new Self();
    
    public Conter(){
       
        //自我注入自我认知系统实现耦合
        ZwRzUtil.inject(self);
    }

}
//活动是个线程方法保持动态性和活跃性--根据自我状态不停进行调控


