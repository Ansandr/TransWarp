package me.ansandr.transwarp;

import me.ansandr.transwarp.model.Transport;
import me.ansandr.transwarp.model.TransportType;
import me.ansandr.transwarp.util.TransportNotFoundException;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Logger;

import static me.ansandr.utils.message.MessageManager.tl;

public class TransportTypeManager {

    private final TransWarp plugin;
    private static Logger LOGGER = TransWarp.getInstance().getLogger();

    public Map<String, TransportType> typeMap;

    public TransportTypeManager(TransWarp plugin, Map<String, TransportType> typeMap) {
        this.plugin = plugin;
        this.typeMap = typeMap;
    }

    /**
     * Возвращает оптимальный типа транспорта, у которого есть локация, исходя по расстоянию
     * @param distance
     * @return TransportType
     */
    public TransportType getByDistance(double distance) throws TransportNotFoundException {
        Map<String, TransportType> availableTypes = getAvailableTypes();
        if (availableTypes == null) {
            LOGGER.warning("Transports is not configured in config");
            throw new TransportNotFoundException();
        }
        for(TransportType type : availableTypes.values()) {
            if (type.getMinDistance() == 0 && type.getMinDistance() == 0) continue;
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

    public TransportType getByName(String transportType) throws TransportNotFoundException {
        Map<String, TransportType> availableTypes = getAvailableTypes();
        if (availableTypes == null) {
            LOGGER.warning("Transports is not configured in config");
            throw new TransportNotFoundException();
        }
        return availableTypes.get(transportType);
    }

    public void addType(String name, TransportType type) {
        typeMap.put(name, type);
    }

    private Map<String, TransportType> getAvailableTypes() {
        Map<String, TransportType> map = new HashMap<>();
        for (String s : plugin.getStorage().getTransportTypes()) {
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
