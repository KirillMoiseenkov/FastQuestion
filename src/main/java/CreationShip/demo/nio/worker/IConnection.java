package CreationShip.demo.nio.worker;

public interface IConnection {

    public void upStage();
    public void downStage();

    public void selectStage(int number);

    public void write();
    public String read();
}
