package com.codeit.donggrina.domain.growth_history.dto.request;

import java.util.List;

public record SearchFilter(
    String keyword,
    List<String> petNames,
    List<String> writerNames
) {

}
