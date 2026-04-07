package br.com.boleiroOn.config.infra.email.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EmailEntity {
    private String from;
    private String to;
    private String subject;
    private String text;
}
