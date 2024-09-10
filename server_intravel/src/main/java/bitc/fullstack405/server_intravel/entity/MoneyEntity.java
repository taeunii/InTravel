package bitc.fullstack405.server_intravel.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "money")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MoneyEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long moneyId;

    private Long travId;

    private String moneyTitle;

    private Long expenses;

}
