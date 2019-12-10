package share.method2.State;

import share.method2.NewGumBall;

/**
 * @author yinchao
 * @Date 2019/10/16 21:16
 */
public class NoCandyState implements State {
    private NewGumBall newgumBall;

    public NoCandyState(NewGumBall newGumBall) {
        this.newgumBall = newGumBall;
    }

    @Override
    public void insertQuarter() {
        System.out.println("售罄");
    }

    @Override
    public void dispense() {
        System.out.println("售罄");
    }

    @Override
    public void ejectQuarter() {
        System.out.println("售罄");

    }

    @Override
    public void turnCrank() {
        System.out.println("售罄");
    }

    @Override
    public String toString() {
        return "NoCandyState";
    }
}
