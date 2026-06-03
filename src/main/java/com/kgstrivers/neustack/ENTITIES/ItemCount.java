package com.kgstrivers.neustack.ENTITIES;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
@AllArgsConstructor
public class ItemCount {
    private String item;
    private int count;
}
