package share.method2.State;

import share.method2.NewGumBall;

/**
 * @author yinchao
 * @Date 2019/10/17 01:11
 */
public class HasQuarterState implements State {
    private NewGumBall newGumBall;

    public HasQuarterState(NewGumBall newGumBall) {
        this.newGumBall = newGumBall;
    }

    @Override
    public void insertQuarter() {
        System.out.println("已投入硬币，请勿重复投入");
    }

    @Override
    public void dispense() {
        System.out.println("请转动曲柄");
    }

    @Override
    public void ejectQuarter() {
        System.out.println("退款成功");
        newGumBall.setState(newGumBall.getNoQuarterState());
    }

    @Override
    public void turnCrank() {
        System.out.println("转动曲柄，等待出货");
        newGumBall.setState(newGumBall.getGoingToBeProduceState());
    }

    @Override
    public String toString() {
        return "HasQuarterState";
    }
}
