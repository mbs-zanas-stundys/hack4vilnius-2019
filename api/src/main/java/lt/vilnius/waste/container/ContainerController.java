package lt.vilnius.waste.container;

import java.time.LocalDate;
import java.util.List;

import lt.metasite.waste.container.dto.*;

import lt.vilnius.waste.container.dto.ContainerForDateView;
import lt.vilnius.waste.container.dto.ContainerListView;
import lt.vilnius.waste.container.dto.ContainerPickupHistoryView;
import lt.vilnius.waste.container.dto.ContainerView;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/containers")
public class ContainerController {
    private final ContainerService service;

    public ContainerController(
            ContainerService service) {
        this.service = service;
    }

    public List<ContainerListView> getContainersByGeoLocation(
            @RequestParam(name = "lon") Double longitude,
            @RequestParam(name = "lat") Double latitude,
            @RequestParam Double distance) {
        return service.findByPosition(longitude, latitude, distance);
    }

    @GetMapping("/{containerNo}")
    public ContainerView getContainer(@PathVariable String containerNo) {
        return service.getContainerView(containerNo);
    }

    @GetMapping("/{containerNo}/history")
    public List<Pickup> getContainerPickupHistory(@PathVariable String containerNo,
                                                  @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        return service.getPickupHistory(containerNo, date);
    }

    @GetMapping("/pickup")
    public List<ContainerForDateView> getMissedContainers(@RequestParam(required = false) @DateTimeFormat(iso =
            DateTimeFormat.ISO.DATE) LocalDate date) {
        if(date == null){
            date = LocalDate.now();
        }
        return service.pickupsForDate(date);
    }


    @GetMapping("/low-ratio")
    public List<ContainerPickupHistoryView> lowRatioContainers(@RequestParam(required = false)
                                                               @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        if (date == null) {
            date = LocalDate.now();
        }
        return service.getLowRatioContainers(date);
    }

}
