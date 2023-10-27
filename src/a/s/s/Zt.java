package a.s.s;

import t.func.evt.EventHan;
import t.worldAttr.WorldObject;

//
public class Zt extends WorldObject{

    public Zt(){

    }

    //持有状态值监听事件，从而实现对状态值的持续监听
    private EventHan eventHan;
    
    //感觉状态，逻辑功能通过此数值进行感觉判断
    private int level = 100;

    //正负面感受区分-子类进行重写
    private boolean goodOrBad = true;


    //感受变化幅度:变化速率
    private int bhsl = 1;


    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
        eventHan.listene(level, this);
    }

    public EventHan getEventHan() {
        return eventHan;
    }

    public void setEventHan(EventHan eventHan) {
        this.eventHan = eventHan;
    }

    public boolean isGoodOrBad() {
        return goodOrBad;
    }

    public void setGoodOrBad(boolean goodOrBad) {
        this.goodOrBad = goodOrBad;
    }

    public int getBhsl() {
        return bhsl;
    }

    public void setBhsl(int bhsl) {
        this.bhsl = bhsl;
    }

    
}
