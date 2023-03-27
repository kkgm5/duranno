package hi.duranno.domain.service;

import hi.duranno.domain.Assignment;
import hi.duranno.domain.Ordinal;
import hi.duranno.domain.repository.AssignmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AssignmentService {
    private final AssignmentRepository assignmentRepository;

    public Long save(Assignment assignment) {
        assignmentRepository.save(assignment);
        return assignment.getId();
    }

    public Assignment findById(Long id) {
        return assignmentRepository.findById(id);
    }

    public void update(Long assignmentId, Assignment updateParam) {
        assignmentRepository.update(assignmentId, updateParam);
    }

    public void remove(Assignment assignment) {
        assignmentRepository.remove(assignment);
    }

    public List<Assignment> search(int firstResult, int maxResult, Ordinal ordinal) {
        return assignmentRepository.search(firstResult, maxResult, ordinal);
    }


    public List<Assignment> searchByOrdinal(Ordinal ordinal) {
        return assignmentRepository.searchByOrdinal(ordinal);
    }

    public int allAssignmentCnt(Ordinal ordinal) {
        return assignmentRepository.allAssignmentCnt(ordinal);
    }
}
