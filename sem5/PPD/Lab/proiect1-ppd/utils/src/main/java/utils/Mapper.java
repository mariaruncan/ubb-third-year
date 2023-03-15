package utils;

import utils.domain.Planning;
import utils.domain.PlanningDto;

public class Mapper {
    public static Planning planningDtoToPlanning(PlanningDto planningDto) {
        return new Planning(planningDto.getName(),
                planningDto.getCnp(),
                planningDto.getDate(),
                planningDto.getLocation(),
                Container.treatmentRepository.findById(planningDto.getTreatment()),
                planningDto.getTreatmentDate(),
                planningDto.getHour());
    }
}
