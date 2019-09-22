package lt.metasite.waste.system;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import javax.annotation.PostConstruct;

import lt.metasite.waste.commo.CsvUploadService;

import org.eclipse.jgit.api.Git;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GitService {

    private static final Logger LOGGER = LoggerFactory.getLogger(GitService.class);
    private final List<CsvUploadService> serviceList;
    @Value("${git-download.enabled}")
    private boolean enabled;

    @Value("${git-download.url}")
    private String gitUrl;

    public GitService(List<CsvUploadService> services) {
        this.serviceList = services;
    }

    @PostConstruct
    public void checkoutGit() throws IOException {
        if(!enabled){
            return;
        }
        Path tempDir = Files.createTempDirectory("waste");
        try {
            LOGGER.info("Cloning " + gitUrl + " into " + tempDir.toString());
            Git.cloneRepository()
               .setURI(gitUrl)
               .setDirectory(tempDir.toFile())
               .call();

            Files.walk(tempDir)
                 .filter(f -> f.toString().endsWith(".csv"))
                 .forEach(key -> serviceList.stream()
                                            .filter(i -> key.getFileName()
                                                            .toString()
                                                            .contains(i.getFilePattern()))
                                            .findFirst()
                                            .ifPresent(s -> s.parseFile(key)));

            System.out.println("Completed Cloning");
        } catch (Exception e) {
            System.out.println("Exception occurred while cloning repo");
            e.printStackTrace();
        }

    }
}
