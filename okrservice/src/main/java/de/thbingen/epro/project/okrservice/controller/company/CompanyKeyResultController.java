package de.thbingen.epro.project.okrservice.controller.company;

import java.lang.reflect.Field;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import de.thbingen.epro.project.okrservice.controller.Utils;
import de.thbingen.epro.project.okrservice.dtos.CompanyKeyResultDto;
import de.thbingen.epro.project.okrservice.dtos.KeyResultDto;
import de.thbingen.epro.project.okrservice.entities.keyresults.CompanyKeyResult;
import de.thbingen.epro.project.okrservice.entities.keyresults.KeyResultType;
import de.thbingen.epro.project.okrservice.entities.objectives.CompanyObjective;
import de.thbingen.epro.project.okrservice.repositories.CompanyKeyResultRepository;
import de.thbingen.epro.project.okrservice.repositories.CompanyObjectiveRepository;
import de.thbingen.epro.project.okrservice.repositories.CompanyRepository;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/company/{companyId}/objective/{objectiveId}/keyresult")
public class CompanyKeyResultController {





    private CompanyRepository companyRepository;

    private CompanyObjectiveRepository companyObjectiveRepository;

    private CompanyKeyResultRepository companyKeyResultRepository;

    @Autowired
    public CompanyKeyResultController(CompanyRepository companyRepository, CompanyKeyResultRepository companyKeyResultRepository,
                                        CompanyObjectiveRepository companyObjectiveRepository) {
        this.companyRepository = companyRepository;
        this.companyKeyResultRepository = companyKeyResultRepository;
        this.companyObjectiveRepository = companyObjectiveRepository;
    }






    @PostMapping
    public ResponseEntity<CompanyKeyResultDto> createCompanyKeyResult(@PathVariable @NonNull Number companyId,
                                                                   @PathVariable @NonNull Number objectiveId,
                                                                   @RequestBody @Valid CompanyKeyResultDto keyResultDto) throws Exception {
        KeyResultType keyResultType = new KeyResultType(keyResultDto.getType());
        CompanyObjective objective =
                Utils.getCompanyObjectiveFromRepository(companyRepository, companyId,
                        companyObjectiveRepository, objectiveId);
        CompanyKeyResult companyKeyResult = new CompanyKeyResult();
        companyKeyResult.setGoal(keyResultDto.getGoal());
        companyKeyResult.setTitle(keyResultDto.getTitle());
        companyKeyResult.setDescription(keyResultDto.getDescription());
        companyKeyResult.setCurrent(keyResultDto.getCurrent());
        companyKeyResult.setConfidenceLevel(keyResultDto.getConfidenceLevel());
        companyKeyResult.setObjective(objective);
        companyKeyResult.setType(keyResultType);

        companyKeyResultRepository.save(companyKeyResult);
        keyResultDto.setId(companyKeyResult.getId());
        return new ResponseEntity<>(keyResultDto, HttpStatus.OK);
    }

    @PatchMapping("{keyResultId}")
    public ResponseEntity<CompanyKeyResultDto> patchCompanyKeyResult(@PathVariable @NonNull Number companyId,
                                                               @PathVariable @NonNull Number objectiveId,
                                                               @PathVariable @NonNull Number keyResultId,
                                                               @RequestBody CompanyKeyResultDto keyResultDto)
            throws Exception {
        CompanyKeyResult oldCompanyKeyResult =
                Utils.getCompanyKeyResultFromRepository(companyRepository, companyId, companyObjectiveRepository,
                        objectiveId, companyKeyResultRepository, keyResultId);
        CompanyKeyResultDto oldKeyResultDto = new CompanyKeyResultDto(oldCompanyKeyResult);

        Field[] fields = KeyResultDto.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true); // Allow access to private fields
            Object value = field.get(keyResultDto);
            if(value != null) {
                field.set(oldKeyResultDto, value);
            }
            field.setAccessible(false);
        }
        fields = CompanyKeyResultDto.class.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true); // Allow access to private fields
            Object value = field.get(keyResultDto);
            if(value != null) {
                field.set(oldKeyResultDto, value);
            }
            field.setAccessible(false);
        }
        CompanyObjective objective =
                Utils.getCompanyObjectiveFromRepository(companyRepository, companyId,
                        companyObjectiveRepository, objectiveId);
        KeyResultType keyResultType = new KeyResultType(oldKeyResultDto.getType());
        oldCompanyKeyResult.setGoal(oldKeyResultDto.getGoal());
        oldCompanyKeyResult.setTitle(oldKeyResultDto.getTitle());
        oldCompanyKeyResult.setDescription(oldKeyResultDto.getDescription());
        oldCompanyKeyResult.setCurrent(oldKeyResultDto.getCurrent());
        oldCompanyKeyResult.setConfidenceLevel(oldKeyResultDto.getConfidenceLevel());
        oldCompanyKeyResult.setObjective(objective);
        oldCompanyKeyResult.setType(keyResultType);
        oldCompanyKeyResult.setRepresenters(oldKeyResultDto.getRepresenters());

        companyKeyResultRepository.save(oldCompanyKeyResult);
        return new ResponseEntity<>(oldKeyResultDto, HttpStatus.OK);
    }

    @GetMapping("keyresult")
    public ResponseEntity<List<CompanyKeyResultDto>> getAllCompanyKeyResults(@PathVariable @NonNull Number objectiveId) {
        List<CompanyKeyResult> companyKeyResults =
            companyKeyResultRepository.findAllByObjectiveId(objectiveId.longValue());
        return new ResponseEntity<>(companyKeyResults.stream()
                .map(CompanyKeyResultDto::new)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @GetMapping("{keyResultId}")
    public ResponseEntity<CompanyKeyResultDto> getCompanyKeyResult(@PathVariable @NonNull Number companyId,
                                                              @PathVariable @NonNull Number objectiveId,
                                                              @PathVariable @NonNull Number keyResultId) throws Exception {
        CompanyKeyResult companyKeyResult =
                Utils.getCompanyKeyResultFromRepository(companyRepository, companyId, companyObjectiveRepository,
                        objectiveId, companyKeyResultRepository, keyResultId);
        return new ResponseEntity<>(new CompanyKeyResultDto(companyKeyResult), HttpStatus.OK);
    }

    @DeleteMapping("{keyResultId}")
    public ResponseEntity<CompanyKeyResultDto> deleteCompanyKeyResult(@PathVariable @NonNull Number companyId,
                                                            @PathVariable @NonNull Number objectiveId,
                                                            @PathVariable @NonNull Number keyResultId) throws Exception {
        CompanyKeyResult companyKeyResult =
                Utils.getCompanyKeyResultFromRepository(companyRepository, companyId, companyObjectiveRepository,
                        objectiveId, companyKeyResultRepository, keyResultId);
        companyKeyResultRepository.deleteById(companyKeyResult.getId());
        return new ResponseEntity<>(new CompanyKeyResultDto(companyKeyResult), HttpStatus.OK);
    }








}
