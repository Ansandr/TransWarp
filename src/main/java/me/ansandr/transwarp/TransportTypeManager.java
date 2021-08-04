package me.ansandr.transwarp;

import me.ansandr.transwarp.model.TransportType;
import me.ansandr.transwarp.util.TransportNotFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import static me.ansandr.utils.message.MessageManager.tl;

public class TransportTypeManager {

    public final Logger LOGGER;

    public Map<String, TransportType> typeMap;

    public Map<String, TransportType> availableTypes;

    public TransportTypeManager(TransWarp plugin, Map<String, TransportType> typeMap) {
        LOGGER = plugin.getLogger();
        this.typeMap = typeMap;
        availableTypes = putSetToMap(plugin.getStorage().getTransportTypes());
    }

    /**
     * Возвращает оптимальный типа транспорта, у которого есть локация, исходя по расстоянию
     * @param distance
     * @return TransportType
     */
    public TransportType getByDistance(double distance) throws TransportNotFoundException {
        if (availableTypes == null) {
            LOGGER.warning("Transports is not configured in config");
            throw new TransportNotFoundException();
        }
        for(TransportType type : availableTypes.values()) {
            //minDistance <= distance <= maxDistance
            if (type.getMinDistance() >= distance) {
                throw new TransportNotFoundException(tl("error.transport_too_close"));
            }
            if  (distance >= type.getMaxDistance()) {
                throw new TransportNotFoundException(tl("error.transport_too_far"));
            }
            return type;
        }
        return null;
    }

    private Map<String, TransportType> putSetToMap(Set<String> transportTypes) {
        Map<String, TransportType> map = new HashMap<>();
        for (String s : transportTypes) {
            map.put(s, typeMap.get(s));//Имя машины, объект машины с таким именем
        }
        return map;
    }

    public Map<String, TransportType> getTypeMap() {
        return this.typeMap;
    }

    /**
     * Возвращает, задан ли данный вид транспорта в конфиге
     * @param transportTypeName
     */
    public boolean isTypeExist(String transportTypeName) {
        return typeMap.containsKey(transportTypeName);
    }
}
