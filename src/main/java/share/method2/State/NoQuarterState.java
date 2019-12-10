package share.method2.State;

import share.method2.NewGumBall;

/**
 * @author yinchao
 * @Date 2019/10/17 01:03
 */
public class NoQuarterState implements State {
    private NewGumBall newGumBall;

    public NoQuarterState(NewGumBall newGumBall) {
        this.newGumBall = newGumBall;
    }

    @Override
    public void insertQuarter() {
        System.out.println("成功投入一枚硬币");
        newGumBall.setState(newGumBall.getHasQuarterState());
    }

    @Override
    public void dispense() {
        System.out.println("请先投入硬币");
    }

    @Override
    public void ejectQuarter() {
        System.out.println("没有硬币，退款失败");
    }

    @Override
    public void turnCrank() {
        System.out.println("请先投入硬币再转动曲柄");
    }

    @Override
    public String toString() {
        return "NoQuarterSate";
    }
}
