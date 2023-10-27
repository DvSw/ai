package t.stracts;

import java.util.Arrays;
import java.util.List;

import a.s.Self;

//感受权重系统工具
public class ZwRzUtil {

     //实时绑定内存数据
     public static Self self;

     public static void inject(Self injectSelf){
         self = injectSelf;
     }

     public static void ziwonenglitiaokong3(SelfObj feel){//入参是相关认知,具体认知

         self.fellChange("自我-存在感",self.getD() + 1);

     }


     public static SelfObj ziwoganshoushuju(Node node){//入参是相关认知,具体认知

        SelfObj selfObj = new SelfObj();

        if(null == node){
            return null;
        }
        
        //TODO 实际感受词，应该是语言系统内部查询，先简单实现
        List<String> zxEmo = Arrays.asList(
        "happy","satisfaction");
        List<String> fxEmo = Arrays.asList("sadness","fear");

       Node qxNode = null;

       qxNode = node.findZwRz(node, zxEmo);

       //TODO 需要根据程度副词进行调控幅度控制
       //感受变化时，将整个状态变化全部保存，子对象并不影响mongo json的查询
       if(qxNode != null){//说明存在正向感受，调节并记录正向感受

            selfObj.setQd(self.getD());
            selfObj.setHd(self.getD() + 1);
            selfObj.setCd(1);

            return selfObj;
       }

       qxNode = null;

       qxNode = node.findZwRz(node, fxEmo);

       if(qxNode != null){//说明存在负向感受，调节并记录正向感受
            
            selfObj.setQd(self.getD());
            selfObj.setHd(self.getD() - 1);
            selfObj.setCd(-1);

            return selfObj;
       }
    
    
       return selfObj;

     }

}
