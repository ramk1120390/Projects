package com.Network.Network.Service;

import com.fasterxml.jackson.databind.JsonNode;

import java.io.Serializable;

public class KafaMessagedto implements Serializable {
    private String Action;
    private JsonNode Data;

    // Use volatile to ensure visibility of changes to the instance across threads
    private static volatile KafaMessagedto instance = null;

    // Private constructor to prevent instantiation
    private KafaMessagedto() {
    }

    // Static method to provide access to the singleton instance
    public static KafaMessagedto getInstance() {
        if (instance == null) {
            synchronized (KafaMessagedto.class) {
                if (instance == null) {
                    instance = new KafaMessagedto();
                }
            }
        }
        return instance;
    }

    public String getAction() {
        return Action;
    }

    public void setAction(String action) {
        Action = action;
    }

    public JsonNode getData() {
        return Data;
    }

    public void setData(JsonNode data) {
        Data = data;
    }
}
