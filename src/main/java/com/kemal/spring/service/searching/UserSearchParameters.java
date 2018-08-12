package com.kemal.spring.service.searching;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.Optional;

@Data
@AllArgsConstructor
public class UserSearchParameters {
    private Optional<String> usersProperty;
    private Optional<String> propertyValue;
    private Optional<Integer> pageSize;
    private Optional<Integer> page;

}
