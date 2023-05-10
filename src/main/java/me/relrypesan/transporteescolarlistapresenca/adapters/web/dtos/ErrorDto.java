package me.relrypesan.transporteescolarlistapresenca.adapters.web.dtos;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ErrorDto {
    @JsonProperty("status")
    private Integer status;
    @JsonProperty("message")
    private String message;
    @JsonProperty("fields")
    private List<FieldErrorDto> fields;

    public void addField(FieldErrorDto fieldErrorDto) {
        if (this.fields == null) this.fields = new ArrayList<>();
        this.fields.add(fieldErrorDto);
    }
}
