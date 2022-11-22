package com.gabia.mbaproject.infrastructure.local;

import com.gabia.mbaproject.model.Member;

public interface UserDefaultsContract {

    void save(Member member);
    Member getCurrentUser();
    void deleteCurrentUser();
}
