package com.codeit.donggrina.common.api;

import java.util.List;

public record SearchFilter(
    String keyword,
    List<String> petNames,
    List<String> writerNames
) {

}
