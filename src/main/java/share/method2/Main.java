package share.method2;

/**
 * @author yinchao
 * @Date 2019/10/16 22:48
 */
public class Main {
    public static void main(String[] args) {
        NewGumBall newGumBall = new NewGumBall(2);
        System.out.println(newGumBall.toString() + "\n##########");

        // 正常流程
        newGumBall.insertQuarter();
        newGumBall.turnCrank();
        System.out.println(newGumBall.toString() + "\n##########");

        // 直接退款无效
        newGumBall.ejectQuarter();
        System.out.println(newGumBall.toString() + "\n##########");

        // 投钱再取出,再转动曲柄，有效
        newGumBall.insertQuarter();
        newGumBall.ejectQuarter();
        newGumBall.turnCrank();
        System.out.println(newGumBall.toString() + "\n##########");

        // 投钱再转动曲柄,再取出，无效
        newGumBall.insertQuarter();
        newGumBall.turnCrank();
        newGumBall.ejectQuarter();
        System.out.println(newGumBall.toString() + "\n##########");

        // 没货了
        newGumBall.insertQuarter();
        newGumBall.ejectQuarter();
        newGumBall.turnCrank();
        System.out.println(newGumBall.toString() + "\n##########");
    }
}
