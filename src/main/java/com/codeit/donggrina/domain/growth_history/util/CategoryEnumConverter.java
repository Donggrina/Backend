package com.codeit.donggrina.domain.growth_history.util;

import com.codeit.donggrina.domain.growth_history.entity.GrowthHistoryCategory;
import jakarta.persistence.AttributeConverter;

public class CategoryEnumConverter implements AttributeConverter<GrowthHistoryCategory, String> {

    @Override
    public String convertToDatabaseColumn(GrowthHistoryCategory growthHistoryCategory) {
        return growthHistoryCategory == null ? null : growthHistoryCategory.getValue();
    }

    @Override
    public GrowthHistoryCategory convertToEntityAttribute(String s) {
        return GrowthHistoryCategory.fromValue(s);
    }
}
