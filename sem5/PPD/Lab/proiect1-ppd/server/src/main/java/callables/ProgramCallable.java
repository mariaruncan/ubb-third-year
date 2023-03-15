package callables;

import utils.Container;
import utils.domain.Planning;
import utils.domain.ProgramResponse;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicInteger;

public class ProgramCallable implements Callable<ProgramResponse> {
    private final Planning planning;
    private final int[][] N;

    public ProgramCallable(Planning planning, int[][] N) {
        this.planning = planning;
        this.N = N;
    }

    @Override
    public ProgramResponse call() throws Exception {
        LocalDateTime start = LocalDateTime.now().withHour(planning.getHour());
        LocalDateTime finish = start.plusMinutes(planning.getTreatment().getDuration());
        synchronized (Container.planningRepository) {
            List<Planning> plannings = getTreatmentsByIdAndLocation(planning.getTreatment().getId(), planning.getLocation());
            AtomicInteger count = new AtomicInteger();
            plannings.forEach(p -> {
                LocalDateTime startTmp = LocalDateTime.now().withHour(p.getHour());
                LocalDateTime finishTmp = startTmp.plusMinutes(p.getTreatment().getDuration());
                if ((start.isAfter(startTmp) || start.isEqual(startTmp)) &&
                        (finish.isBefore(finishTmp) || finish.equals(finishTmp)))
                    count.getAndIncrement();
                else if (!((start.isAfter(startTmp) && start.isBefore(finishTmp)) ||
                        (finish.isAfter(startTmp) && finish.isBefore(finishTmp)))) {
                    count.getAndIncrement();
                }
            });

            if (count.get() < N[planning.getLocation()][planning.getTreatment().getId()]) {
                Container.planningRepository.save(planning);
                return new ProgramResponse(Container.planningRepository.findLast().getId(), planning.getTreatment().getCost());
            }

            return null;
        }
    }

    private List<Planning> getTreatmentsByIdAndLocation(Integer treatmentId, Integer location) {
        return Container.planningRepository.findAll()
                .stream().filter(p -> Objects.equals(p.getTreatment().getId(), treatmentId) &&
                        Objects.equals(p.getLocation(), location)).toList();
    }
}
