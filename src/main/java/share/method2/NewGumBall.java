package share.method2;

import share.method2.State.*;

/**
 * @author yinchao
 * @Date 2019/10/16 21:10
 */
public class NewGumBall {
    private State state;
    private State goingToBeProduceState;
    private State HasQuarterState;
    private State NoCandyState;
    private State NoQuarterState;
    private int candyNumber = 0;

    NewGumBall(int candyNumber) {
        goingToBeProduceState = new GoingToBeProduceState(this);
        HasQuarterState = new HasQuarterState(this);
        NoCandyState = new NoCandyState(this);
        NoQuarterState = new NoQuarterState(this);

        if (candyNumber >= 0) {
            this.candyNumber = candyNumber;
            state = NoQuarterState;
        } else {
            state = NoCandyState;
        }
    }


    public void releaseCandy() {
        System.out.println("糖果已出货");
        if (candyNumber > 0) {
            candyNumber--;
        }
    }

    public void setState(State state) {
        this.state = state;
    }

    public int getCandyNumber() {
        return candyNumber;
    }

    public State getGoingToBeProduceState() {
        return goingToBeProduceState;
    }

    public State getHasQuarterState() {
        return HasQuarterState;
    }

    public State getNoCandyState() {
        return NoCandyState;
    }

    public State getNoQuarterState() {
        return NoQuarterState;
    }

    public void insertQuarter() {
        state.insertQuarter();
    }

    public void turnCrank() {
        state.turnCrank();
        state.dispense();
    }


    public void ejectQuarter() {
        state.ejectQuarter();
    }

    @Override
    public String toString() {
        return "state:" + state + "\t inventory:" + candyNumber;
    }
}
