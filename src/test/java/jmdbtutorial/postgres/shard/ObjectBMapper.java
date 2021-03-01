package jmdbtutorial.postgres.shard;

class ObjectBMapper {

     public static ObjectB mapFrom(ObjectA from) {
         ObjectB to = new ObjectB();
         mapField(from::getPropertyA_1, to::setPropertyB_1);
         mapField(from::getPropertyA_2, to::setPropertyB_2);
         return to;
     }

    private static <T> void mapField(Mapping_Test.PropertyAccessor<T> getter, Mapping_Test.PropertyModifier<T> setter) {
        setter.set(getter.get());
    }
}
