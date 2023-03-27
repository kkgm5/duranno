package hi.duranno.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Textbook {
    @Id @GeneratedValue
    @Column(name = "textbook_id")
    private Long id;
    @NotEmpty
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
        ordinal.getTextbooks().add(this);
    }
}
