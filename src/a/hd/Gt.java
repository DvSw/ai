package a.hd;

import java.util.List;
import java.util.Scanner;

import a.s.s.Zt;
import m.M;

//沟通--语言功能
public class Gt extends Hd{

    
    //沟通能力的场景时间间隔--场景会话感知能力
    private Long gtJg = 0l;

    //上一次活动-会话时间
    private Long lastChatTime = 0L;
    

    //存储交互
    private M m = new M();

    public Gt(List<Zt> fellings) {
       
    }

    

    //输入输出型线程不需要时间等待，自动输入等待控制即可
    @Override
    public void run() {
        super.run();

        while(true){
            
            System.out.println("沟通后台进行,请说些什么");
            
            talk(null);
        }
        
    }


    //说话
    public void talk(Zt zt){
        
        Scanner scanner = new Scanner(System.in);

        String rz =  scanner.nextLine();

        //对上此活动时间进行比对,从而拥有时间感知能力,感知就是比对能力,后期抽象
        //应该具备时间间隔感知能力,从而通过时间间隔进行各种判断
        Long temp = System.currentTimeMillis();
        gtJg = temp - lastChatTime;

        //如果间隔时间长达五分钟,则停止活动,此处需要升级为从认知中获取相关数据能力
        //String back = m.remanber4("大概过多久算是沟通结束");
        //从认知中解析数据能力
        Long min = gtJg / (60*1000);
        if(min > 5 ){//算作活动场景中止,清除上次活动内容
            M.chatList.clear();
        }

        //认知存储以及加载相关认知回馈--升级后根据场景回忆进行交互
        String back = m.remanber4(rz);

        System.out.println(back);
    }

    
}
