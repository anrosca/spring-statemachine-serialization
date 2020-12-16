//package com.endava.statemachine.serde;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import org.junit.jupiter.api.Test;
//
//import java.util.List;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//public class JsonSerdeTest {
//
//    private final JsonSerde jsonSerde = new JsonSerde();
//    private final ObjectMapper objectMapper = new ObjectMapper();
//
//    @Test
//    public void shouldBeAbleToSerializeToJson_objectWithStringFields() {
//        StringDummy dummy = new StringDummy("Mike", "Smith");
//
//        String actualJson = jsonSerde.serialize(dummy);
//
//        String expectedJson = """
//                {
//                  "targetClass" : "com.endava.statemachine.serde.JsonSerdeTest$StringDummy",
//                  "state" : {
//                    "firstName" : {
//                      "type" : "java.lang.String",
//                      "value" : "Mike"
//                    },
//                    "lastName" : {
//                      "type" : "java.lang.String",
//                      "value" : "Smith"
//                    }
//                  }
//                }""";
//        assertJsonEquals(expectedJson, actualJson);
//    }
//
//    @Test
//    public void shouldBeAbleToSerializeToJson_objectWithEnumFields() {
//        EnumDummy dummy = new EnumDummy(EnumDummy.Season.SPRING, "Mike");
//
//        String actualJson = jsonSerde.serialize(dummy);
//
//        String expectedJson = """
//                {
//                  "targetClass" : "com.endava.statemachine.serde.JsonSerdeTest$EnumDummy",
//                  "state" : {
//                    "season" : {
//                      "type" : "com.endava.statemachine.serde.JsonSerdeTest$EnumDummy$Season",
//                      "value" : "SPRING"
//                    },
//                    "name" : {
//                      "type" : "java.lang.String",
//                      "value" : "Mike"
//                    }
//                  }
//                }""";
//        assertJsonEquals(expectedJson, actualJson);
//    }
//
//    @Test
//    public void shouldBeAbleToDeserializeToJson_objectWithEnumFields() {
//        String json = """
//                {
//                  "targetClass" : "com.endava.statemachine.serde.JsonSerdeTest$EnumDummy",
//                  "state" : {
//                    "season" : {
//                      "type" : "com.endava.statemachine.serde.JsonSerdeTest$EnumDummy$Season",
//                      "value" : "SPRING"
//                    },
//                    "name" : {
//                      "type" : "java.lang.String",
//                      "value" : "Mike"
//                    }
//                  }
//                }""";
//
//        EnumDummy actualDeserializedInstance = jsonSerde.deserialize(json, EnumDummy.class);
//
//        EnumDummy expectedInstance = new EnumDummy(EnumDummy.Season.SPRING, "Mike");
//        assertEquals(actualDeserializedInstance, expectedInstance);
//    }
//
//    @Test
//    public void shouldBeAbleToSerializeToJson_objectWithListFields() {
//        ListDummy dummy = new ListDummy(List.of("Funky", "shit"));
//
//        String actualJson = jsonSerde.serialize(dummy);
//
//        String expectedJson = """
//                {
//                  "targetClass" : "com.endava.statemachine.serde.JsonSerdeTest$ListDummy",
//                  "state" : {
//                    "values" : [ "Funky", "shit" ]
//                  }
//                }""";
//        assertJsonEquals(expectedJson, actualJson);
//    }
//
//    @Test
//    public void shouldBeAbleToDeserializeToJson_objectWithListFields() {
//        String json = """
//                {
//                  "targetClass" : "com.endava.statemachine.serde.JsonSerdeTest$ListDummy",
//                  "state" : {
//                    "values" : [ "Funky", "shit" ]
//                  }
//                }""";
//
//        ListDummy actualDummy = jsonSerde.deserialize(json, ListDummy.class);
//
//        ListDummy expectedDummy = new ListDummy(List.of("Funky", "shit"));
//        assertEquals(expectedDummy, actualDummy);
//    }
//
//    @Test
//    public void shouldBeAbleToDeserializeToJson_objectWithStringFields() {
//        String json = """
//                {
//                  "targetClass" : "com.endava.statemachine.serde.JsonSerdeTest$StringDummy",
//                  "state" : {
//                    "firstName" : {
//                      "type" : "java.lang.String",
//                      "value" : "Mike"
//                    },
//                    "lastName" : {
//                      "type" : "java.lang.String",
//                      "value" : "Smith"
//                    }
//                  }
//                }""";
//
//        StringDummy actualDeserializedInstance = jsonSerde.deserialize(json, StringDummy.class);
//
//        assertEquals(new StringDummy("Mike", "Smith"), actualDeserializedInstance);
//    }
//
//    private void assertJsonEquals(String expectedJson, String actualJson) {
//        try {
//            assertEquals(objectMapper.readTree(expectedJson), objectMapper.readTree(actualJson));
//        } catch (JsonProcessingException e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    public static class StringDummy {
//
//        private String firstName;
//        private String lastName;
//
//        public StringDummy() {
//        }
//
//        public StringDummy(String firstName, String lastName) {
//            this.firstName = firstName;
//            this.lastName = lastName;
//        }
//
//        @Override
//        public boolean equals(Object o) {
//            if (this == o) return true;
//            if (o == null || getClass() != o.getClass()) return false;
//
//            StringDummy dummy = (StringDummy) o;
//
//            if (firstName != null ? !firstName.equals(dummy.firstName) : dummy.firstName != null) return false;
//            return lastName != null ? lastName.equals(dummy.lastName) : dummy.lastName == null;
//        }
//
//        @Override
//        public int hashCode() {
//            int result = firstName != null ? firstName.hashCode() : 0;
//            result = 31 * result + (lastName != null ? lastName.hashCode() : 0);
//            return result;
//        }
//
//        @Override
//        public String toString() {
//            return "StringDummy{" +
//                    "firstName='" + firstName + '\'' +
//                    ", lastName='" + lastName + '\'' +
//                    '}';
//        }
//    }
//
//    public static class ListDummy {
//        private List<String> values;
//
//        public ListDummy() {
//        }
//
//        public ListDummy(List<String> values) {
//            this.values = values;
//        }
//
//        @Override
//        public boolean equals(Object o) {
//            if (this == o) return true;
//            if (o == null || getClass() != o.getClass()) return false;
//
//            ListDummy listDummy = (ListDummy) o;
//
//            return values != null ? values.equals(listDummy.values) : listDummy.values == null;
//        }
//
//        @Override
//        public int hashCode() {
//            return values != null ? values.hashCode() : 0;
//        }
//    }
//
//    public static class EnumDummy {
//        enum Season {SPRING, WINTER}
//
//        private Season season;
//        private String name;
//
//        public EnumDummy(Season season, String name) {
//            this.season = season;
//            this.name = name;
//        }
//
//        public EnumDummy() {
//        }
//
//        @Override
//        public boolean equals(Object o) {
//            if (this == o) return true;
//            if (o == null || getClass() != o.getClass()) return false;
//
//            EnumDummy enumDummy = (EnumDummy) o;
//
//            if (season != enumDummy.season) return false;
//            return name != null ? name.equals(enumDummy.name) : enumDummy.name == null;
//        }
//
//        @Override
//        public int hashCode() {
//            int result = season != null ? season.hashCode() : 0;
//            result = 31 * result + (name != null ? name.hashCode() : 0);
//            return result;
//        }
//    }
//}
