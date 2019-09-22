package lt.metasite.waste.container;

import java.time.LocalDate;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/containers")
public class ContainerController {
    private final ContainerService service;
    private final ScheduleService scheduleService;

    public ContainerController(
            ContainerService service,
            ScheduleService scheduleService) {
        this.service = service;
        this.scheduleService = scheduleService;
    }

    @GetMapping(params = {"lon", "lat", "distance"})
    public List<Container> getContainersByGeoLocation(
            @RequestParam(name = "lon") Double longitude,
            @RequestParam(name = "lat") Double latitude,
            @RequestParam Double distance) {
        return service.findByPosition(longitude, latitude, distance);
    }

    @GetMapping(params = {"street","houseNo"})
    public List<Container> getContainersByAddress(
            @RequestParam String street,
            @RequestParam String houseNo,
            @RequestParam(required = false) String flatNo) {
        return service.findByAddress(street, houseNo, flatNo);
    }

    @GetMapping("/{containerNo}")
    public Container getContainer(@PathVariable String containerNo){
        return service.getContainer(containerNo);
    }

    @GetMapping("/{containerNo}/schedulex")
    public List<Schedule> getSchedule(@PathVariable String containerNo,
                                      @RequestParam(required = false) LocalDate dateFrom,
                                      @RequestParam(required = false) LocalDate dateTo){
        return scheduleService.getScheduleForContainer(containerNo,
                                                       dateFrom == null? LocalDate.now() : dateFrom,
                                                       dateTo == null? LocalDate.now().plusMonths(1).withDayOfMonth(1).minusDays(1): dateTo);
    }

    @GetMapping("/{containerNo}/history")
    public List<PickupHistory> getContainerPickupHistory(@PathVariable String containerNo){
        return service.getPickupHistory(containerNo);
    }

}
