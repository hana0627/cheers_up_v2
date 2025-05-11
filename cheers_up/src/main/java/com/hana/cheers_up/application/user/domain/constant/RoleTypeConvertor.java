package com.hana.cheers_up.application.user.domain.constant;

import javax.persistence.AttributeConverter;
import java.util.Arrays;
import java.util.NoSuchElementException;

public class RoleTypeConvertor implements AttributeConverter<RoleType,String> {
    @Override
    public String convertToDatabaseColumn(RoleType attribute) {
        if(attribute == null) return null;
        return attribute.getRoleName();
    }

    @Override
    public RoleType convertToEntityAttribute(String dbData) {
        return Arrays.stream(RoleType.values())
                .filter(element -> element.getRoleName().equals(dbData))
                .findFirst().orElseThrow(() -> new NoSuchElementException("해당 값에 해당하는 enumType을 찾을 수 없습니다. dbData = {} " + dbData));
    }
}
