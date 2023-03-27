package lab_8.entity;

import lombok.*;
import javax.persistence.*;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "actions")
public class Action {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String dateActions;

    @Column(nullable = false)
    private String description;

    public Action(String dateActions) {
        this.dateActions = dateActions;
    }
}
