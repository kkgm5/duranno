package hi.duranno.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Assignment {
    @Id @GeneratedValue
    @Column(name = "assignment_id")
    private Long id;
    private String title;
    private String fileName;
    private String localDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordinal_id")
    private Ordinal ordinal;

    /**
     * 연관관계 편의 메소드
     */
    public void changeOrdinal(Ordinal ordinal) {
        this.ordinal = ordinal;
        ordinal.getAssignments().add(this);
    }
}
