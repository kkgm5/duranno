package hi.duranno.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Ordinal {

    @Id @GeneratedValue
    @Column(name = "ordinal_id")
    private Long id;
    private String ordinal;

    @OneToMany(mappedBy = "ordinal")
    private List<Member> members = new ArrayList<>();
    @OneToMany(mappedBy = "ordinal")
    private List<Assignment> assignments = new ArrayList<>();
    @OneToMany(mappedBy = "ordinal")
    private List<Textbook> textbooks = new ArrayList<>();
}
