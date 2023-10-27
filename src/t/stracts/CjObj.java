package t.stracts;

import java.util.List;

//场景对象
public class CjObj {
    
    //场景列表
    List<ActObj> cjList;

    Integer zhgs;//综合感受

    ActObj ppNode;//匹配的场景认知节点

    public List<ActObj> getCjList() {
        return cjList;
    }

    public void setCjList(List<ActObj> cjList) {
        this.cjList = cjList;
    }

    public Integer getZhgs() {
        return zhgs;
    }

    public void setZhgs(Integer zhgs) {
        this.zhgs = zhgs;
    }

    public ActObj getPpNode() {
        return ppNode;
    }

    public void setPpNode(ActObj ppNode) {
        this.ppNode = ppNode;
    }

}
