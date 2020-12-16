//package com.endava.statemachine.serde;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.fasterxml.jackson.databind.node.ArrayNode;
//import com.fasterxml.jackson.databind.node.JsonNodeFactory;
//import com.fasterxml.jackson.databind.node.ObjectNode;
//import com.fasterxml.jackson.databind.node.TextNode;
//import org.objenesis.Objenesis;
//import org.objenesis.ObjenesisStd;
//
//import java.lang.reflect.Field;
//import java.lang.reflect.Method;
//import java.util.*;
//
//public class JsonSerde {
//    public String serialize(Object instance) {
//        return toJson(instance).toPrettyString();
//    }
//
//    private ObjectNode toJson(Object instance) {
//        try {
//            ObjectNode rootNode = new ObjectNode(JsonNodeFactory.instance);
//            ObjectNode stateNode = new ObjectNode(JsonNodeFactory.instance);
//            rootNode.set("targetClass", new TextNode(instance.getClass().getName()));
//            rootNode.set("state", stateNode);
//            Class<?> contextClass = instance.getClass();
//            for (Field field : contextClass.getDeclaredFields()) {
//                field.setAccessible(true);
//                if (isCollection(field.getType())) {
//                    stateNode.set(field.getName(), serializeValue(field.get(instance)));
//                } else {
//                    ObjectNode fieldNode = new ObjectNode(JsonNodeFactory.instance);
//                    stateNode.set(field.getName(), fieldNode);
//                    Object fieldValue = field.get(instance);
//                    fieldNode.set("type", new TextNode(fieldValue != null ? fieldValue.getClass().getName() : field.getType().getName()));
//                    fieldNode.set("value", serializeValue(field.get(instance)));
//                }
//            }
//            return rootNode;
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private boolean isCollection(Class<?> type) {
//        return Collection.class.isAssignableFrom(type);
//    }
//
//    @SuppressWarnings("unchecked")
//    private JsonNode serializeValue(Object instance) {
//        if (instance instanceof String) {
//            return new TextNode((String) instance);
//        } else if (instance instanceof Collection) {
//            ArrayNode jsonNodes = new ArrayNode(JsonNodeFactory.instance);
//            Collection<Object> collection = (Collection<Object>) instance;
//            for (Object item : collection) {
//                jsonNodes.add(serializeValue(item));
//            }
//            return jsonNodes;
//        } else if (instance != null && instance.getClass().isEnum()) {
//            return new TextNode(instance.toString());
//        } //else if (instance != null) {
////            return toJson(instance);
////        }
//        // else if (instance instanceof Map) {
////            ObjectNode mapNodes = new ObjectNode(JsonNodeFactory.instance);
////            for (Map.Entry<Object, Object> entry : ((Map<Object, Object>) instance).entrySet()) {
////                mapNodes.set(entry.getKey().toString(), getValue(entry.getValue()));
////            }
////            return mapNodes;
////        }
//        return null;
//    }
//
//    @SuppressWarnings("unchecked")
//    public <T> T deserialize(String json, Class<T> clazz) {
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            JsonNode rootNode = objectMapper.readTree(json);
//            JsonNode stateNode = rootNode.get("state");
//            String resultingType = rootNode.get("targetClass").asText();
//            Class<?> resultingClass = Class.forName(resultingType);
//            Object instance = makeInstance(resultingClass);
//            Iterator<Map.Entry<String, JsonNode>> fields = stateNode.fields();
//            while (fields.hasNext()) {
//                Map.Entry<String, JsonNode> fieldEntry = fields.next();
//                JsonNode fieldNode = fieldEntry.getValue();
//                Field field = resultingClass.getDeclaredField(fieldEntry.getKey());
//                field.setAccessible(true);
//                if (fieldNode.isArray()) {
//                    field.set(instance, getCollection((ArrayNode) fieldNode, field.getType()));
//                } else {
//                    field.set(instance, getValueAs(fieldNode.get("value"), fieldNode.get("type").asText()));
//                }
//            }
//            return (T) instance;
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }
//
//    private Object makeInstance(Class<?> clazz) {
//        Objenesis objenesis = new ObjenesisStd();
//        return objenesis.newInstance(clazz);
//    }
//
//    private Object getCollection(ArrayNode arrayNode, Class<?> type) throws Exception {
//        if (List.class.isAssignableFrom(type)) {
//            List<Object> resultingList = new ArrayList<>();
//            for (int i = 0; i < arrayNode.size(); ++i) {
//                JsonNode node = arrayNode.get(i);
//                resultingList.add(getValueAs(node, null));
//            }
//            return resultingList;
//        }
//        return null;
//    }
//
//    private Object getValueAs(JsonNode value, String type) throws Exception {
//        if (type != null) {
//            Class<?> resultingClass = Class.forName(type);
//            if (resultingClass == String.class) {
//                return value.asText().equals("null") ? null : value.asText();
//            } else if (resultingClass.isEnum()) {
//                return getEnumValue(resultingClass, value.asText());
//            }
//        }
//        if (value.isTextual()) {
//            return value.asText();
//        }
//        return null;
//    }
//
//    private Enum<?> getEnumValue(Class<?> enumClass, String enumValue) throws Exception {
//        Method valueOfMethod = enumClass.getDeclaredMethod("valueOf", String.class);
//        valueOfMethod.setAccessible(true);
//        return (Enum<?>) valueOfMethod.invoke(null, enumValue);
//    }
//}
