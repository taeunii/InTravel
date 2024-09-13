package bitc.fullstack405.server_intravel.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "map")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MapEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long mapId;

    private Long travId;

    private String latitude;

    private String longitude;

    private String pinName;

    private String pinColor;
}
