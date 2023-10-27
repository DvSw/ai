package t.stracts;

public class RzObj {
    
    // 认知
    private String rz;


    //准确度
    private int zqd = 100;

    @Override
    public String toString() {

        return rz + "::" + zqd;
    }


    public RzObj(String objString) {
        String[] res = objString.split("::");
        this.rz = res[0];
        this.zqd = Integer.valueOf(res[1]);
    }

    public static String getRzText(String objString) {
        
       return objString.split("::")[0];
    }


    public RzObj() {
    }


    public String getRz() {
        return rz;
    }

    public void setRz(String rz) {
        this.rz = rz;
    }


    public int getZqd() {
        return zqd;
    }


    public void setZqd(int zqd) {
        this.zqd = zqd;
    }

    

}
