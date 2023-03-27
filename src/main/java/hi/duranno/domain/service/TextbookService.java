package hi.duranno.domain.service;

import hi.duranno.domain.Ordinal;
import hi.duranno.domain.repository.TextbookRepository;
import hi.duranno.domain.Textbook;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class TextbookService {
    private final TextbookRepository textbookRepository;

    public Long save(Textbook textbook) {
        textbookRepository.save(textbook);
        return textbook.getId();
    }

    public Textbook findById(Long id) {
        return textbookRepository.findById(id);
    }

    public void update(Long textbookId, Textbook updateParam) {
        textbookRepository.update(textbookId, updateParam);
    }

    public void remove(Textbook textbook) {
        textbookRepository.remove(textbook);
    }

    public List<Textbook> search(int firstResult, int maxResult, Ordinal ordinal) {
        return textbookRepository.search(firstResult, maxResult, ordinal);
    }

    public int allTextbookCnt(Ordinal ordinal) {
        return textbookRepository.allTextbookCnt(ordinal);
    }
}
