package jmdbtutorial.postgres.shard;

import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class Mapping_Test {

    @Test
    public void can_use_method_references() {

        ObjectB objectB = new ObjectB("foo");


        ObjectA objectA = applyMapper(objectB, ObjectBtoAMapper::mapObjectBToA);


        assertThat(objectA.propertyA_1, is(objectB.propertyB_1));

    }

    @Test
    public void declarative_form() {
        ObjectA objectA = new ObjectA("foo");
        ObjectB objectB = new ObjectB();

        map(objectA::getPropertyA_1, objectB::setPropertyB_1);
        map(objectA::getPropertyA_2, objectB::setPropertyB_2);

        assertThat(objectA.propertyA_1, is(objectB.propertyB_1));
        assertThat(objectA.propertyA_2, is(objectB.propertyB_2));
    }

    private static <T> void map(PropertyAccessor<T> getter, PropertyModifier<T> setter) {
        setter.set(getter.get());
    }



    @FunctionalInterface
    public interface PropertyAccessor<R> {
        public R get();
    }

    @FunctionalInterface
    public interface PropertyModifier<V> {
        public void set(V value);
    }

    @FunctionalInterface
    public interface Mapper<S, T> {
        T map(S source);
    }

    private ObjectA applyMapper(ObjectB objectB, Mapper<ObjectB, ObjectA> mapper) {
        return mapper.map(objectB);
    }


    public static class ObjectBtoAMapper {

        public static ObjectA mapObjectBToA(ObjectB objectB) {
            return new ObjectA(objectB.propertyB_1);
        }
    }

    public static class ObjectA {
        public  String propertyA_1;
        private int propertyA_2;

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

    public static class ObjectB {
        public  String propertyB_1;
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
}
