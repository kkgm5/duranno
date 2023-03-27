package hi.duranno.domain.service;

import hi.duranno.domain.Ordinal;
import hi.duranno.domain.repository.OrdinalRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class OrdinalService {
    private final OrdinalRepository ordinalRepository;

    public void save(Ordinal ordinal) {
        ordinalRepository.save(ordinal);
    }

    @Transactional(readOnly = true)
    public Ordinal findByOrdinal(String ordinal) {
        return ordinalRepository.findByOrdinal(ordinal);
    }
}
