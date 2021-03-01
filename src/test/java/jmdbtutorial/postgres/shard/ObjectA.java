package jmdbtutorial.postgres.shard;

public class ObjectA {
    public String propertyA_1;
    public int propertyA_2;

    public ObjectA() {
    }

    public ObjectA(String propertyA_1) {
        this.propertyA_1 = propertyA_1;
    }

    public String getPropertyA_1() {
        return propertyA_1;
    }

    public void setPropertyA_1(String propertyA_1) {
        this.propertyA_1 = propertyA_1;
    }

    public int getPropertyA_2() {
        return propertyA_2;
    }

    public void setPropertyA_2(int propertyA_2) {
        this.propertyA_2 = propertyA_2;
    }
}
