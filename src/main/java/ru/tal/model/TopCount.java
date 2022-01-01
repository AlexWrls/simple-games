package ru.tal.model;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class TopCount {
    private Long id;
    private String name;
    private Integer count;
}
