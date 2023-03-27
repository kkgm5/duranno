package hi.duranno.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotEmpty;

@Entity
@Getter @Setter
@NoArgsConstructor
public class Board {
    @Id @GeneratedValue
    private Long id;
    private String writer;
    @NotEmpty
    private String title;
    @NotEmpty
    private String content;
    private String localDate;
    private Integer cnt = 0;
}
