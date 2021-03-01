package jmdbtutorial.postgres.shard;

public class ObjectB {
    public String propertyB_1;
    public int propertyB_2;

    public ObjectB() {
    }

    public ObjectB(String propertyB_1) {
        this.propertyB_1 = propertyB_1;
    }

    public String getPropertyB_1() {
        return propertyB_1;
    }

    public void setPropertyB_1(String propertyB_1) {
        this.propertyB_1 = propertyB_1;
    }

    public int getPropertyB_2() {
        return propertyB_2;
    }

    public void setPropertyB_2(int propertyB_2) {
        this.propertyB_2 = propertyB_2;
    }
}
