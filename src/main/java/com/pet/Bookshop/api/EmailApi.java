package com.pet.Bookshop.api;

import com.pet.Bookshop.dto.EmailDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Email API", description = "API для работы с email")
public interface EmailApi {
    @Operation(
            description = "Отправка письма администратором на почту",
            summary = "Отправка письма администратором на почту"
    )
    String sendAdminEmail(
            @Parameter(description = "ДТО письма")
            EmailDto emailDto
    );
}
