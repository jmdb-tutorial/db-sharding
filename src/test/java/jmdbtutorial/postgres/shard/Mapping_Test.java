package jmdbtutorial.postgres.shard;

import org.junit.Test;

import java.util.Arrays;
import java.util.function.Function;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class Mapping_Test {

    @Test
    public void can_use_method_references() {

        ObjectB objectB = new ObjectB("foo");


        ObjectA objectA = applyMapper(objectB, ObjectBtoAMapper::mapObjectBToA);


        assertThat(objectA.propertyA_1, is(objectB.propertyB_1));

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
        public final String propertyA_1;

        public ObjectA(String propertyA_1) {
            this.propertyA_1 = propertyA_1;
        }
    }

    public static class ObjectB {
        public final String propertyB_1;

        public ObjectB(String propertyB_1) {
            this.propertyB_1 = propertyB_1;
        }
    }
}
