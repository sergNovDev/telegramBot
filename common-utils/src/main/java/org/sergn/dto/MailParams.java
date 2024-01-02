package org.sergn.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class MailParams {
    private String id;
    private String emailTo;
}
