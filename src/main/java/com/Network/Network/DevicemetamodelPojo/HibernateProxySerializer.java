package com.Network.Network.DevicemetamodelPojo;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.hibernate.proxy.HibernateProxy;

import java.io.IOException;

public class HibernateProxySerializer extends JsonSerializer<HibernateProxy> {

    @Override
    public void serialize(HibernateProxy value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        if (value != null) {
            // Unwrap the proxy object to get the actual entity
            Object entity = value.getHibernateLazyInitializer().getImplementation();
            // Serialize the entity
            serializers.defaultSerializeValue(entity, gen);
        } else {
            gen.writeNull();
        }
    }
}
