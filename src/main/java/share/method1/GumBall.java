package share.method1;

/**
 * @author yinchao
 * @Date 2019/10/16 18:38
 */
public class GumBall {
    /**
     * 糖果机状态
     */
    private State state;

    /**
     * 糖果数目
     */
    private int number;

    GumBall(int number) {
        if (number >= 0) {
            this.number = number;
            state = State.NO_QUARTER_STATE;
        } else {
            this.number = 0;
            state = State.NO_CANDY_STATE;
        }
    }

    /**
     * 投入一个25美分硬币买糖果
     */
    public void insertQuarter() {
        System.out.print("投入硬币");
        if (state == State.HAS_QUARTER_STATE) {
            System.out.println(",已经放有硬币，请稍候");
            return;
        }
        if (state == State.NO_CANDY_STATE) {
            System.out.println(",售罄");
            return;
        }
        if (state == State.GOING_TO_PRODUCE_STATE) {
            System.out.println(",正在出货，请等待");
            return;
        }
        /**
         * 允许投入硬币的情况
         */
        if (state == State.NO_QUARTER_STATE) {
            System.out.println(",成功投入一枚硬币");
            state = State.HAS_QUARTER_STATE;
        }
    }

    /**
     * 拿回硬币
     */
    public void ejectQuarter() {
        System.out.print("退款");
        if (state == State.NO_CANDY_STATE) {
            System.out.println(",已售罄");
        }
        if (state == State.NO_QUARTER_STATE) {
            System.out.println(",没有硬币");
        } else if (state == State.GOING_TO_PRODUCE_STATE) {
            System.out.println(",硬币已存入");
        } else if (state == State.HAS_QUARTER_STATE) {
            System.out.println(",退还硬币成功");
            state = State.NO_QUARTER_STATE;
        }
    }

    /**
     * 转动曲柄
     */
    public void turnCrank() {
        System.out.print("转动曲柄");
        if (state == State.NO_QUARTER_STATE) {
            System.out.println(",未付款，请先投入硬币");
            return;
        }
        if (state == State.NO_CANDY_STATE) {
            System.out.println("，已售罄");
            return;
        }
        if (state == State.GOING_TO_PRODUCE_STATE) {
            System.out.println("，请稍候正在出货");
            return;
        }
        if (state == State.HAS_QUARTER_STATE) {
            System.out.println();
            state = State.GOING_TO_PRODUCE_STATE;
            dispense();
        }
    }

    /**
     * 分发糖果
     */
    private void dispense() {
        System.out.print("分发糖果");
        if (state == State.NO_CANDY_STATE) {
            System.out.println(",没有糖果，无法出货");
        } else if (state == State.NO_QUARTER_STATE) {
            System.out.println(",没有硬币，请先付款");
        } else if (state == State.HAS_QUARTER_STATE) {
            System.out.println(",请先转动曲柄");
        } else if (state == State.GOING_TO_PRODUCE_STATE) {
            System.out.println(",出货成功");
            number--;
            if (number == 0) {
                System.out.println("糖果卖完了");
                state = State.NO_CANDY_STATE;
            } else {
                state = State.NO_QUARTER_STATE;
            }
        }
    }

    public String toString() {
        return "state:" + state + "\tinventory:" + number;
    }

    /**
     * 装填糖果
     *
     * @param number 新加入糖果数量
     */
    public void refill(int number) {
        System.out.println("before refill:" + this.number);
        this.number += number;
        if (number > 0 || state == State.NO_CANDY_STATE) {
            state = State.NO_QUARTER_STATE;
        }
        System.out.println("after refill:" + this.number);
    }

    public enum State {
        /**
         * 没有25美分
         */
        NO_QUARTER_STATE,

        /**
         * 有25美分
         */
        HAS_QUARTER_STATE,

        /**
         * 即将产生糖果
         */
        GOING_TO_PRODUCE_STATE,

        /**
         * 售罄
         */
        NO_CANDY_STATE
    }
}

