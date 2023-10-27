package t.worldAttr;

public class WorldObject extends Thread{
    
    //各类事物判断权重属性
    private int level = 100;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

}
