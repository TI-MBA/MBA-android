package com.gabia.mbaproject.application.modules.admin.utils;

import com.gabia.mbaproject.model.PresenceRequest;

public interface SavePresenceListener {
    void save(PresenceRequest presence, int presenceID);
}
