package SkillSync.application.api;

import SkillSync.application.entity.FieldOfStudy;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/fields-of-study")
public class FieldOfStudyController {

    @GetMapping
    public List<String> getFieldsOfStudy() {
        // Return the names of all enum values
        return Arrays.stream(FieldOfStudy.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }
}
