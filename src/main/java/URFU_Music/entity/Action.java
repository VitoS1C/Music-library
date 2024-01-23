package URFU_Music.entity;

import jakarta.persistence.*;
import lombok.*;
import java.text.SimpleDateFormat;
import java.util.Date;

@Setter
@Getter
@AllArgsConstructor
@Entity
@Table(name = "actions")
public class Action {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Temporal(TemporalType.TIMESTAMP)
    private Date dateActions;

    @Column(nullable = false)
    private String description;

    @ManyToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private User user;

    @Transient
    private String dateTime;

    public Action() {
        dateActions = new Date();
        setDateTime();
    }

    public void setDateTime() {
        SimpleDateFormat formatter = new SimpleDateFormat();
        formatter.applyPattern("dd-MM-yyyy HH:mm");
        this.dateTime = formatter.format(dateActions);
    }

}
