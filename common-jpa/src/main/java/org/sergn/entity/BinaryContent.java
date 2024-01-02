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
@Table(name="binary_content")
public class BinaryContent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private byte[] fileAsArrayOfBytes;
}
