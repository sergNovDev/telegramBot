package org.sergn.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.annotations.Type;
import org.hibernate.type.SqlTypes;
import org.telegram.telegrambots.meta.api.objects.Update;

//@Data
@Getter
@Setter
@EqualsAndHashCode(exclude = "id")
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name="raw_data")
public class RawData {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @JdbcTypeCode(SqlTypes.JSON)
    @Column(columnDefinition = "jsonb")
    private Update event;

}