package com.pet.Bookshop.api;

import com.pet.Bookshop.dto.EmailDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "Email API", description = "API для работы с email")
public interface EmailApi {

    @Operation(
            description = "Отправка письма администратором на почту",
            summary = "Отправка письма администратором на почту"
    )
    String sendAdminEmail(
            @RequestBody @Valid @Parameter(description = "ДТО письма")
            @io.swagger.v3.oas.annotations.media.Schema(
                    example = "{\"to\": \"victoryagraz@gmail.com\", \"subject\": \"Check email api\", \"text\": \"Hello\"}")
            EmailDto emailDto
    );

}
