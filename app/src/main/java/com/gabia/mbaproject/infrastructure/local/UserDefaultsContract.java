package com.gabia.mbaproject.infrastructure.local;

import com.gabia.mbaproject.model.User;

public interface UserDefaultsContract {

    void save(User user);
    User getCurrentUser();
    void deleteCurrentUser();
}
