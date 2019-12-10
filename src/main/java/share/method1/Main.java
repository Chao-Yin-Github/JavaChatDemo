package share.method1;

/**
 * @author yinchao
 * @Date 2019/10/16 19:24
 */
public class Main {
    public static void main(String[] args) {
        GumBall gumBall = new GumBall(2);
        System.out.println(gumBall.toString() + "\n==========");

        // 正常流程
        gumBall.insertQuarter();
        gumBall.turnCrank();
        System.out.println(gumBall.toString() + "\n==========");

        // 直接退款无效
        gumBall.ejectQuarter();
        System.out.println(gumBall.toString() + "\n==========");

        // 投钱再取出,再转动曲柄，有效
        gumBall.insertQuarter();
        gumBall.ejectQuarter();
        gumBall.turnCrank();
        System.out.println(gumBall.toString() + "\n==========");

        // 投钱再转动曲柄,再取出，无效
        gumBall.insertQuarter();
        gumBall.turnCrank();
        gumBall.ejectQuarter();
        System.out.println(gumBall.toString() + "\n==========");

        // 没货了
        gumBall.insertQuarter();
        gumBall.ejectQuarter();
        gumBall.turnCrank();
        System.out.println(gumBall.toString() + "\n==========");
    }
}
