package me.ansandr.transwarp;

import me.ansandr.transwarp.model.TransportType;
import me.ansandr.transwarp.util.TransportNotFoundException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class TransportTypeManager {

    public static Map<String, TransportType> typeMap;
    public static List<String> typeList;

    public Map<String, TransportType> availableTypes;

    public TransportTypeManager(TransWarp plugin, List<String> typeList, Map<String, TransportType> typeMap) {
        TransportTypeManager.typeMap = typeMap;
        TransportTypeManager.typeList = typeList;
        availableTypes = putSetToMap(plugin.getStorage().getTransportTypes());
    }

    /**
     * Возвращает оптимальный типа транспорта, у которого есть локация, исходя по расстоянию
     * @param distance
     * @return TransportType
     */
    public TransportType getByDistance(double distance) throws TransportNotFoundException {

        for(TransportType type : availableTypes.values()) {
            //если minDistance <= distance <= maxDistance
            if (type.getMinDistance() <= distance && distance <= type.getMaxDistance()) {
                return type;
            }
        }
        if (availableTypes == null) throw new TransportNotFoundException();
        return null;
    }

    private Map<String, TransportType> putSetToMap(Set<String> transportTypes) {
        Map<String, TransportType> map = new HashMap<>();
        for (String s : transportTypes) {
            map.put(s, typeMap.get(s));//Имя машины, объект машины с таким именем
        }
        return map;
    }

    /**
     * Возвращает, задан ли данный вид транспорта в конфиге
     * @param transportTypeName
     */
    public static boolean isTypeExist(String transportTypeName) {
        return typeMap.containsKey(transportTypeName);
    }
}