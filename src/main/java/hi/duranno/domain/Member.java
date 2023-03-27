package hi.duranno.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Member {
    @Id @GeneratedValue
    @Column(name = "member_id")
    private Long id;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordinal_id")
    private Ordinal ordinal;

    /**
     * 연관관계 편의 메소드
     */
    public void changeOrdinal(Ordinal ordinal) {
        this.ordinal = ordinal;
        ordinal.getMembers().add(this);
    }
}
