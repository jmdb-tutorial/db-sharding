package jmdbtutorial.postgres.shard;

import org.junit.Test;

import static jmdbtutorial.postgres.shard.ObjectBMapper.mapFrom;
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

        ObjectB objectB = mapFrom(objectA);

        assertThat(objectA.propertyA_1, is(objectB.propertyB_1));
        assertThat(objectA.propertyA_2, is(objectB.propertyB_2));
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

}
