package com.example.cmproject.dto;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
public class MailDTO {
    private String toAddress;
    private String title;
    private String message;
    private String fromAddress;

}