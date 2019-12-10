package share.method2.State;

/**
 * @author yinchao
 * @Date 2019/10/16 21:11
 */
public interface State {
    void insertQuarter();

    void dispense();

    void ejectQuarter();

    void turnCrank();
}
