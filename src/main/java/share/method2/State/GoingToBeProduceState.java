package share.method2.State;

import share.method2.NewGumBall;

/**
 * @author yinchao
 * @Date 2019/10/17 01:08
 */
public class GoingToBeProduceState implements State {
    private NewGumBall newGumBall;

    public GoingToBeProduceState(NewGumBall newGumBall) {
        this.newGumBall = newGumBall;
    }

    @Override
    public void insertQuarter() {
        System.out.println("正在出货，请勿重复投币");
    }

    @Override
    public void dispense() {
        newGumBall.releaseCandy();
        if (newGumBall.getCandyNumber() > 0) {
            newGumBall.setState(newGumBall.getNoQuarterState());
        } else {
            System.out.println("最后一个糖果已卖出");
            newGumBall.setState(newGumBall.getNoCandyState());
        }
        System.out.println("出货成功");
    }

    @Override
    public void ejectQuarter() {
        System.out.println("退款失败，硬币已投入");
    }

    @Override
    public void turnCrank() {
        System.out.println("正在出货，请勿转动曲柄");
    }

    @Override
    public String toString() {
        return "GoingToBeProduceState";
    }
}
