package com.gabia.mbaproject.application;

import com.gabia.mbaproject.model.Member;

public interface SelectListener<M> {
    void didSelect(M model);
}
