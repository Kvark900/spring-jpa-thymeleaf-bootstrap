package com.kemal.spring.web.searching;

import com.kemal.spring.web.dto.UserDto;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.data.domain.Page;

@Data
@AllArgsConstructor
public class UserSearchResult {
    private Page<UserDto> userDtoPage;
    private boolean hasNumberFormatException;
}
