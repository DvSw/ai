package m;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

//主要记录一切活动的标准格式
public class Rizhi {

    public static void save(String jl) {
        Date now = new Date();
        String rz = now.toLocaleString() + ":==:" + jl;
        M.write(rz);
    }

    //简单活动文本记录，也就是活动+活动内容
    public static String saveHd(String hd,List list) {
        String rz = hd + "::" + list;
        save(rz);
        return rz;
    }

     //对于自我感受进行数值数据还原
     public static List<Integer> zwToList(String zw) {

        List<Integer> list = new ArrayList<>();
        zw = zw.replace(" ", "");
        String[] qzwData = zw.split("::");
        String[] qzw = qzwData[1].substring(1,qzwData[1].length()-1).split(",");

        for (String one : qzw) {
            list.add(Integer.valueOf(one));
        }

        return list;
    }


     //获取综合感受变化值-后状态对于前状态的变化
     public static Integer getChangeValue( List<Integer> qzw, List<Integer> hzw) {

        //正向状态变化值
        Integer zxbh = (hzw.get(3)+hzw.get(4)+hzw.get(5)) - (qzw.get(3)+qzw.get(4)+qzw.get(5));
        //负向状态变化值
        Integer fxbh = (hzw.get(0)+hzw.get(1)+hzw.get(2)) - (qzw.get(0)+qzw.get(1)+qzw.get(2));

        //负向状态变化要取反，表面变化是向负面变化的
        return zxbh+(-fxbh);
    }

    //获取日志任何活动后的携带数据
    public static String valueGet(String rz) {

        String[] data = rz.split("::");
        String value = data[1].substring(1,data[1].length()-1);

        return value;
    }
}
