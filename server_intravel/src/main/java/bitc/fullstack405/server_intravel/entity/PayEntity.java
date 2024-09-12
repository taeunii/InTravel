package bitc.fullstack405.server_intravel.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Table(name = "pay")
@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class PayEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long payId;

    private Long travId;

    private Long moneyId;

    private String payTitle;

    private Long plusAmt;

    private Long minusAmt;
}