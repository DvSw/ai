package t.stracts;

import java.util.ArrayList;
import java.util.List;

//高级认知对象,认知具备层级性,高层认知是对低层认知的解答,语法规则上也是如此
public class HighLevelRz {
    
    //关键内容认知
    private Node gjrz;

    //感受认知
    private SelfObj feel;

    //行为认知列表
    private List<SelfObj> feelList = new ArrayList<>();

    //行为认知列表
    private List<Node> actList = new ArrayList<>();

    
    public SelfObj getFeel() {
        return feel;
    }

    public void setFeel(SelfObj feel) {
        this.feel = feel;
    }

    public List<Node> getActList() {
        return actList;
    }

    public void setActList(List<Node> actList) {
        this.actList = actList;
    }

    public Node getGjrz() {
        return gjrz;
    }

    public void setGjrz(Node gjrz) {
        this.gjrz = gjrz;
    }

    public List<SelfObj> getFeelList() {
        return feelList;
    }

    public void setFeelList(List<SelfObj> feelList) {
        this.feelList = feelList;
    }

}
