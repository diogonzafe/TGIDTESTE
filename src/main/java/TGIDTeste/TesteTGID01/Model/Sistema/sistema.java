package TGIDTeste.TesteTGID01.Model.Sistema;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Table(name = "sistema")
@Entity(name = "sistema")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class sistema {
    @Id
    @Column(name = "id")
    private String id;

    @Column(name = "lucro")
    private Double lucro;
}