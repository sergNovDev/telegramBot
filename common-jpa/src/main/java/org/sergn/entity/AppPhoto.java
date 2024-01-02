package org.sergn.entity;

import jakarta.persistence.*;
import lombok.*;

@Setter
@Getter
@EqualsAndHashCode(exclude = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="app_photo")
public class AppPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String telegramFileId;
    @OneToOne
    private BinaryContent binaryContent;
    private String mimeType;
    private Integer fileSize;
}
