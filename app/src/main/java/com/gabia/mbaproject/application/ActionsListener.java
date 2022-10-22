package com.gabia.mbaproject.application;

public interface ActionsListener<I> {
    void edit(I item);
    void delete(I item);
}
